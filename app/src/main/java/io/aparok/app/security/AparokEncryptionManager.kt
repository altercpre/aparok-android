package io.aparok.app.security

import android.content.Context
import android.content.SharedPreferences
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import timber.log.Timber

/**
 * Sovereign at-rest encryption manager for Aparok.
 * Implements Phase R7 requirements:
 * - Master key and credentials protected by Android Keystore and EncryptedSharedPreferences (AES-256-GCM)
 * - 256-bit database passphrase generation and secure storage for SQLCipher / SQLite
 * - Streaming AEAD (AES-256-GCM, 128-bit auth tag) for media and file encryption at rest
 * - Zero plaintext leakage and tamper verification
 */
class AparokEncryptionManager(
    private val keystoreManager: KeystoreManager
) {

    companion object {
        private const val PREF_DB_PASSPHRASE = "aparok_db_passphrase_key"
        private const val PREF_FILE_KEY = "aparok_file_encryption_key"
        private const val AES_GCM_ALGORITHM = "AES/GCM/NoPadding"
        private const val KEY_SIZE_BYTES = 32 // 256 bits
        private const val GCM_IV_LENGTH_BYTES = 12 // 96 bits
        private const val GCM_TAG_LENGTH_BITS = 128
    }

    private val secureRandom = SecureRandom()

    private val encryptedPrefs: SharedPreferences by lazy {
        keystoreManager.initializeEncryptedPreferences()
    }

    /**
     * Retrieves or generates a 256-bit secure database passphrase for SQLCipher/SQLite.
     */
    fun getOrCreateDatabasePassphrase(): String {
        val existing = encryptedPrefs.getString(PREF_DB_PASSPHRASE, null)
        if (!existing.isNullOrEmpty()) {
            return existing
        }
        val keyBytes = ByteArray(KEY_SIZE_BYTES)
        secureRandom.nextBytes(keyBytes)
        val generatedPassphrase = keyBytes.joinToString("") { "%02x".format(it) }
        encryptedPrefs.edit().putString(PREF_DB_PASSPHRASE, generatedPassphrase).apply()
        return generatedPassphrase
    }

    /**
     * Retrieves or generates a 256-bit raw key for streaming file AEAD encryption.
     */
    fun getOrCreateFileEncryptionKey(): ByteArray {
        val existingHex = encryptedPrefs.getString(PREF_FILE_KEY, null)
        if (!existingHex.isNullOrEmpty()) {
            return hexToBytes(existingHex)
        }
        val keyBytes = ByteArray(KEY_SIZE_BYTES)
        secureRandom.nextBytes(keyBytes)
        val hex = bytesToHex(keyBytes)
        encryptedPrefs.edit().putString(PREF_FILE_KEY, hex).apply()
        return keyBytes
    }

    /**
     * Encrypts a source file to target file at rest using Streaming AES-256-GCM.
     * The 12-byte random IV is written as the prefix of the target file.
     */
    fun encryptFile(source: File, target: File, key: ByteArray = getOrCreateFileEncryptionKey()) {
        require(source.exists()) { "Source file does not exist: ${source.absolutePath}" }
        val iv = ByteArray(GCM_IV_LENGTH_BYTES).also { secureRandom.nextBytes(it) }

        val cipher = Cipher.getInstance(AES_GCM_ALGORITHM)
        val keySpec = SecretKeySpec(key, "AES")
        val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)

        target.parentFile?.mkdirs()
        FileOutputStream(target).use { fos ->
            fos.write(iv) // Write IV prefix
            CipherOutputStream(fos, cipher).use { cos ->
                FileInputStream(source).use { fis ->
                    fis.copyTo(cos)
                }
            }
        }
    }

    /**
     * Decrypts a target file encrypted with Streaming AES-256-GCM back to plain output.
     */
    fun decryptFile(source: File, target: File, key: ByteArray = getOrCreateFileEncryptionKey()) {
        require(source.exists()) { "Encrypted source file does not exist: ${source.absolutePath}" }
        FileInputStream(source).use { fis ->
            val iv = ByteArray(GCM_IV_LENGTH_BYTES)
            val bytesRead = fis.read(iv)
            require(bytesRead == GCM_IV_LENGTH_BYTES) { "Invalid encrypted file header in: ${source.absolutePath}" }

            val cipher = Cipher.getInstance(AES_GCM_ALGORITHM)
            val keySpec = SecretKeySpec(key, "AES")
            val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)

            target.parentFile?.mkdirs()
            FileOutputStream(target).use { fos ->
                CipherInputStream(fis, cipher).use { cis ->
                    cis.copyTo(fos)
                }
            }
        }
    }

    /**
     * Wraps an output stream with streaming AES-GCM encryption, writing the IV first.
     */
    fun wrapEncryptStream(baseOut: OutputStream, key: ByteArray = getOrCreateFileEncryptionKey()): OutputStream {
        val iv = ByteArray(GCM_IV_LENGTH_BYTES).also { secureRandom.nextBytes(it) }
        baseOut.write(iv)
        val cipher = Cipher.getInstance(AES_GCM_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv))
        return CipherOutputStream(baseOut, cipher)
    }

    /**
     * Wraps an input stream with streaming AES-GCM decryption, reading the 12-byte IV header first.
     */
    fun wrapDecryptStream(baseIn: InputStream, key: ByteArray = getOrCreateFileEncryptionKey()): InputStream {
        val iv = ByteArray(GCM_IV_LENGTH_BYTES)
        val read = baseIn.read(iv)
        require(read == GCM_IV_LENGTH_BYTES) { "Invalid or truncated encrypted stream header" }
        val cipher = Cipher.getInstance(AES_GCM_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(key, "AES"), GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv))
        return CipherInputStream(baseIn, cipher)
    }

    private fun bytesToHex(bytes: ByteArray): String = bytes.joinToString("") { "%02x".format(it) }

    private fun hexToBytes(hex: String): ByteArray {
        val len = hex.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(hex[i], 16) shl 4) + Character.digit(hex[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }
}
