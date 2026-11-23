package io.aparok.app.backup

import java.io.File
import java.nio.charset.StandardCharsets
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class AparokBackupManagerTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var backupManager: AparokBackupManager
    private lateinit var sourceDir: File
    private lateinit var restoreDir: File
    private lateinit var backupFile: File

    private val testPassphrase = "SuperSecureSovereignPassword123!".toCharArray()

    @Before
    fun setup() {
        backupManager = AparokBackupManager()
        sourceDir = tempFolder.newFolder("source_data")
        restoreDir = tempFolder.newFolder("restored_data")
        backupFile = File(tempFolder.root, "sovereign_backup.aparok")

        // Populate sample data
        File(sourceDir, "account.db").writeText("SQLITE3 account database header and metadata")
        val spaceDir = File(sourceDir, "spaces/space_main").apply { mkdirs() }
        File(spaceDir, "tree.db").writeText("Tree storage database payload")
        val filesDir = File(sourceDir, "files").apply { mkdirs() }
        File(filesDir, "avatar.png").writeBytes(byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00, 0x01, 0x02))
    }

    @Test
    fun `test backup and restore roundtrip byte-for-byte fidelity`() {
        val backupSuccess = backupManager.createBackup(sourceDir, backupFile, testPassphrase)
        assertTrue("Backup creation must succeed", backupSuccess)
        assertTrue("Backup file must exist", backupFile.exists())
        assertTrue("Backup file size must be non-zero", backupFile.length() > 36) // Header is 36 bytes

        val backupBytes = backupFile.readBytes()
        val magic = String(backupBytes.sliceArray(0..7), StandardCharsets.US_ASCII)
        assertEquals("APAROK01", magic)

        // Verify zero plaintext leaks
        val backupText = String(backupBytes, StandardCharsets.ISO_8859_1)
        assertFalse("Ciphertext must not leak database text", backupText.contains("account database header"))
        assertFalse("Ciphertext must not leak space text", backupText.contains("Tree storage database"))

        // Restore into clean directory
        val restoreSuccess = backupManager.restoreBackup(backupFile, restoreDir, testPassphrase)
        assertTrue("Restore must succeed with valid passphrase", restoreSuccess)

        // Assert all files restored correctly
        val restoredAccountDb = File(restoreDir, "account.db")
        assertTrue(restoredAccountDb.exists())
        assertEquals("SQLITE3 account database header and metadata", restoredAccountDb.readText())

        val restoredTreeDb = File(restoreDir, "spaces/space_main/tree.db")
        assertTrue(restoredTreeDb.exists())
        assertEquals("Tree storage database payload", restoredTreeDb.readText())

        val restoredAvatar = File(restoreDir, "files/avatar.png")
        assertTrue(restoredAvatar.exists())
        assertArrayEquals(
            byteArrayOf(0x89.toByte(), 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, 0x00, 0x01, 0x02),
            restoredAvatar.readBytes()
        )
    }

    @Test(expected = SecurityException::class)
    fun `test restore with wrong passphrase throws SecurityException`() {
        backupManager.createBackup(sourceDir, backupFile, testPassphrase)
        val wrongPassphrase = "WrongPassword999!".toCharArray()

        backupManager.restoreBackup(backupFile, restoreDir, wrongPassphrase)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `test restore invalid non-aparok file throws IllegalArgumentException`() {
        val nonAparokFile = File(tempFolder.root, "invalid.aparok").apply {
            writeText("Random non-aparok text data that lacks the APAROK01 magic header")
        }
        backupManager.restoreBackup(nonAparokFile, restoreDir, testPassphrase)
    }
}
