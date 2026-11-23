# Milestone R9: Encrypted .aparok Backup & Restore Architecture

## Overview
Phase R9 implements the sovereign backup and restore system for Aparok. Users can create a single encrypted archive file (`.aparok`) containing their entire identity, spaces, databases, documents, and media attachments, protected by a passphrase of their choice with PBKDF2 and AES-256-GCM streaming encryption.

## Verified Implementation Details

### 1. Cryptographic Format Specification
- **Magic Header (8 bytes)**: `APAROK01` (ASCII) ensures archive type recognition and versioning.
- **Salt (16 bytes)**: Cryptographically secure random salt generated per backup.
- **Nonce/IV (12 bytes)**: AES-GCM 96-bit initialization vector.
- **Key Derivation**: `PBKDF2WithHmacSHA256` with 100,000 iterations deriving a 256-bit AES master key.
- **Authenticated Encryption**: `AES/GCM/NoPadding` with 128-bit authentication tag protecting the entire ZIP stream from unauthorized tampering.

### 2. Complete Sovereign Storage Coverage
- Recursively bundles all assets in the active storage root:
  - Account and identity database (`account.db`)
  - Space metadata and tree storage (`spaces/*/tree.db`)
  - Attached media, files, and image caches (`files/*`)
- Streamed in chunks (8 KB buffer) through `ZipOutputStream` into `CipherOutputStream`, allowing multi-gigabyte backups without out-of-memory errors.

### 3. Secure Restore & Verification
- `AparokBackupManager.restoreBackup`:
  - Validates `APAROK01` magic header and extracts salt/IV.
  - Derives decryption key and streams through `CipherInputStream` into `ZipInputStream`.
  - Enforces canonical path checks on every entry to prevent zip slip directory traversal vulnerabilities.
  - If the passphrase is incorrect or any part of the ciphertext is tampered with, AES-GCM tag validation fails and immediately throws a `SecurityException`.

### 4. Unit Test Verification
- Implemented in `AparokBackupManagerTest.kt`:
  - `test backup and restore roundtrip byte-for-byte fidelity`: Verifies exact restoration of database and binary assets.
  - `Zero plaintext leak test`: Verifies ciphertext bytes contain no recognizable strings from the source files.
  - `test restore with wrong passphrase throws SecurityException`: Verifies that unauthorized restore attempts are rejected.
  - `test restore invalid non-aparok file throws IllegalArgumentException`: Verifies magic header enforcement.
