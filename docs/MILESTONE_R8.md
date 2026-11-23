# Milestone R8: Custom Storage Location & Pointer File Architecture

## Overview
Phase R8 implements the sovereign custom storage location requirement. Users can direct all databases, space files, and media caches to a custom directory (e.g., SD card or external SAF path), decoupled from Android's default sandboxed directory.

## Verified Implementation Details

### 1. Persistent Pointer File Mechanism
- Default storage directory (`context.filesDir`) stores a lightweight pointer file: `.aparok_storage_pointer`.
- If the pointer file exists and contains a valid, readable, and writable directory path, all app and middleware storage points to that directory.
- If the custom directory is unavailable, unmounted, or corrupted, the system falls back safely to internal `context.filesDir` without crashes.

### 2. Core Middleware & App Path Wiring
- **Middleware Root**: `DefaultInitialParamsProvider.workDir` dynamically resolves the active storage directory via `AparokStorageManager.resolveStorageDir(context)`. The Go middleware uses this as its root working directory for all database files (`account.db`, space stores, Badger/SQLite).
- **App Storage**: `DefaultPathProvider.providePath()` dynamically routes local file and cache storage to the active directory.

### 3. Migration & Reset Operations
- `AparokStorageManager`:
  - `setCustomStorageLocation(newDir, migrateFiles = true)`: Validates directory permissions, recursively migrates existing files, and updates the pointer file.
  - `resetToInternalStorage()`: Deletes the pointer file and safely reverts the root path to internal filesDir.
  - `isUsingCustomStorage()`: Provides real-time detection of whether internal or external storage is active.

### 4. Unit Test Verification
- Implemented in `AparokStorageManagerTest.kt`:
  - `test default storage directory falls back to internal filesDir`: Confirms default resolution with no pointer file.
  - `test custom storage pointer resolution`: Confirms custom path resolution when pointer file is present.
  - `test invalid path in pointer file falls back safely to default`: Confirms zero crashes on unreadable or invalid custom paths.
  - `test setCustomStorageLocation writes pointer and copies files`: Confirms pointer persistence and file data migration.
  - `test resetToInternalStorage removes pointer file`: Confirms clean reversal back to internal storage.
