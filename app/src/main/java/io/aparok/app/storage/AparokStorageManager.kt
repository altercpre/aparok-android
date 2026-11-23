package io.aparok.app.storage

import android.content.Context
import java.io.File
import timber.log.Timber

/**
 * Sovereign custom storage manager for Aparok (Phase R8).
 * Manages user-chosen storage directories (e.g. external SD, SAF mount)
 * via a persistent pointer file: `.aparok_storage_pointer`.
 */
class AparokStorageManager(private val context: Context) {

    companion object {
        const val POINTER_FILE_NAME = ".aparok_storage_pointer"

        /**
         * Resolves the active storage directory given a context:
         * 1. Reads pointer file in internal filesDir.
         * 2. Validates that the pointed directory exists or can be created and is writable.
         * 3. Falls back safely to internal filesDir if invalid or unreadable.
         */
        @JvmStatic
        fun resolveStorageDir(context: Context): File {
            val defaultDir = context.filesDir
            return try {
                val pointerFile = File(defaultDir, POINTER_FILE_NAME)
                if (pointerFile.exists() && pointerFile.isFile) {
                    val customPath = pointerFile.readText().trim()
                    if (customPath.isNotEmpty()) {
                        val customDir = File(customPath)
                        if (customDir.exists() || customDir.mkdirs()) {
                            if (customDir.canRead() && customDir.canWrite()) {
                                return customDir
                            }
                        }
                    }
                }
                defaultDir
            } catch (e: Exception) {
                Timber.e(e, "Failed to resolve custom storage pointer. Falling back to internal filesDir.")
                defaultDir
            }
        }
    }

    /**
     * Returns true if a custom storage directory is currently active.
     */
    fun isUsingCustomStorage(): Boolean {
        val current = resolveStorageDir(context)
        return current.absolutePath != context.filesDir.absolutePath
    }

    /**
     * Gets the currently active storage directory.
     */
    fun getActiveStorageDir(): File = resolveStorageDir(context)

    /**
     * Sets a custom storage location and writes the pointer file.
     * Optionally copies existing files from old to new location.
     */
    fun setCustomStorageLocation(newDir: File, migrateFiles: Boolean = true): Boolean {
        return try {
            if (!newDir.exists() && !newDir.mkdirs()) {
                Timber.e("Could not create custom storage directory: ${newDir.absolutePath}")
                return false
            }
            if (!newDir.canWrite()) {
                Timber.e("Custom storage directory is not writable: ${newDir.absolutePath}")
                return false
            }

            val currentDir = getActiveStorageDir()
            if (migrateFiles && currentDir.absolutePath != newDir.absolutePath) {
                currentDir.listFiles()?.forEach { file ->
                    if (file.name != POINTER_FILE_NAME) {
                        file.copyRecursively(File(newDir, file.name), overwrite = true)
                    }
                }
            }

            val pointerFile = File(context.filesDir, POINTER_FILE_NAME)
            pointerFile.writeText(newDir.absolutePath)
            Timber.i("Updated Aparok storage pointer to: ${newDir.absolutePath}")
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to set custom storage location")
            false
        }
    }

    /**
     * Resets storage back to internal filesDir by deleting the pointer file.
     */
    fun resetToInternalStorage(): Boolean {
        return try {
            val pointerFile = File(context.filesDir, POINTER_FILE_NAME)
            if (pointerFile.exists()) {
                pointerFile.delete()
            }
            Timber.i("Reset storage to internal filesDir")
            true
        } catch (e: Exception) {
            Timber.e(e, "Failed to reset storage to internal")
            false
        }
    }
}
