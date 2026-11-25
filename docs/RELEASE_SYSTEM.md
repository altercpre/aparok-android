# Aparok Release & Signing System (Phase R11)

## Overview
Aparok's release system enforces strict cryptographic authenticity, verifiable builds, zero secrets in source code, and SHA-256 certificate pinning.

---

## 1. Release Security Architecture

- **Keystore Parameters**: RSA 4096-bit key, 10,000 days validity, subject `CN=Aparok`.
- **Passphrase Generation**: Keystore and key passwords generated via cryptographically secure randomness (`openssl rand -base64 32`). Passwords are never committed to version control.
- **Certificate Pinning**: The SHA-256 certificate fingerprint is committed in `release/CERT_SHA256`:
  ```
  A1:A1:B4:B0:AA:C2:A9:83:C9:FC:EE:10:CC:F0:07:91:DA:02:B7:B8:72:59:A1:BE:88:12:37:10:FD:10:CE:4F
  ```
- **Signing Verification**: Release builds are verified via `apksigner verify --verbose --print-certs` for both Android APK Signature Scheme v2 and v3.
- **Zero-Secret Guarantee**: No plaintext signing secrets exist in git history or configuration files.

---

## 2. Release Gates Prior to Publishing

1. **Merged-Manifest Audit**: Strict gate requiring only allowed permissions (`INTERNET` for localhost gateway and `CAMERA`).
2. **Zero-Telemetry Gate**: Grep check across dependencies and code ensuring zero tracking SDKs (Amplitude, Sentry, Firebase).
3. **Zipalign & Signature Check**: Apksigner must verify signature validity and fingerprint matching `release/CERT_SHA256`.
4. **ABI Integrity**: Shipped packages strictly contain per-ABI APKs (`arm64-v8a` and `armeabi-v7a`), with universal APK generation disabled.
5. **Checksum Generation**: Automated `sha256sum` generation committed in `SHA256SUMS.txt`.
