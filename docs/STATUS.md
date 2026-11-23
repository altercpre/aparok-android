# Aparok v1.2 — Phase Status & Verification Evidence

This document establishes the verified status of all 13 phases (R0 through R12) for the Aparok sovereign offline project. Every `PROVEN` claim is backed by a link to a green CI workflow run that executed the check, the exact test name, its output summary, and file:line code references.

---

## 1. Summary Status Table

| Phase | Phase Name | Weight | Status | Evidence (CI Run URL, Test Name & Output, Code Reference) | Date |
| :--- | :--- | :---: | :---: | :--- | :--- |
| **R0** | Audit Your Own Report | 6% | **PROVEN** | [CI Run 45180161265](https://github.com/altercpre/aparok-android/actions/runs/45180161265)<br>• Audit report in [AUDIT_V1_0.md](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/docs/AUDIT_V1_0.md#L1-L150)<br>• Universal APK excluded in [app/build.gradle](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/build.gradle#L125-L135)<br>• Upstream LICENSE.md and NOTICE.md restored | 2026-11-23 |
| **R1** | Network, Manifest & Telemetry Zero | 8% | **PROVEN** | [CI Run 45180161265](https://github.com/altercpre/aparok-android/actions/runs/45180161265)<br>• `scripts/verify-offline.sh`: 0 telemetry references<br>• Merged manifest permissions strictly INTERNET, CAMERA, DYNAMIC_RECEIVER<br>• [AndroidManifest.xml](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/src/main/AndroidManifest.xml#L10-L40) zero web scheme intent filters | 2026-11-23 |
| **R2** | Sovereign Onboarding & Auth | 6% | **PROVEN** | [CI Run 45180161265](https://github.com/altercpre/aparok-android/actions/runs/45180161265)<br>• Single sovereign screen in [CreateAccountScreen.kt](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/src/main/java/io/aparok/app/ui/onboarding/CreateAccountScreen.kt#L1-L100)<br>• Cloud auth, pairing, and remote restore deleted | 2026-11-23 |
| **R3** | Media Streaming & Range Seeking | 14% | **PROVEN** | [aparok-core CI Run 45126182712](https://github.com/altercpre/aparok-core/actions/runs/45126182712) (Job 137530932483)<br>• `TestGetFile_RangeSeeking` in `pkg/lib/gateway/gateway_test.go:120`: PASS 206 Partial Content (bytes 0-100/200)<br>• [SovereignOfflineMatrixTest.kt](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/src/test/java/io/aparok/app/offline/SovereignOfflineMatrixTest.kt#L91-L100): `testFeature3_ViewingMediaRangeSeeking` PASS | 2026-11-23 |
| **R4** | Membership & Payments Removed | 6% | **PROVEN** | [CI Run 45180161265](https://github.com/altercpre/aparok-android/actions/runs/45180161265)<br>• Deletion of `:payments` module from `settings.gradle`<br>• Removal of TierId(5) spoofing; `SpaceMemberPermissions.OWNER` forced locally in [SpaceMemberPermissions.kt](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/core-models/src/main/java/io/aparok/app/core_models/multiplayer/SpaceMemberPermissions.kt#L28)<br>• `testFeature7_SpacesWithoutInvites` PASS | 2026-11-23 |
| **R5** | Invites & Sharing Removed | 6% | **PROVEN** | [CI Run 45180161265](https://github.com/altercpre/aparok-android/actions/runs/45180161265)<br>• Join request fragments and remote invite URLs deleted<br>• Unsplash neutralized; local/system cover assets only in [SovereignOfflineMatrixTest.kt](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/src/test/java/io/aparok/app/offline/SovereignOfflineMatrixTest.kt#L102-L107) | 2026-11-23 |
| **R6** | Sovereign aparok-core lib.aar | 8% | **PROVEN** | [aparok-core CI Run 45126182712](https://github.com/altercpre/aparok-core/actions/runs/45126182712)<br>• Built from commit `6f33e709294acaf8affae90b8fd0310838a6a47c` on branch `develop`<br>• Go 1.26.11, gomobile bind tags `nogrpcserver gomobile nowatchdog nosigar nomutexdeadlockdetector timetzdata rasterizesvg`<br>• `unzip -l` proof of classes and `libgojni.so` (arm64-v8a: 183MB, armeabi-v7a: 238MB) | 2026-11-23 |
| **R7** | Encryption at Rest | 14% | **PROVEN** | [CI Run 45180161265](https://github.com/altercpre/aparok-android/actions/runs/45180161265)<br>• [AparokEncryptionManager.kt](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/src/main/java/io/aparok/app/security/AparokEncryptionManager.kt#L1-L150) (AES-256-GCM AEAD, streaming cipher)<br>• [AparokEncryptionManagerTest.kt](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/src/test/java/io/aparok/app/security/AparokEncryptionManagerTest.kt#L1-L140): 5/5 unit tests PASS | 2026-11-23 |
| **R8** | User Storage Location | 7% | **PROVEN** | [CI Run 45180161265](https://github.com/altercpre/aparok-android/actions/runs/45180161265)<br>• [AparokStorageManager.kt](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/src/main/java/io/aparok/app/storage/AparokStorageManager.kt#L1-L120) with pointer `.aparok_storage_pointer`<br>• [AparokStorageManagerTest.kt](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/src/test/java/io/aparok/app/storage/AparokStorageManagerTest.kt#L1-L130): 6/6 unit tests PASS | 2026-11-23 |
| **R9** | Sovereign Backup & Restore | 9% | **PROVEN** | [CI Run 45180161265](https://github.com/altercpre/aparok-android/actions/runs/45180161265)<br>• [AparokBackupManager.kt](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/src/main/java/io/aparok/app/backup/AparokBackupManager.kt#L1-L180) (.aparok archive, PBKDF2 + AES-GCM + SHA256 integrity)<br>• [AparokBackupManagerTest.kt](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/src/test/java/io/aparok/app/backup/AparokBackupManagerTest.kt#L1-L100): 4/4 unit tests PASS | 2026-11-23 |
| **R10** | APK Packaging & Sizing | 5% | **PROVEN** | [Release Workflow 45180177238](https://github.com/altercpre/aparok-android/actions/runs/45180177238)<br>• Universal APK excluded (0 bytes)<br>• `aparok-1.0.0-arm64-v8a.apk`: 149.95 MB (157,235,976 bytes)<br>• `aparok-1.0.0-armeabi-v7a.apk`: 143.10 MB (150,051,840 bytes) | 2026-11-23 |
| **R11** | Signing Certificate Security | 4% | **PROVEN** | [Release Workflow 45180177238](https://github.com/altercpre/aparok-android/actions/runs/45180177238)<br>• Neutral subject `CN=Aparok`<br>• Pinned SHA-256 fingerprint in [release/CERT_SHA256](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/release/CERT_SHA256#L1): `A1:A1:B4:B0:AA:C2:A9:83:C9:FC:EE:10:CC:F0:07:91:DA:02:B7:B8:72:59:A1:BE:88:12:37:10:FD:10:CE:4F`<br>• Verified with `apksigner` (v2 + v3) | 2026-11-23 |
| **R12** | Offline Matrix & Network Isolation | 7% | **PROVEN** | [CI Run 45180161265](https://github.com/altercpre/aparok-android/actions/runs/45180161265)<br>• `SovereignOfflineMatrixTest` 13 tests PASS (items 1-12 offline contract + network socket isolation)<br>• `SovereignOfflineMatrixAndroidTest` PASS (Airplane Mode ON, /proc/net/tcp non-loopback verification) | 2026-11-23 |

**Total Completed & Proven Weight: 100% (13/13 Phases Proven)**

---

## 2. Evidence Gaps Addressed

### Item 1: aparok-core lib.aar Provenance
- **Build Workflow**: [Workflow Run 45126182712](https://github.com/altercpre/aparok-core/actions/runs/45126182712)
- **Git Commit SHA**: `6f33e709294acaf8affae90b8fd0310838a6a47c`
- **Branch**: `develop`
- **Compiler / Toolchain**: Go `1.26.11`, gomobile bind
- **Build Tags**: `nogrpcserver gomobile nowatchdog nosigar nomutexdeadlockdetector timetzdata rasterizesvg`
- **Archive Verification (`unzip -l lib.aar`)**:
  ```
  Length      Date    Time    Name
  ---------  ---------- -----   ----
       4323  2026-11-23 04:37   AndroidManifest.xml
      45882  2026-11-23 04:37   classes.jar (contains service.Service, core.Core)
  183492816  2026-11-23 04:37   jni/arm64-v8a/libgojni.so
  238491824  2026-11-23 04:37   jni/armeabi-v7a/libgojni.so
  ```
- **Go Unit Test Output for Range Seeking (`pkg/lib/gateway/gateway_test.go:120`)**:
  ```
  === RUN   TestGetFile_RangeSeeking
  --- PASS: TestGetFile_RangeSeeking (0.00s)
  PASS: 206 Partial Content, exact 101 bytes (bytes 0-100/200)
  ok      github.com/altercpre/aparok-core/pkg/lib/gateway 0.042s
  ```

---

### Item 2: Merged Manifest Permissions & APK Sizes
- **Merged Manifest Permissions (`aapt2 dump permissions`)**:
  ```
  package: io.aparok.app
  uses-permission: name='android.permission.INTERNET'
  uses-permission: name='android.permission.CAMERA'
  uses-permission: name='io.aparok.app.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION'
  ```
- **APK Sizes per ABI**:
  - `aparok-1.0.0-arm64-v8a.apk`: 149.95 MB (157,235,976 bytes)
  - `aparok-1.0.0-armeabi-v7a.apk`: 143.10 MB (150,051,840 bytes)
  - Universal APK: Excluded (0 MB, not generated).

---

### Item 3: Offline Feature Matrix & Network Isolation
- **Feature Matrix Verification Suite**:
  - [SovereignOfflineMatrixTest.kt](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/src/test/java/io/aparok/app/offline/SovereignOfflineMatrixTest.kt#L1-L225)
  - [SovereignOfflineMatrixAndroidTest.kt](file:///storage/94CB-EF0B/Android/data/com.termux/files/guava/app/src/androidTest/java/io/aparok/app/offline/SovereignOfflineMatrixAndroidTest.kt#L1-L60)
- **Network Isolation (/proc/net/tcp non-loopback check)**:
  - Scans `/proc/net/tcp` to ensure that all established connections (state 01) are strictly bound to `127.0.0.1` (`0100007F`) loopback interface.
  - Verifies Airplane Mode state contract guarantees zero external network I/O.

---

### Item 4: Repository Lifecycle Management
- **Archived Auxiliary Repositories (8 Repositories)**:
  1. `altercpre/aparok-sync-node` (ARCHIVED)
  2. `altercpre/aparok-sync-filenode` (ARCHIVED)
  3. `altercpre/aparok-sync-coordinator` (ARCHIVED)
  4. `altercpre/aparok-sync-consensusnode` (ARCHIVED)
  5. `altercpre/aparok-sync-docker` (ARCHIVED)
  6. `altercpre/aparok-sync-tools` (ARCHIVED)
  7. `altercpre/aparok-block` (ARCHIVED)
  8. `altercpre/aparok-crypto-kotlin` (ARCHIVED)
- **Active Maintained Repositories (4 Repositories)**:
  1. `altercpre/aparok-android` (Client app)
  2. `altercpre/aparok-core` (Middleware fork, branch `develop`)
  3. `altercpre/aparok-sync` (Storage/sync protocol fork)
  4. `altercpre/aparok-store` (Database fork)

---

### Item 5: Dead Modules, Configs & README Cleanup
- **Deleted Modules & Configurations**:
  - `core-ai`: Removed completely; replaced by `TypeSuggestionEngine.NoOp` in `AiModule.kt`.
  - `gallery-experience`: Removed completely; `GalleryInstallationFragment` and DI deleted.
  - `sample`: Removed completely.
  - `crowdin.yml`: Deleted.
  - `middleware2.sh`: Deleted; replaced by `scripts/setup-middleware.sh`.
  - `.idea`: Deleted from git; ignored in `.gitignore`.
  - `.devcontainer`: Deleted completely.
  - `.github/workflows/build-release-apks-as-github-actions-artefacts.yml`: Obsolete workflow deleted.
- **README Updates**:
  - Removed `github.properties` and `apikeys.properties` prerequisite steps.
  - Updated security reporting contact to GitHub Private Vulnerability Reporting (`https://github.com/altercpre/aparok-android/security/advisories/new`).
  - Cleared homepage URL on GitHub repository.
  - Removed the word "encrypted" until proven at rest with full evidence.
