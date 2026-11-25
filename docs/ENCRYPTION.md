# Aparok Sovereign Encryption Architecture (Phase R7)

## Overview
Aparok implements comprehensive at-rest encryption across all storage tiers, strictly ensuring zero plaintext data leakage, fail-closed access control, and complete data integrity without external server dependencies.

---

## 1. Cryptographic Specifications

| Component | Primitive / Algorithm | Key Derivation / Storage | Purpose |
| :--- | :--- | :--- | :--- |
| **Master Storage Key** | AES-256-GCM (256-bit) | Android Keystore (`KeyStore.getInstance("AndroidKeyStore")`) with StrongBox backed hardware security when available | Envelopes all application subkeys and master credentials |
| **Local Databases** | SQLCipher / SQLite v2 Codec | 256-bit high-entropy passphrase generated via `SecureRandom` and sealed in hardware Keystore | Encrypts Room, Badger, and any-store account/space databases |
| **File & Media Storage** | Streaming AES-256-GCM / XChaCha20-Poly1305 | HKDF-SHA256 derived subkeys from master storage key | Encrypts chat attachments, picked photos, videos, audio, and documents |
| **Ephemeral Files & Cache** | Streaming AEAD | Dynamic session key in `noBackupFilesDir` | Encrypts camera temp, sharing temp, thumbnails, and logs |
| **Settings & Flags** | EncryptedSharedPreferences / Tink | AES-256-GCM master keyset | Encrypts local user configuration, PIN hash, and storage paths |

---

## 2. Protection Matrix

| Data Classification | Storage Layer | At-Rest State | Verification Mechanism |
| :--- | :--- | :--- | :--- |
| **Chat Messages** | Chat repository / database | Ciphertext (AES-256-GCM) | `AparokStorageMarkerScanTest` (UTF-8, UTF-16, Base64 scan) |
| **Media Attachments** | Filesystem storage | Ciphertext (AEAD with 96-bit IV) | Multi-encoding binary scan (`doc_42.pdf.enc`, `photo.jpg.enc`) |
| **Editor Content** | Block tree database | Ciphertext (SQLCipher) | Full directory hex inspection with zero marker hits |
| **Relations & Metadata** | Key-value store | Ciphertext (XChaCha20) | Marker scan across indices and relations |
| **Database Encryption** | App & Core DBs | Ciphertext pages | Database page entropy and wrong-key failure tests |

---

## 3. Threat Model & Residual Risks

While Aparok achieves sovereign at-rest confidentiality and integrity on disk, the following residual risks are honestly documented per specification:
1. **Volatile RAM**: Active messages and decrypted keys reside in process RAM during runtime. They are wiped upon `Lock()`, but physical memory imaging while the device is unlocked and running remains an inherent hardware-level boundary.
2. **Garbage Collection (GC) Latency**: In the JVM/Android runtime, immutable String objects cannot be explicitly zeroized prior to garbage collection. Sensitive cryptographic keys are therefore handled as `ByteArray` and cleared immediately after use.
3. **Filesystem Metadata**: File sizes, modification timestamps, and directory structures remain visible to lower-level OS inspection, although file names and payloads are encrypted.
4. **Rooted Devices**: An adversary with active root privileges or runtime ptrace capabilities on the device could potentially inspect live process memory while the app is actively unlocked.
