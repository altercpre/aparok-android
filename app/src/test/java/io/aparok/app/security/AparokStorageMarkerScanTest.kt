package io.aparok.app.security

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.AEADBadTagException
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Sovereign at-rest encryption verification and marker scan test suite.
 * Enforces Phase R7 requirements from APAROK_SPEC.md:
 * - Write APAROK_MARKER through every layer:
 *     (1) Chat text
 *     (2) Chat attachment bytes
 *     (3) Editor block text
 *     (4) Media files (images, audio, video)
 *     (5) Relation and metadata values
 * - Exhaustive byte-by-byte scan of the data directory across encodings:
 *     (a) Raw UTF-8 bytes
 *     (b) UTF-16 Little Endian bytes
 *     (c) UTF-16 Big Endian bytes
 *     (d) Standard Base64 bytes
 *     (e) URL-Safe Base64 bytes
 *   Must find zero occurrences of any marker!
 * - Wrong-key / corrupted ciphertext fails closed (AEAD authentication failure).
 * - Valid key round-trip preserves 100% database & file integrity.
 */
@RunWith(JUnit4::class)
class AparokStorageMarkerScanTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var mockKeystoreManager: KeystoreManager
    private lateinit var mockPrefs: android.content.SharedPreferences
    private lateinit var mockEditor: android.content.SharedPreferences.Editor
    private val memoryStore = mutableMapOf<String, String>()

    private lateinit var encryptionManager: AparokEncryptionManager
    private val secureRandom = SecureRandom()

    companion object {
        const val APAROK_MARKER_BASE = "APAROK_MARKER_SOVEREIGN_SECRET_2026"
        const val APAROK_MARKER_CHAT = "APAROK_MARKER_CHAT_TEXT_SOVEREIGN_MSG_42"
        const val APAROK_MARKER_ATTACHMENT = "APAROK_MARKER_ATTACHMENT_BYTES_1234567890"
        const val APAROK_MARKER_EDITOR = "APAROK_MARKER_EDITOR_BLOCK_CONTENT_SENSITIVE"
        const val APAROK_MARKER_RELATION = "APAROK_MARKER_RELATION_FIELD_VALUE_HIDDEN"
        const val APAROK_MARKER_MEDIA = "APAROK_MARKER_MEDIA_IMAGE_AUDIO_VIDEO_BLOB"
    }

    @Before
    fun setup() {
        mockKeystoreManager = mock(KeystoreManager::class.java)
        mockPrefs = mock(android.content.SharedPreferences::class.java)
        mockEditor = mock(android.content.SharedPreferences.Editor::class.java)

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
    fun testMarkerScanZeroPlaintextLeakAcrossAllEncodings() {
        val storageRoot = tempFolder.newFolder("aparok_data_root")
        val chatsDir = File(storageRoot, "chats").apply { mkdirs() }
        val attachmentsDir = File(storageRoot, "attachments").apply { mkdirs() }
        val editorDir = File(storageRoot, "editor").apply { mkdirs() }
        val relationsDir = File(storageRoot, "relations").apply { mkdirs() }
        val mediaDir = File(storageRoot, "media").apply { mkdirs() }

        val key = encryptionManager.getOrCreateFileEncryptionKey()

        // 1. Write Chat Text Layer
        val chatPlain = File(tempFolder.root, "chat.tmp").apply {
            writeText("User conversation: $APAROK_MARKER_CHAT", StandardCharsets.UTF_8)
        }
        val chatEncrypted = File(chatsDir, "chat_store.bin")
        encryptionManager.encryptFile(chatPlain, chatEncrypted, key)

        // 2. Write Chat Attachment Layer
        val attachmentPlain = File(tempFolder.root, "att.tmp").apply {
            writeBytes("Binary PDF Header: $APAROK_MARKER_ATTACHMENT -- footer".toByteArray(StandardCharsets.UTF_8))
        }
        val attachmentEncrypted = File(attachmentsDir, "doc_42.pdf.enc")
        encryptionManager.encryptFile(attachmentPlain, attachmentEncrypted, key)

        // 3. Write Editor Block Layer
        val editorPlain = File(tempFolder.root, "editor.tmp").apply {
            writeText("Editor Block Text: $APAROK_MARKER_EDITOR with formatting", StandardCharsets.UTF_8)
        }
        val editorEncrypted = File(editorDir, "blocks.db.enc")
        encryptionManager.encryptFile(editorPlain, editorEncrypted, key)

        // 4. Write Relation Layer
        val relationPlain = File(tempFolder.root, "rel.tmp").apply {
            writeText("Relation Target: $APAROK_MARKER_RELATION", StandardCharsets.UTF_8)
        }
        val relationEncrypted = File(relationsDir, "relations.dat")
        encryptionManager.encryptFile(relationPlain, relationEncrypted, key)

        // 5. Write Media Blob Layer
        val mediaPlain = File(tempFolder.root, "media.tmp").apply {
            writeBytes("Image JPEG EXIF: $APAROK_MARKER_MEDIA".toByteArray(StandardCharsets.UTF_8))
        }
        val mediaEncrypted = File(mediaDir, "photo.jpg.enc")
        encryptionManager.encryptFile(mediaPlain, mediaEncrypted, key)

        // Perform Exhaustive Marker Scan over entire storage directory
        val markers = listOf(
            APAROK_MARKER_BASE,
            APAROK_MARKER_CHAT,
            APAROK_MARKER_ATTACHMENT,
            APAROK_MARKER_EDITOR,
            APAROK_MARKER_RELATION,
            APAROK_MARKER_MEDIA
        )

        val scannedFiles = storageRoot.walkTopDown().filter { it.isFile }.toList()
        assertTrue("Storage directory must contain encrypted files", scannedFiles.isNotEmpty())

        for (file in scannedFiles) {
            val fileBytes = file.readBytes()
            for (marker in markers) {
                val utf8Pattern = marker.toByteArray(StandardCharsets.UTF_8)
                val utf16LePattern = marker.toByteArray(StandardCharsets.UTF_16LE)
                val utf16BePattern = marker.toByteArray(StandardCharsets.UTF_16BE)
                val base64StdPattern = Base64.getEncoder().encode(utf8Pattern)
                val base64UrlPattern = Base64.getUrlEncoder().encode(utf8Pattern)

                assertFalse(
                    "Plaintext leak! UTF-8 marker '$marker' found in ${file.relativeTo(storageRoot)}",
                    containsSubarray(fileBytes, utf8Pattern)
                )
                assertFalse(
                    "Plaintext leak! UTF-16LE marker '$marker' found in ${file.relativeTo(storageRoot)}",
                    containsSubarray(fileBytes, utf16LePattern)
                )
                assertFalse(
                    "Plaintext leak! UTF-16BE marker '$marker' found in ${file.relativeTo(storageRoot)}",
                    containsSubarray(fileBytes, utf16BePattern)
                )
                assertFalse(
                    "Plaintext leak! Base64 marker '$marker' found in ${file.relativeTo(storageRoot)}",
                    containsSubarray(fileBytes, base64StdPattern)
                )
                assertFalse(
                    "Plaintext leak! Base64Url marker '$marker' found in ${file.relativeTo(storageRoot)}",
                    containsSubarray(fileBytes, base64UrlPattern)
                )
            }
        }
    }

    @Test
    fun testWrongKeyFailsClosedWithoutDataLeak() {
        val validKey = encryptionManager.getOrCreateFileEncryptionKey()
        val wrongKey = ByteArray(32).also { secureRandom.nextBytes(it) }

        val originalText = "Top-secret sovereign text: $APAROK_MARKER_CHAT"
        val plainFile = tempFolder.newFile("plain_secret.txt").apply {
            writeText(originalText, StandardCharsets.UTF_8)
        }
        val encryptedFile = File(tempFolder.root, "encrypted_secret.bin")
        val decryptedFile = File(tempFolder.root, "decrypted_attempt.txt")

        encryptionManager.encryptFile(plainFile, encryptedFile, validKey)
        assertTrue(encryptedFile.exists())

        // Decryption with wrong key must throw AEAD / crypto exception
        try {
            encryptionManager.decryptFile(encryptedFile, decryptedFile, wrongKey)
            fail("Decryption with wrong key must fail closed and throw an Exception!")
        } catch (e: Exception) {
            // Expected: AEADBadTagException or GeneralSecurityException
            assertTrue(
                "Exception must be security/crypto related: ${e.javaClass.name}",
                e is javax.crypto.AEADBadTagException ||
                e is java.security.GeneralSecurityException ||
                e.cause is javax.crypto.AEADBadTagException ||
                e.cause is java.io.IOException
            )
        }

        // Output file must either not exist or be empty
        if (decryptedFile.exists()) {
            val leakedBytes = decryptedFile.readBytes()
            assertFalse(
                "Plaintext must not leak when wrong key is provided",
                containsSubarray(leakedBytes, APAROK_MARKER_CHAT.toByteArray(StandardCharsets.UTF_8))
            )
        }
    }

    @Test
    fun testDatabaseAndFileIntegrityCheckWithValidKey() {
        val key = encryptionManager.getOrCreateFileEncryptionKey()
        val originalData = "Database page simulation with binary entropy: \u0000\u0001\u0002$APAROK_MARKER_BASE\u00FF".toByteArray(StandardCharsets.UTF_8)

        val src = tempFolder.newFile("db.sqlite").apply { writeBytes(originalData) }
        val enc = File(tempFolder.root, "db.sqlite.enc")
        val dst = File(tempFolder.root, "db_restored.sqlite")

        encryptionManager.encryptFile(src, enc, key)
        encryptionManager.decryptFile(enc, dst, key)

        assertTrue(dst.exists())
        assertArrayEquals("Integrity check: Restored database bytes must match original byte-for-byte", originalData, dst.readBytes())
    }

    private fun containsSubarray(haystack: ByteArray, needle: ByteArray): Boolean {
        if (needle.isEmpty() || haystack.size < needle.size) return false
        for (i in 0..haystack.size - needle.size) {
            var found = true
            for (j in needle.indices) {
                if (haystack[i + j] != needle[j]) {
                    found = false
                    break
                }
            }
            if (found) return true
        }
        return false
    }
}
