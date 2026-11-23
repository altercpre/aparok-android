# Milestone R11: Unified Release Pipeline & Cryptographic Signing Audit

## Overview
Phase R11 enforces reproducible, auditable, and cryptographically pinned release artifacts. The release pipeline guarantees that every distributed APK is signed with a neutral certificate subject, modern signature schemes (v2 + v3), and verified against a pinned SHA-256 fingerprint.

## Verified Implementation Details

### 1. Neutral Certificate Subject & Security Hygiene
- Removed all legacy or invented certificate subjects (`L=Geneva, C=CH, O=Altercpre`).
- Enforced neutral subject strictly: `CN=Aparok`.
- Keystore files are permanently excluded from the repository via `.gitignore` and securely injected via GitHub Actions secret `RELEASE_KEYSTORE_BASE64`.

### 2. Hardened SHA-256 Fingerprint Pinning
- Pinned fingerprint stored in `release/CERT_SHA256`:
  ```
  A1:A1:B4:B0:AA:C2:A9:83:C9:FC:EE:10:CC:F0:07:91:DA:02:B7:B8:72:59:A1:BE:88:12:37:10:FD:10:CE:4F
  ```
- Before assembling or signing any artifact, `scripts/release/build-sign-verify-release.sh` executes:
  ```bash
  CURRENT_FP=$(keytool -list -v -keystore "${KEYSTORE_FILE}" -alias "${KEY_ALIAS}" -storepass "${STORE_PASS}" | grep "SHA256:" | head -n 1 | awk '{print $NF}')
  if [ "${CURRENT_FP}" != "${PINNED_FP}" ]; then
      echo "ERROR: Certificate fingerprint mismatch!"
      exit 1
  fi
  ```
  Any discrepancy immediately fails the build with exit code 1.

### 3. Modern Signature Schemes (v2 + v3)
- Configured in `app/build.gradle`:
  ```groovy
  signingConfigs {
      release {
          enableV1Signing = false
          enableV2Signing = true
          enableV3Signing = true
      }
  }
  ```
- Legacy v1 JAR signing is explicitly disabled, mitigating tampering vulnerabilities.
- Both APK Signature Scheme v2 and APK Signature Scheme v3 are enabled and active.

### 4. Verification with apksigner
- Post-build step verifies each generated APK using Android SDK `apksigner`:
  ```bash
  apksigner verify --verbose --print-certs <apk>
  ```
  Outputs:
  - `Verifies: true`
  - `Verified using v2 scheme (APK Signature Scheme v2): true`
  - `Verified using v3 scheme (APK Signature Scheme v3): true`
  - `Number of signers: 1`
  - `Signer #1 certificate DN: CN=Aparok`
  - `Signer #1 certificate SHA-256 digest: a1a1b4b0aac2a983c9fcee10ccf00791da02b7b87259a1be88123710fd10ce4f`
- Automated generation of `SHA256SUMS.txt` attached to every release.
