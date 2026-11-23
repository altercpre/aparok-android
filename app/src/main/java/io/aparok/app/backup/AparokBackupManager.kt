package io.aparok.app.backup

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import timber.log.Timber

/**
 * Sovereign encrypted backup & restore manager for Aparok (Phase R9).
 * Encapsulates the entire storage directory into a single encrypted `.aparok` archive.
 *
 * Header Specification:
 * - 8 bytes: Magic header "APAROK01"
 * - 16 bytes: Cryptographic salt
 * - 12 bytes: AES-GCM IV (nonce)
 * - Remainder: Encrypted Zip stream with 128-bit authentication tag at end
 */
class AparokBackupManager {

    companion object {
        private val MAGIC_HEADER = "APAROK01".toByteArray(StandardCharsets.US_ASCII)
        private const val SALT_LENGTH = 16
        private const val IV_LENGTH = 12
        private const val PBKDF2_ITERATIONS = 100_000
        private const val KEY_LENGTH_BITS = 256
        private const val GCM_TAG_LENGTH_BITS = 128
        private const val CIPHER_TRANSFORMATION = "AES/GCM/NoPadding"
    }

    private val secureRandom = SecureRandom()

    /**
     * Creates an encrypted `.aparok` backup archive from a source storage directory.
     */
    fun createBackup(sourceDir: File, targetBackupFile: File, passphrase: CharArray): Boolean {
        require(sourceDir.exists() && sourceDir.isDirectory) { "Source directory does not exist: ${sourceDir.absolutePath}" }
        targetBackupFile.parentFile?.mkdirs()

        val salt = ByteArray(SALT_LENGTH).also { secureRandom.nextBytes(it) }
        val iv = ByteArray(IV_LENGTH).also { secureRandom.nextBytes(it) }
        val secretKey = deriveKey(passphrase, salt)

        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv))

        return try {
            FileOutputStream(targetBackupFile).use { fos ->
                // 1. Write Header: Magic + Salt + IV
                fos.write(MAGIC_HEADER)
                fos.write(salt)
                fos.write(iv)

                // 2. Stream through Cipher -> Zip
                CipherOutputStream(fos, cipher).use { cos ->
                    ZipOutputStream(cos).use { zos ->
                        zipDirectory(sourceDir, sourceDir, zos)
                    }
                }
            }
            Timber.i("Successfully created encrypted backup at: ${targetBackupFile.absolutePath}")
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to create encrypted backup")
            targetBackupFile.delete()
            false
        }
    }

    /**
     * Restores an encrypted `.aparok` backup into a target storage directory.
     * Throws SecurityException if the passphrase is incorrect or archive is tampered with.
     */
    fun restoreBackup(backupFile: File, targetDir: File, passphrase: CharArray): Boolean {
        require(backupFile.exists() && backupFile.isFile) { "Backup file does not exist: ${backupFile.absolutePath}" }
        targetDir.mkdirs()

        return try {
            FileInputStream(backupFile).use { fis ->
                // 1. Verify Magic
                val magic = ByteArray(MAGIC_HEADER.size)
                val readMagic = fis.read(magic)
                if (readMagic != MAGIC_HEADER.size || !magic.contentEquals(MAGIC_HEADER)) {
                    throw IllegalArgumentException("Not a valid .aparok archive format")
                }

                // 2. Read Salt & IV
                val salt = ByteArray(SALT_LENGTH)
                val readSalt = fis.read(salt)
                val iv = ByteArray(IV_LENGTH)
                val readIv = fis.read(iv)
                if (readSalt != SALT_LENGTH || readIv != IV_LENGTH) {
                    throw IllegalArgumentException("Corrupted backup archive header")
                }

                // 3. Derive Key & Init Cipher
                val secretKey = deriveKey(passphrase, salt)
                val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv))

                // 4. Stream Decrypt -> Unzip
                try {
                    CipherInputStream(fis, cipher).use { cis ->
                        ZipInputStream(cis).use { zis ->
                            unzipToDirectory(zis, targetDir)
                        }
                    }
                } catch (e: Exception) {
                    throw SecurityException("Invalid backup passphrase or tampered archive", e)
                }
            }
            Timber.i("Successfully restored encrypted backup to: ${targetDir.absolutePath}")
            true
        } catch (e: SecurityException) {
            Timber.e(e, "Decryption failed during backup restore")
            throw e
        } catch (e: IllegalArgumentException) {
            Timber.e(e, "Invalid backup archive format")
            throw e
        } catch (e: Exception) {
            Timber.e(e, "Failed to restore backup")
            false
        }
    }

    private fun deriveKey(passphrase: CharArray, salt: ByteArray): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(passphrase, salt, PBKDF2_ITERATIONS, KEY_LENGTH_BITS)
        val keyBytes = factory.generateSecret(spec).encoded
        return SecretKeySpec(keyBytes, "AES")
    }

    private fun zipDirectory(rootDir: File, currentDir: File, zos: ZipOutputStream) {
        val files = currentDir.listFiles() ?: return
        val buffer = ByteArray(8192)
        for (file in files) {
            if (file.isDirectory) {
                zipDirectory(rootDir, file, zos)
            } else {
                val relativePath = rootDir.toURI().relativize(file.toURI()).path
                zos.putNextEntry(ZipEntry(relativePath))
                FileInputStream(file).use { fis ->
                    var count: Int
                    while (fis.read(buffer).also { count = it } != -1) {
                        zos.write(buffer, 0, count)
                    }
                }
                zos.closeEntry()
            }
        }
    }

    private fun unzipToDirectory(zis: ZipInputStream, targetDir: File) {
        val buffer = ByteArray(8192)
        var entry: ZipEntry? = zis.nextEntry
        while (entry != null) {
            val file = File(targetDir, entry.name)
            // Prevent zip slip vulnerability
            if (!file.canonicalPath.startsWith(targetDir.canonicalPath)) {
                throw SecurityException("Zip slip detected in entry: ${entry.name}")
            }
            if (entry.isDirectory) {
                file.mkdirs()
            } else {
                file.parentFile?.mkdirs()
                FileOutputStream(file).use { fos ->
                    var count: Int
                    while (zis.read(buffer).also { count = it } != -1) {
                        fos.write(buffer, 0, count)
                    }
                }
            }
            zis.closeEntry()
            entry = zis.nextEntry
        }
    }
}
