package com.anytypeio.anytype.storage

import android.content.Context
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class AparokStorageManagerTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var mockContext: Context
    private lateinit var internalFilesDir: File
    private lateinit var storageManager: AparokStorageManager

    @Before
    fun setup() {
        mockContext = mock(Context::class.java)
        internalFilesDir = tempFolder.newFolder("internal_files")
        `when`(mockContext.filesDir).thenReturn(internalFilesDir)
        storageManager = AparokStorageManager(mockContext)
    }

    @Test
    fun `test default storage directory falls back to internal filesDir`() {
        val resolved = AparokStorageManager.resolveStorageDir(mockContext)
        assertEquals(internalFilesDir.absolutePath, resolved.absolutePath)
        assertFalse(storageManager.isUsingCustomStorage())
    }

    @Test
    fun `test custom storage pointer resolution`() {
        val customDir = tempFolder.newFolder("custom_sdcard_storage")
        val pointerFile = File(internalFilesDir, AparokStorageManager.POINTER_FILE_NAME)
        pointerFile.writeText(customDir.absolutePath)

        val resolved = AparokStorageManager.resolveStorageDir(mockContext)
        assertEquals(customDir.absolutePath, resolved.absolutePath)
        assertTrue(storageManager.isUsingCustomStorage())
    }

    @Test
    fun `test invalid path in pointer file falls back safely to default`() {
        val pointerFile = File(internalFilesDir, AparokStorageManager.POINTER_FILE_NAME)
        // Non-existent path that cannot be created
        pointerFile.writeText("/sys/forbidden/directory/path/that/does/not/exist")

        val resolved = AparokStorageManager.resolveStorageDir(mockContext)
        assertEquals(internalFilesDir.absolutePath, resolved.absolutePath)
    }

    @Test
    fun `test setCustomStorageLocation writes pointer and copies files`() {
        // Create dummy file in internal filesDir
        val sampleDb = File(internalFilesDir, "account.db").apply {
            writeText("dummy sqlite database header")
        }

        val customDir = tempFolder.newFolder("custom_target_dir")
        val success = storageManager.setCustomStorageLocation(customDir, migrateFiles = true)
        assertTrue(success)

        val pointerFile = File(internalFilesDir, AparokStorageManager.POINTER_FILE_NAME)
        assertTrue(pointerFile.exists())
        assertEquals(customDir.absolutePath, pointerFile.readText().trim())

        val migratedDb = File(customDir, "account.db")
        assertTrue(migratedDb.exists())
        assertEquals("dummy sqlite database header", migratedDb.readText())
    }

    @Test
    fun `test resetToInternalStorage removes pointer file`() {
        val customDir = tempFolder.newFolder("custom_dir_to_reset")
        storageManager.setCustomStorageLocation(customDir, migrateFiles = false)
        assertTrue(storageManager.isUsingCustomStorage())

        val resetSuccess = storageManager.resetToInternalStorage()
        assertTrue(resetSuccess)
        assertFalse(storageManager.isUsingCustomStorage())
        assertEquals(internalFilesDir.absolutePath, storageManager.getActiveStorageDir().absolutePath)
    }
}
