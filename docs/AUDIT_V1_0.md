# Aparok v1.0 Independent Code Audit

**Date:** 2026-11-24
**App commit:** a2d44a0 (aparok-android, branch main)
**Core commit:** 6f33e70 (aparok-core, branch develop)

## APP AUDIT

### Genuinely Done (Verified)

| Claim | True | Evidence |
|-------|------|----------|
| Package is io.aparok.app | ✅ | app/src/main/AndroidManifest.xml:L38 `android:name="io.aparok.app.app.AndroidApplication"` |
| Permissions INTERNET + CAMERA only | ✅ | AndroidManifest.xml:L5-L6; all others have `tools:node="remove"` L8-L22 |
| Firebase/Sentry/Amplitude removed | ✅ | `grep -rn firebase\|sentry\|amplitude` returns 0 matches in source/gradle |
| MDNSResolver no-op | ✅ | Implementation returns empty/NOT_CONNECTED |
| NetworkMode forced LOCAL | ✅ | NetworkConnectionStatus reports NOT_CONNECTED |
| One OnboardingProfileScreen | ✅ | Single onboarding flow |
| MembershipProvider no-op emptyFlow | ✅ | No spoofed tier |
| Modules removed: payments, analytics, crash-reporting, core-ai, gallery-experience, sample | ✅ | Not in settings.gradle |
| Release script CN=Aparok, v2+v3 | ✅ | scripts/release/ |

### Still Wrong

| Issue | Claim | True | Evidence | Fix |
|-------|-------|------|----------|-----|
| (a) Membership refs | 68 files | ❌ 94 files | `grep -rln membership\|Membership\|...` = 94 files; playBilling in libs.versions.toml:163 | R4: delete all |
| (b) FileLimits | Present | ✅ 32 files | `grep -rln FileLimits\|NodeUsageInfo\|...` = 32 files | R0/R4: delete |
| (c) Invite/Share/Unsplash/Gallery/Publish | Present | ✅ | invite=121, unsplash=53, gallery=122, publish=70 files | R5: delete |
| (d) Anytype refs | 79 files | ✅ | `grep -rln Anytype --include=*.kt` = 79 | R5: rebrand per case |
| (e) DefaultUserPermissionProvider | Falls back OWNER | ✅ PARTIAL | get()/observe() fall back to OWNER when member missing; all() uses `?: OWNER`. But no unit tests exist. | R3: add unit tests |
| (f) Signing passwords | Hardcoded aparok123456 | ✅ | scripts/release/; app-release.gpg exists; CERT_SHA256 exists | R0: move to secrets, fail without keystore |
| (g) verify-offline.sh | Weak | ✅ | Passes with warning on missing manifest; ignores "billing" in telemetry grep | R0: make strict |
| (h) Middleware coords | io.altercpre:anytype-heart-android | ✅ | libs.versions.toml:72 | R6: change to io.aparok:middleware |
| (i) releases/latest links | 29 files | ✅ | Blind replace put releases/latest URLs where web links were | R5: remove features, not redirect |

## CORE AUDIT

| Claim | True | Evidence |
|-------|------|----------|
| go.mod is anyproto/anytype-heart | ✅ NOT DONE | go.mod:1 `module github.com/anyproto/anytype-heart` |
| 0 altercpre references in go.mod | ✅ | `grep altercpre go.mod` = 0 |
| go.mod requires upstream any-sync v0.13.8, any-store v1.0.3, any-store/v2 v2.5.0 | ✅ | go.mod |
| bootstrap.go registers ALL network components | ✅ | bootstrap.go:L1-50 imports nodeconf, yamux, quic, pool, etc. |
| ci-compile-android-lib.mk ends with `\|\| true` | ✅ | makefiles/ci-compile-android-lib.mk:L4 |
| Upstream workflows remain | ✅ | cla.yml, mirror-any-block.yml, perftests.yml, reusable_wait_for_self_hosted_mac_mini.yml |
| README says "encrypted" | ✅ | README.md:2 "the encrypted, local-first engine" — no encryption code exists |
| IsLocalOnlyMode used in 7 files | ✅ | config.go, upload.go, delete.go, service.go (filesync), fileobject/service.go, reindex.go, pushnotification/service.go, workspace.go |
| R6-R9 NOT STARTED | ✅ | No encryption, no backup, no storage location code |

## SIDE REPOS

| Repo | Status | Action |
|------|--------|--------|
| aparok-sync | Renamed module path, unused by core | Archive unless R7 needs patch |
| aparok-store | Renamed module path, unused by core | Archive unless R7 needs patch |
| aparok-block | Renamed, archived | Archive |
| aparok-sync-node | Renamed, archived | Archive |
| aparok-sync-filenode | Renamed, archived | Archive |
| aparok-sync-coordinator | Renamed, archived | Archive |
| aparok-sync-consensusnode | Renamed, archived | Archive |
| aparok-sync-docker | Renamed, archived | Archive |
| aparok-sync-tools | Renamed, archived | Archive |
| aparok-crypto-kotlin | Renamed, archived | Archive |
