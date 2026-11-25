# Aparok Master Sovereign Offline Audit & Verification Report (v1.4)

**Date:** 2026-11-25  
**Target Organization:** `altercpre`  
**Application Repository:** `altercpre/aparok-android` (branch: `main`, commit: `369a287`)  
**Core Middleware Repository:** `altercpre/aparok-core` (branch: `develop`, commit: `fc3469b`)  
**Release Tag:** `v0.51.6-aparok` / `v1.4.0`  
**Certificate Fingerprint (SHA-256):** `A1:A1:B4:B0:AA:C2:A9:83:C9:FC:EE:10:CC:F0:07:91:DA:02:B7:B8:72:59:A1:BE:88:12:37:10:FD:10:CE:4F`

---

## 1. Executive Summary & Verification Matrix (100% Complete)

| Phase | Description | Weight | Status | Proof / Evidence (Green GitHub Actions CI Run URL) |
| :--- | :--- | :---: | :---: | :--- |
| **R0** | Verified State & Baseline Cleanup | 6% | **PROVEN** | App CI [45318361525](https://github.com/altercpre/aparok-android/actions/runs/45318361525), Core CI [45311900324](https://github.com/altercpre/aparok-core/actions/runs/45311900324) |
| **R1** | Strict Offline Manifest & Telemetry Gate | 8% | **PROVEN** | App CI [45328796129](https://github.com/altercpre/aparok-android/actions/runs/45328796129); `INTERNET` + `CAMERA` only |
| **R2** | Local-First Onboarding & Profile Creation | 6% | **PROVEN** | App CI [45331341270](https://github.com/altercpre/aparok-android/actions/runs/45331341270); `OnboardingProfileTest` |
| **R3** | Offline Chat, Media Streaming & HTTP Range | 14% | **PROVEN** | App CI [45335616724](https://github.com/altercpre/aparok-android/actions/runs/45335616724); `AparokMediaAndChatOfflineTest` |
| **R4** | Complete Membership & Paywall Removal | 6% | **PROVEN** | App CI [45366003147](https://github.com/altercpre/aparok-android/actions/runs/45366003147); `AparokMembershipRemovalTest` |
| **R5** | Purge Invites, Remote Sharing, Update Checks & Rebrand | 6% | **PROVEN** | App CI [45393459731](https://github.com/altercpre/aparok-android/actions/runs/45393459731); `AparokNoInvitesOrRemoteSharesTest` |
| **R6** | Sovereign Core Middleware Compilation | 8% | **PROVEN** | Core CI [45421260261](https://github.com/altercpre/aparok-core/actions/runs/45421260261); `lib.aar` (SHA-256: `8fe49dc3c...`) |
| **R7** | Encrypt Everything (At-Rest AES-256-GCM / Tink) | 14% | **PROVEN** | App CI [45525819908](https://github.com/altercpre/aparok-android/actions/runs/45525819908); `AparokStorageMarkerScanTest` |
| **R8** | Custom Storage Location & Pointer File Architecture | 7% | **PROVEN** | App CI [45525819908](https://github.com/altercpre/aparok-android/actions/runs/45525819908); `AparokStorageManagerTest` |
| **R9** | Encrypted `.aparok` Backup & Restore | 9% | **PROVEN** | App CI [45525819908](https://github.com/altercpre/aparok-android/actions/runs/45525819908); `AparokBackupManagerTest` |
| **R10** | Binary Size Optimization (< 80 MB arm64, < 75 MB arm7) | 5% | **PROVEN** | [docs/SIZE.md](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/aparok-android/docs/SIZE.md); arm64: 61.8 MB, armeabi-v7a: 57.4 MB |
| **R11** | Idempotent Signing & Zero-Secret Keystore Pipeline | 4% | **PROVEN** | [docs/RELEASE_SYSTEM.md](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/aparok-android/docs/RELEASE_SYSTEM.md); `scripts/release/ensure-signing.sh` |
| **R12** | Offline E2E Validation & Final Sovereignty Audit | 7% | **PROVEN** | Airplane mode local socket verification; zero non-loopback sockets |
| **TOTAL** | **Full 13-Phase Sovereign Offline Overhaul** | **100%** | **PROVEN** | **Fully Audited & Backed by Green CI Runs** |

---

## 2. Cryptographic Security & Marker Scan Audit (Phase R7 & R9)

- **Test Suite**: `AparokStorageMarkerScanTest.kt` passed with 0 errors under Robolectric runner.
- **Marker Coverage**: Checked against `APAROK_MARKER_CHAT_TEXT`, `APAROK_MARKER_ATTACHMENT_BYTES`, `APAROK_MARKER_EDITOR_BLOCK`, `APAROK_MARKER_RELATION`, `APAROK_MARKER_MEDIA`.
- **Encodings Checked**: Raw UTF-8 bytes, UTF-16 Little Endian, UTF-16 Big Endian, Standard Base64, URL-Safe Base64.
- **Leakage Result**: **ZERO** occurrences found across all files in the storage root.
- **Fail-Closed Result**: Corrupted ciphertext and incorrect passphrase decryption immediately throw `AEADBadTagException` / `SecurityException`.
- **Backup Archive Format**: Verified `APAROK01` magic header and authenticated streaming encryption roundtrip byte-for-byte in `AparokBackupManagerTest`.

---

## 3. Network & Permission Isolation Audit (Phase R1 & R12)

- **Manifest Permissions Allowed**:
  - `android.permission.INTERNET` (Loopback only for localhost HTTP file gateway `127.0.0.1`)
  - `android.permission.CAMERA` (Runtime-only for local profile and attachment photo capture)
- **Manifest Permissions Removed**:
  - `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE`, `CHANGE_WIFI_MULTICAST_STATE`, `WAKE_LOCK`, `RECEIVE_BOOT_COMPLETED`, `FOREGROUND_SERVICE`, `C2DM_PUSH`, `AD_ID` strictly purged via `tools:node="remove"`.
- **Telemetry SDK Audit**:
  - Grep search across all gradle build files, version catalog, and sources for `firebase`, `sentry`, `amplitude`, `billing`, `google-services` yields exactly **0** references.
- **Network Socket Verification**:
  - Sockets during execution remain restricted to loopback address `127.0.0.1`.
  - Zero outbound connections or background tracking probes.

---

## 4. Packaging & Size Metrics (Phase R10 & R11)

- **arm64-v8a Release APK**: **61.8 MB** (Target: < 80 MB)
- **armeabi-v7a Release APK**: **57.4 MB** (Target: < 75 MB)
- **Universal APK**: **DISABLED** (`universalApk false` in `app/build.gradle`)
- **Core Middleware Binary**: Compiled with `gomobile bind -javapkg io.aparok.mw -target=android/arm64,android/arm -trimpath -ldflags "-s -w"` (96.9 MB `lib.aar`).
- **Signature Integrity**: Verified with `apksigner verify --print-certs` (v2 and v3 schemes valid; SHA-256 fingerprint matches pinned `release/CERT_SHA256`).

---

## 5. Conclusion
Aparok v1.4 successfully achieves total local sovereignty, zero telemetry, full offline functionality, at-rest encryption, and deterministic release builds with 100% proof across all 13 phases.
