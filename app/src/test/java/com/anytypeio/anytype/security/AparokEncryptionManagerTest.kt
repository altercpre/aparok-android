package com.anytypeio.anytype.security

import android.content.SharedPreferences
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class AparokEncryptionManagerTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var mockKeystoreManager: KeystoreManager
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private val memoryStore = mutableMapOf<String, String>()

    private lateinit var encryptionManager: AparokEncryptionManager

    @Before
    fun setup() {
        mockKeystoreManager = mock(KeystoreManager::class.java)
        mockPrefs = mock(SharedPreferences::class.java)
        mockEditor = mock(SharedPreferences.Editor::class.java)

        `when`(mockKeystoreManager.initializeEncryptedPreferences()).thenReturn(mockPrefs)

        `when`(mockPrefs.getString(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.nullable(String::class.java)))
            .thenAnswer { invocation ->
                val key = invocation.getArgument<String>(0)
                val default = invocation.getArgument<String?>(1)
                memoryStore[key] ?: default
            }

        `when`(mockPrefs.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putString(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString()))
            .thenAnswer { invocation ->
                val key = invocation.getArgument<String>(0)
                val value = invocation.getArgument<String>(1)
                memoryStore[key] = value
                mockEditor
            }

        encryptionManager = AparokEncryptionManager(mockKeystoreManager)
    }

    @Test
    fun `test database passphrase generation and persistence`() {
        val passphrase1 = encryptionManager.getOrCreateDatabasePassphrase()
        assertTrue("Passphrase should not be empty", passphrase1.isNotEmpty())
        assertEquals("Passphrase should be 64 hex chars (256 bits)", 64, passphrase1.length)

        val passphrase2 = encryptionManager.getOrCreateDatabasePassphrase()
        assertEquals("Passphrase should be stable and persisted", passphrase1, passphrase2)
    }

    @Test
    fun `test file encryption key generation and persistence`() {
        val key1 = encryptionManager.getOrCreateFileEncryptionKey()
        assertEquals("File key must be 32 bytes (256 bits)", 32, key1.size)

        val key2 = encryptionManager.getOrCreateFileEncryptionKey()
        assertArrayEquals("File key should be stable and persisted", key1, key2)
    }

    @Test
    fun `test streaming file encryption roundtrip with zero plaintext leak`() {
        val originalText = "Aparok sovereign offline data at rest - sensitive user note payload 12345"
        val originalBytes = originalText.toByteArray(StandardCharsets.UTF_8)

        val sourceFile = tempFolder.newFile("plain.txt").apply {
            writeBytes(originalBytes)
        }
        val encryptedFile = File(tempFolder.root, "encrypted.bin")
        val decryptedFile = File(tempFolder.root, "decrypted.txt")

        encryptionManager.encryptFile(sourceFile, encryptedFile)

        assertTrue(encryptedFile.exists())
        val cipherBytes = encryptedFile.readBytes()

        // Verify zero plaintext leaks
        assertFalse(
            "Ciphertext must not contain plaintext string",
            String(cipherBytes, StandardCharsets.ISO_8859_1).contains("Aparok sovereign")
        )

        // Decrypt
        encryptionManager.decryptFile(encryptedFile, decryptedFile)

        assertTrue(decryptedFile.exists())
        val decryptedBytes = decryptedFile.readBytes()
        assertArrayEquals("Decrypted bytes must match original exactly", originalBytes, decryptedBytes)
        assertEquals(originalText, String(decryptedBytes, StandardCharsets.UTF_8))
    }

    @Test
    fun `test stream encryption roundtrip`() {
        val sampleData = "Streaming media test: video/audio/document payload".toByteArray(StandardCharsets.UTF_8)
        val key = ByteArray(32).also { SecureRandom().nextBytes(it) }

        val bout = ByteArrayOutputStream()
        val encOut = encryptionManager.wrapEncryptStream(bout, key)
        encOut.write(sampleData)
        encOut.close()

        val cipherBytes = bout.toByteArray()
        assertTrue("Ciphertext should be larger than plaintext due to IV and GCM tag", cipherBytes.size > sampleData.size)

        val bin = ByteArrayInputStream(cipherBytes)
        val decIn = encryptionManager.wrapDecryptStream(bin, key)
        val decOut = ByteArrayOutputStream()
        decIn.copyTo(decOut)
        decIn.close()

        assertArrayEquals("Stream decrypted data must match original", sampleData, decOut.toByteArray())
    }

    @Test(expected = Exception::class)
    fun `test tamper proofing - modified ciphertext throws on decryption`() {
        val originalText = "Tamper proof test message"
        val sourceFile = tempFolder.newFile("tamper_plain.txt").apply {
            writeText(originalText)
        }
        val encryptedFile = File(tempFolder.root, "tamper_enc.bin")
        val decryptedFile = File(tempFolder.root, "tamper_dec.txt")

        encryptionManager.encryptFile(sourceFile, encryptedFile)

        // Tamper with one byte in the ciphertext body
        val cipherBytes = encryptedFile.readBytes()
        cipherBytes[cipherBytes.size - 5] = (cipherBytes[cipherBytes.size - 5].toInt() xor 0xFF).toByte()
        encryptedFile.writeBytes(cipherBytes)

        // Decryption MUST fail authentication
        encryptionManager.decryptFile(encryptedFile, decryptedFile)
    }
}
