# Aparok Master Sovereign Offline Audit & Verification Report (v1.4)

**Date:** 2026-11-26  
**Target Organization:** `altercpre`  
**Application Repository:** `altercpre/aparok-android` (branch: `main`)  
**Core Middleware Repository:** `altercpre/aparok-core` (branch: `develop`)  
**Middleware Release Tag:** `v0.51.6-aparok` (`lib.aar` SHA-256: `99f40c64d2430309299e7f27afff51368f730d68e073247fcd8db996394c4e1c`)  
**Certificate Fingerprint (SHA-256):** `A1:A1:B4:B0:AA:C2:A9:83:C9:FC:EE:10:CC:F0:07:91:DA:02:B7:B8:72:59:A1:BE:88:12:37:10:FD:10:CE:4F`

---

## 1. Executive Summary & Verification Matrix (22% Proven — Aligned with `docs/STATUS.md`)

> See `docs/STATUS.md` for canonical phase status and R3 evidence rules. Only phases backed by real `src/main` or Go production tests, green CI runs, failing mutation CI runs, revert green CI runs, and verified screen-reachable `file:line` call sites count as `PROVEN`.

| Phase | Description | Weight | Status | Proof / Evidence & Remaining Gaps |
| :--- | :--- | :---: | :---: | :--- |
| **R0** | Verified State + Cleanup | 6% | **PARTIAL** | Android stale docs (`AUDIT.md`, `SIZE.md`) reconciled; `aparok-core` still retains 6 active upstream workflows. |
| **R1** | Strict Offline Manifest & Telemetry Gate | 8% | **PARTIAL** | App manifest has `INTERNET`+`CAMERA` and merged manifest + component gate (`scripts/verify-offline.sh`); pending CI mutation proof & `STATUS.md` update. |
| **R2** | Local-First Onboarding & Profile Creation | 6% | **PARTIAL** | `OnboardingProfileScreen` + real unit tests (`OnboardingProfileTest.kt`) and Splash routing exist; instrumented cold-start test & mutation proof pending. |
| **R3** | Offline Chat, Media Streaming & HTTP Range | 14% | **PROVEN** | Green/Revert CI [45644804329](https://github.com/altercpre/aparok-android/actions/runs/45644804329), Mutation Fail CI [45642003462](https://github.com/altercpre/aparok-android/actions/runs/45642003462); `AparokSovereignProductionOfflineTest.kt`, `gateway_test.go`. |
| **R4** | Complete Membership & Paywall Removal | 6% | **PARTIAL** | `MembershipProvider` no-op & payments strings purged; remaining `membership` references & `FileLimits`/`NodeUsageInfo` purge + mutation proof pending. |
| **R5** | Purge Invites, Remote Sharing, Update Checks & Rebrand | 6% | **PARTIAL** | `AboutAppScreen.kt` rebranded; remaining `invite`/`ShareSpace`/`publish`/`unsplash`/`Anytype` literals, dead `graph.xml` destinations, N8/N10 & mutation proof pending. |
| **R6** | Sovereign Core Middleware Compilation | 8% | **PROVEN** | Core Green/Revert CI [45647877925](https://github.com/altercpre/aparok-core/actions/runs/45647877925), Mutation Fail CI [45646581061](https://github.com/altercpre/aparok-core/actions/runs/45646581061); `lib.aar` SHA-256 `99f40c64d2430309299e7f27afff51368f730d68e073247fcd8db996394c4e1c`. |
| **R7** | At-Rest Encryption (`GetStorageKey` / Tink / SQLCipher) | 14% | **PARTIAL** | Core `SetStorageKey`/`Lock()` implemented in `sovereign.go`; full `any-store`/Tantivy wiring, Tink+StrongBox/SQLCipher, and real core directory `APAROK_MARKER` scan pending. |
| **R8** | Custom Storage Location & Probe Architecture | 7% | **PARTIAL** | Core `ProbeStorage` and `SovereignScreens.kt` stubs exist; full `mmap` probe, move verification, and screen wiring pending. |
| **R9** | Encrypted `.aparok` Backup & Restore | 9% | **PARTIAL** | Temporary Kotlin `AparokBackupManager` still present; full Go core R9 quiesce/Argon2id/atomic swap + UI wiring pending. |
| **R10** | Binary Size Optimization (< 80 MB arm64, < 75 MB arm7) | 5% | **UNVERIFIED** | `v1.4.0` release APKs measured **156.25 MB** (`arm64-v8a`) and **149.11 MB** (`armeabi-v7a`) vs `< 80 MB` / `< 75 MB` target (`FAILING`). |
| **R11** | Idempotent Signing & Zero-Secret Keystore Pipeline | 4% | **PARTIAL** | Hardcoded `'aparok123456'` fallback & `CN=Aparok-DEV` removed, `scripts/distribution/` deleted; full release CI signing verification pending. |
| **R12** | Offline E2E Validation & Final Sovereignty Audit | 7% | **PARTIAL** | `.github/workflows/emulator-test.yml` defined; clean CI emulator E2E + `/proc/net/tcp` & `/proc/net/tcp6` UID audit pending. |
| **TOTAL** | **Sovereign Offline Overhaul** | **22%** | **IN PROGRESS** | **R3 (14%) + R6 (8%) PROVEN; R0–R2, R4–R5, R7–R12 pending** |

---

## 2. Cryptographic Security & Marker Scan Status (Phase R7 & R9 — PARTIAL)

- **Current State**: `AparokStorageMarkerScanTest.kt` runs under standard JUnit4 against the temporary Kotlin `AparokEncryptionManager` helper only; it does not yet scan a real Go-core-created data directory.
- **Backup Archive Status**: `AparokBackupManagerTest.kt` currently exercises the temporary Kotlin `AparokBackupManager`, which is scheduled for deletion once R9 Go core backup/restore (`Backup`, `VerifyBackup`, `Restore`) is wired to `CreateBackupActivity` and `RestoreBackupActivity`.

---

## 3. Network & Permission Isolation Audit (Phase R1)

- **Manifest Permissions Allowed**:
  - `android.permission.INTERNET` (Loopback only for localhost HTTP file gateway `127.0.0.1`)
  - `android.permission.CAMERA` (Runtime-only for local profile and attachment photo capture)
- **Manifest Permissions Removed**:
  - `ACCESS_NETWORK_STATE`, `ACCESS_WIFI_STATE`, `CHANGE_WIFI_MULTICAST_STATE`, `WAKE_LOCK`, `RECEIVE_BOOT_COMPLETED`, `FOREGROUND_SERVICE`, `C2DM_PUSH`, `AD_ID` stripped via `tools:node="remove"` in `app/src/main/AndroidManifest.xml`.
- **Telemetry SDK Audit**:
  - `scripts/verify-offline.sh` checks zero `firebase`, `sentry`, `amplitude`, `playBilling`, `billingclient`, `google-services`, and non-removed `c2dm` references across sources, and audits merged-manifest `<uses-permission>`, `<service>`, `<receiver>`, and `<provider>` components.
  - Note: `mlkit-barcode-scanning` (`app/build.gradle:277`) and `app/proguard-rules.pro` firebase comments remain scheduled for removal in R5/R10.

---

## 4. Packaging & Size Metrics (Phase R10 & R11 — UNVERIFIED / PARTIAL)

- **arm64-v8a Release APK (v1.4.0 baseline)**: **156.25 MB** (Target: `< 80 MB` — **FAILING**)
- **armeabi-v7a Release APK (v1.4.0 baseline)**: **149.11 MB** (Target: `< 75 MB` — **FAILING**)
- **Universal APK**: **DISABLED** (`universalApk false` in `app/build.gradle:183`)
- **Pending Size Work**: Remove `-dontobfuscate` (`app/proguard-rules.pro:22`), remove `mlkit-barcode-scanning` (`app/build.gradle:277`), and complete R5/R10 asset/resource cleanup (see `docs/SIZE.md`).
- **Signing Status**: `scripts/release/ensure-signing.sh` and `scripts/release/build-sign-verify-release.sh` enforce `CN=Aparok` only and fail closed without hardcoded password fallbacks; `release/CERT_SHA256` pins the release fingerprint.

---

## 5. Conclusion
Aparok v1.4 is actively in progress at **22% PROVEN** per `docs/STATUS.md` (`R3` and `R6` proven). Remaining phases (`R0`–`R2`, `R4`–`R5`, `R7`–`R12`) require implementation, CI verification, mutation proofs, and APK size reduction before release completion.
