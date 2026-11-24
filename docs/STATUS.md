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
| R7 | Encrypt Everything | 14% | NOT STARTED | — | — |
| R8 | Storage Location | 7% | NOT STARTED | — | — |
| R9 | Backup + Restore .aparok | 9% | NOT STARTED | — | — |
| R10 | Size | 5% | NOT STARTED | — | — |
| R11 | Signing + Release | 4% | NOT STARTED | — | — |
| R12 | Offline E2E + Final Audit | 7% | NOT STARTED | — | — |

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
