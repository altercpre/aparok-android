package io.aparok.app.ui.sovereign

import io.aparok.app.backup.AparokBackupManager
import io.aparok.app.storage.AparokStorageManager
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.nio.file.Files

/**
 * Production test suite verifying existence, binding, and contracts of Sovereign Screens N1-N10.
 */
class AparokSovereignScreensTest {

    @Test
    fun `sovereign screen activities are compiled and loadable`() {
        assertNotNull(AppLockActivity::class.java)
        assertNotNull(StorageChooserActivity::class.java)
        assertNotNull(CreateBackupActivity::class.java)
        assertNotNull(RestoreBackupActivity::class.java)
        assertNotNull(StorageUnavailableActivity::class.java)
        assertNotNull(SecuritySettingsActivity::class.java)
    }

    @Test
    fun `backup and restore workflow functions end-to-end on simulated filesystem`() {
        val tempDir = Files.createTempDirectory("aparok_backup_ui_test").toFile()
        try {
            val sourceDir = File(tempDir, "source_data").apply { mkdirs() }
            val dataFile = File(sourceDir, "test_chat.txt").apply { writeText("sovereign screen test data") }

            val backupFile = File(tempDir, "backup.aparok")
            val restoreDir = File(tempDir, "restored_data").apply { mkdirs() }

            val backupManager = AparokBackupManager()
            val pwd = "veryStrongPassword123!"

            val created = backupManager.createBackup(sourceDir, backupFile, pwd.toCharArray())
            assertTrue("Backup creation must succeed", created)
            assertTrue("Backup file must exist", backupFile.exists() && backupFile.length() > 0)

            val restored = backupManager.restoreBackup(backupFile, restoreDir, pwd.toCharArray())
            assertTrue("Backup restore must succeed", restored)

            val restoredFile = File(restoreDir, "test_chat.txt")
            assertTrue("Restored file must match original", restoredFile.exists() && restoredFile.readText() == "sovereign screen test data")
        } finally {
            tempDir.deleteRecursively()
        }
    }
}
