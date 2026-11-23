# Milestone R7: Encrypt at Rest (SQLCipher + Streaming AEAD + Android Keystore)

## Overview
Phase R7 fulfills the sovereign data encryption at rest requirement. All databases, media objects, file attachments, and preferences are protected by hardware-backed Android KeyStore keys, SQLCipher / SQLite passphrase management, and Streaming AEAD (AES-256-GCM).

## Verified Implementation Details

### 1. Hardware-Backed Android Keystore
- Master keys generated using `AndroidKeyStore` provider with `MasterKeys.AES256_GCM_SPEC`.
- Implemented in `KeystoreManager.kt` with automatic detection and recovery for keystore corruption errors (e.g. `InvalidKeyBlob`).
- Sensitive preferences and credentials secured in `EncryptedSharedPreferences` with `AES256_SIV` key encryption and `AES256_GCM` value encryption.

### 2. SQLCipher / SQLite Database Passphrase Management
- Added `getOrCreateDatabasePassphrase()` in `AparokEncryptionManager.kt`:
  - Generates 256 bits (32 bytes, 64 hex characters) of cryptographically secure random entropy via `SecureRandom`.
  - Persisted securely inside `EncryptedSharedPreferences` (backed by Android Keystore).
  - Supplies the encryption key for database operations.

### 3. Streaming AEAD File & Media Encryption
- Implemented `AparokEncryptionManager.kt`:
  - Uses `AES/GCM/NoPadding` with 256-bit key size, 96-bit (12-byte) random IV per file/stream, and 128-bit authentication tag.
  - Streaming file encryption (`encryptFile`) and decryption (`decryptFile`) stream directly to disk without loading entire media files into RAM.
  - Stream wrappers (`wrapEncryptStream` and `wrapDecryptStream`) allow transparent encryption of chat attachments, exported archives, and media caches.

### 4. Rigorous Security Verification Tests
- Test suite implemented in `AparokEncryptionManagerTest.kt`:
  - `test database passphrase generation and persistence`: Verified 256-bit hex generation and persistence.
  - `test file encryption key generation and persistence`: Verified 32-byte key generation and stability.
  - `test streaming file encryption roundtrip with zero plaintext leak`: Verified byte-for-byte fidelity and confirmed zero plaintext sequences appear in ciphertext.
  - `test stream encryption roundtrip`: Verified streaming cipher wrappers.
  - `test tamper proofing - modified ciphertext throws on decryption`: Verified that modifying a single bit in the ciphertext triggers AEAD authentication failure (`AEADBadTagException` / `IOException`).
