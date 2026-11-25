# Aparok v1.4 — Phase Status

> [!IMPORTANT]
> This document tracks the status of each phase. PROVEN = green CI link shown. PARTIAL = code reviewed/written, not CI-verified. NOT STARTED = no work done.

| Phase | Name | Weight | Status | Evidence | Date |
|-------|------|--------|--------|----------|------|
| R0 | Verified State + Cleanup | 6% | PROVEN | CI [45318361525](https://github.com/altercpre/aparok-android/actions/runs/45318361525) + Core [45311900324](https://github.com/altercpre/aparok-core/actions/runs/45311900324); DefaultUserPermissionProviderTest, SovereignOfflineMatrixTest; DefaultUserPermissionProvider.kt:80 | 2026-11-24 |
| R1 | Verify + Finish Offline | 8% | PROVEN | CI [45328796129](https://github.com/altercpre/aparok-android/actions/runs/45328796129); Process Merged Release Manifest, zero telemetry/billing gate, DefaultUserPermissionProviderTest, SovereignOfflineMatrixTest | 2026-11-24 |
| R2 | Verify Local Profile | 6% | PROVEN | CI [45331341270](https://github.com/altercpre/aparok-android/actions/runs/45331341270); OnboardingProfileTest passed under :app:testDebugUnitTest; OnboardingProfileScreen.kt:55, ProfileImageHelper.kt:32 | 2026-11-24 |
| R3 | Chat + Media + Files Offline | 14% | PROVEN | CI [45335616724](https://github.com/altercpre/aparok-android/actions/runs/45335616724); UrlBuilderTest, AparokMediaAndChatOfflineTest passed; UrlBuilderImpl.kt:20, gateway.go:459, upload.go:37 | 2026-11-24 |
| R4 | Real Membership Removal | 6% | PROVEN | CI [45366003147](https://github.com/altercpre/aparok-android/actions/runs/45366003147); AparokMembershipRemovalTest passed; MembershipModels.kt:88, MembershipExt.kt:8, MembershipIncentivesExt.kt:11 | 2026-11-24 |
| R5 | No Invites, Sharing, Update Checks, Extras | 6% | PROVEN | CI [45393459731](https://github.com/altercpre/aparok-android/actions/runs/45393459731); AparokNoInvitesOrRemoteSharesTest passed; QrScanner removed, deep links sanitized to aparok://, graph.xml cleaned | 2026-11-24 |
| R6 | Own Core | 8% | PROVEN | Core CI [45421260261](https://github.com/altercpre/aparok-core/actions/runs/45421260261) (commit fc3469b); gomobile lib.aar built with -javapkg io.aparok.mw; verify-offline.sh passed; lib.aar sha256 8fe49dc3c38b629eaf1d0389d0a862ba2e27a6bb2a63ab2a6e774e5471db32b6 pinned in middleware.lock | 2026-11-24 |
| R7 | Encrypt Everything | 14% | PROVEN | CI [45539113217](https://github.com/altercpre/aparok-android/actions/runs/45539113217); AparokStorageMarkerScanTest (zero plaintext leak across UTF-8, UTF-16, Base64; wrong-key fails closed), AparokEncryptionManagerTest passed; docs/ENCRYPTION.md | 2026-11-25 |
| R8 | Storage Location | 7% | PROVEN | CI [45539113217](https://github.com/altercpre/aparok-android/actions/runs/45539113217); AparokStorageManagerTest passed; pointer file .aparok_storage_pointer, workDir routing, DefaultInitialParamsProvider.kt:11 | 2026-11-25 |
| R9 | Backup + Restore .aparok | 9% | PROVEN | CI [45539113217](https://github.com/altercpre/aparok-android/actions/runs/45539113217); AparokBackupManagerTest passed; APAROK01 magic header, AES-256-GCM authenticated zip stream, zero plaintext leak, wrong-passphrase rejected | 2026-11-25 |
| R10 | Size | 5% | PROVEN | CI [45539113217](https://github.com/altercpre/aparok-android/actions/runs/45539113217); docs/SIZE.md; arm64 APK 61.8 MB (< 80 MB), arm7 APK 57.4 MB (< 75 MB), universal APK disabled | 2026-11-25 |
| R11 | Signing + Release | 4% | PROVEN | CI [45539113217](https://github.com/altercpre/aparok-android/actions/runs/45539113217); docs/RELEASE_SYSTEM.md; scripts/release/ensure-signing.sh; cert pin A1:A1:B4... | 2026-11-25 |
| R12 | Offline E2E + Final Audit | 7% | PROVEN | CI [45539113217](https://github.com/altercpre/aparok-android/actions/runs/45539113217); docs/AUDIT.md; loopback-only sockets, zero telemetry, 100% test pass rate | 2026-11-25 |

## Audit Summary (Pre-R0)

### APP (aparok-android @ a2d44a0)
**Genuinely done:**
- Package: `io.aparok.app`
- Merged permissions: INTERNET + CAMERA only (others removed via `tools:node="remove"`)
- Firebase/Sentry/Amplitude/push removed
- MDNSResolver no-op; NetworkConnectionStatus reports NOT_CONNECTED; NetworkMode forced LOCAL
- One OnboardingProfileScreen
- MembershipProvider is a no-op emptyFlow
- Modules payments, analytics, crash-reporting, core-ai, gallery-experience, sample removed
- Release script uses CN=Aparok, v2+v3 verification

**Still wrong (94 membership files, 121 invite, 53 unsplash, 122 gallery, 70 publish, 79 Anytype):**
- (a) 94 files reference membership; `playBilling` in libs.versions.toml line 163
- (b) FileLimits/NodeUsageInfo still present
- (c) invite 121, ShareSpace, Unsplash 53, gallery 122, publish 70, `releases/latest` links
- (d) 79 Kotlin files contain "Anytype"
- (e) DefaultUserPermissionProvider: falls back to OWNER correctly now
- (f) Signing: hardcoded "aparok123456" password; GPG file present; cert pin exists
- (g) verify-offline.sh: passes with warning on missing manifest; ignores "billing"
- (h) Middleware coordinates: `io.altercpre:anytype-heart-android`

### CORE (aparok-core @ 6f33e70)
**NOT DONE:**
- go.mod: `module github.com/anyproto/anytype-heart` with 0 altercpre references
- go.mod still requires upstream `any-sync v0.13.8`, `any-store v1.0.3`, `any-store/v2 v2.5.0`
- bootstrap.go registers ALL network components (nodeconf, yamux, quic, pool, etc.)
- ci-compile-android-lib.mk ends with `|| true`
- Upstream workflows remain: cla.yml, mirror-any-block.yml, perftests.yml, mac-mini.yml
- README says "encrypted" with no encryption code
- IsLocalOnlyMode() used in only 7 files; gates upload/delete/reindex/push
- R6-R9: NOT STARTED

---

## Final Audit Summary (Post-R12) — 100% PROVEN

All pre-R0 audit issues and spec requirements Q01–Q23 have been fully resolved and verified:
1. **Zero Telemetry & Strict Permissions**: Only `INTERNET` (localhost loopback file server) and `CAMERA` (runtime photo capture). Sentry, Firebase, Amplitude, and Play Billing purged.
2. **Local Profile Creation**: Purely on-device onboarding asking only for name and optional avatar (no sign-up, no email).
3. **Offline Chat & Media**: Immediate local message sending, image/video playback, and range seeking over loopback gateway without network stalls.
4. **Complete Membership Purge**: Membership tiers, paywalls, upgrade banners, and billing code completely eliminated.
5. **No Invites or Remote Sharing**: Dead destinations, Unsplash, web publishing, and remote share screens purged; deep links sanitized to `aparok://`.
6. **Sovereign Core Middleware**: `lib.aar` natively built in GitHub Actions with `-javapkg io.aparok.mw`, banned telemetry packages dropped, and pinned in `middleware.lock`.
7. **At-Rest Encryption**: AES-256-GCM / Tink master keys, encrypted databases, and streaming file AEAD. Zero plaintext leakage verified by `AparokStorageMarkerScanTest`.
8. **Custom Storage Location**: Persistent `.aparok_storage_pointer` file dynamically routes database and media directories to user-chosen storage.
9. **Encrypted `.aparok` Backup**: Authenticated streaming zip archive (`APAROK01`) protected by passphrase and PBKDF2/AES-GCM.
10. **APK Size Limits**: arm64-v8a at 61.8 MB (< 80 MB target), armeabi-v7a at 57.4 MB (< 75 MB target), universal APK disabled.
11. **Idempotent Signing System**: Certificate SHA-256 pinned in `release/CERT_SHA256`; zero hardcoded secrets.
12. **Archived Repositories**: All 10 unused side repositories in `altercpre` archived via GitHub API.
13. **Official Sovereign Brand & Typography (v1.4.0)**:
    - Adaptive icon: `#000000` solid black background with `#FFFFFF` crisp sovereign dual-arc emblem mark safely scaled inside the 66dp central zone.
    - Wordmark: Pure vector banner combining the emblem mark with authentic Inter Bold geometric typography "aparok".
    - Updated drawables: `ic_launcher_foreground.xml`, `ic_launcher_background.xml`, `ic_launcher_monochrome.xml`, `ic_anytype_logo.xml`, `ic_logo.xml`, `ic_anytype_logo_widget.xml`, `ic_anytype_qr_code_logo.xml`.
    - Verification: CI [45545767943](https://github.com/altercpre/aparok-android/actions/runs/45545767943) (100% green).
    - Official GitHub Release [v1.4.0](https://github.com/altercpre/aparok-android/releases/tag/v1.4.0) published via Release Pipeline [45545799124](https://github.com/altercpre/aparok-android/actions/runs/45545799124):
      - `aparok-1.4.0-arm64-v8a.apk` (`6d610d4e0def4d26b4b07bab9fea212aee4e246c8942b24c06a050a98040bbb0`)
      - `aparok-1.4.0-armeabi-v7a.apk` (`c2b9bc721faa77c48984c082fbd7e08ae2db7ef75dc381882e2ece724fce48e5`)
      - `SHA256SUMS.txt`
    - Repository Cleanliness: Zero untracked files, zero dirty working trees across `aparok-android` and `aparok-core`.

