# Aparok v1.4 — Phase Status

`[00%] Phase 0/13 | Reset Baseline | step 1/1 - doing: reset docs/STATUS.md to honest baseline per R1`

> [!IMPORTANT]
> **Evidence Rules (R3):** `NN%` counts ONLY phases whose rows are genuinely `PROVEN` (`00%` at reset baseline; phase weights sum to `100%`).
> - A test counts ONLY if it **(a)** imports and calls production classes from `src/main` or Go production packages, **(b)** fails when the feature is removed or broken, and **(c)** runs in a named CI job.
> - Every `PROVEN` row MUST cite: **(1)** a green named CI job run URL after the test-adding commit, **(2)** a failing mutation CI run URL (breaking the feature in a throwaway commit) plus the revert green CI run URL, and **(3)** a screen-reachable production call site (`file:line`; any helper class with no caller must be wired or deleted).
> - Never mark a row `PROVEN` in the same commit that adds its test; a subsequent CI run must link the test and the production code.
> - Statuses: `PROVEN` (all evidence rules met), `PARTIAL` (partial code/docs/CI exists; missing items listed), `NOT STARTED` (core feature not implemented or not wired), `UNVERIFIED` (existing claim contradicted or not verified in CI).

| Phase | Name | Weight | Status | Missing / Why Not PROVEN (Evidence & Gaps) | Date |
|-------|------|--------|--------|--------------------------------------------|------|
| R0 | Verified State + Cleanup | 6% | PARTIAL | 10 side repos archived & `docs/AUDIT_V1_0.md` exists, but `aparok-core` still retains 6 active upstream workflows (`anyblock-proto-check.yml`, `build.yml`, `golangci-lint.yml`, `nightly.yml`, `smoke-tests.yml`, `test.yml`); READMEs/docs contain unverified claims; no mutation/CI proof. | 2026-11-25 |
| R1 | Offline App, Verify & Finish | 8% | PARTIAL | App manifest has `INTERNET`+`CAMERA`, but `scripts/verify-offline.sh` only checks `app/build` conditionally, ignores `google-services`/`c2dm`, and does not inspect `<service>`/`<receiver>`/`<provider>` entries; `libs/build.gradle` still uses `io.altercpre:anytype-heart-android` instead of `io.aparok:middleware`; `SovereignOfflineMatrixTest` is a tautology test; no mutation proof. | 2026-11-25 |
| R2 | Local Profile & First-Launch Flow | 6% | PARTIAL | `OnboardingProfileScreen` exists, but full N1 -> N2 -> N4 flow, Splash N1/N7/N4 routing, <=512px EXIF-stripped photo verification, and instrumented cold-start -> Home test + mutation proof are missing. | 2026-11-25 |
| R3 | Chat, Media, Files & Everything Offline | 14% | PARTIAL | `UrlBuilderTest` & `DefaultUserPermissionProviderTest` exist, but `AparokMediaAndChatOfflineTest` and `SovereignOfflineMatrixTest` are tautology tests; chat permission/read-only path, `UrlBuilder` token, file `Done` state, offline bookmarks, N10 status UI, `FileLimits` removal, Go chat/gateway tests & mutation proof are incomplete. | 2026-11-25 |
| R4 | Real Membership Removal | 6% | PARTIAL | `payments`/`MembershipProvider` partially cleaned, but membership, `payments_*`, `FileLimits`/`NodeUsageInfo`, and `playBilling` leftovers remain; `AparokMembershipRemovalTest` lacks real production-caller/mutation proof. | 2026-11-25 |
| R5 | No Invites, Sharing, Update Checks, Extras; Logical Rebrand | 6% | NOT STARTED | Leftovers of `invite`, `ShareSpace`, `Unsplash`, `gallery`, `publish`, `"Anytype"` refs, and redirected `https://github.com/altercpre/aparok-android/releases/latest` literals remain; no real screen/nav-graph & grep CI proof. | 2026-11-25 |
| R6 | Own Core (`aparok-core`) | 8% | PARTIAL | Core CI builds `lib.aar` with `-javapkg io.aparok.mw`, but `core/anytype/bootstrap.go` still registers network/telemetry/discovery/payment/publish/push/unsplash/gallery components, `aparok-core/scripts/verify-offline.sh` uses `2>/dev/null \|\| true` with only 3 banned packages, `go.mod` is still `github.com/anyproto/anytype-heart`, and upstream workflows remain. | 2026-11-25 |
| R7 | Encrypt Everything | 14% | NOT STARTED | Core has no `any-store/v2` `Config.Encryption` `Codec`, no v1 storage upgrade/patch, no per-block `XChaCha20-Poly1305`, no RAM/encrypted tantivy, and no key zeroization/`Lock()`; app `AparokEncryptionManager` is not wired to real core/Room SQLCipher/Tink callers; marker scan does not scan real core data directory. | 2026-11-25 |
| R8 | Storage Location | 7% | PARTIAL | Pointer file concept started, but core `ProbeStorage(dir)`, safe move with SHA-256 manifest + crash-resumable wipe, volume unmount/eject handling, and screens N2, N6, N7 are missing or unwired. | 2026-11-25 |
| R9 | Backup & Restore `.aparok` | 9% | NOT STARTED | Core quiesced `Backup`/`VerifyBackup`/`Restore` (`age` scrypt or Argon2id + STREAM `XChaCha20-Poly1305`, staging, atomic swap, manifest hashes) and screens N3 & N5 are not implemented/wired. | 2026-11-25 |
| R10 | Size | 5% | UNVERIFIED | `docs/SIZE.md` claims 61.8 MB / 57.4 MB, but actual `v1.4.0` GitHub Release APKs are 156.25 MB (`arm64-v8a`) and 149.11 MB (`armeabi-v7a`) — failing the < 80 MB / < 75 MB targets. | 2026-11-25 |
| R11 | Signing & Release System | 4% | PARTIAL | `scripts/release/ensure-signing.sh` and `release/CERT_SHA256` exist, but `ensure-signing.sh:29` uses `"CN=Aparok, O=Aparok Sovereign, C=US"` instead of `"CN=Aparok"` only, release script/workflow still has `'aparok123456'` fallback, and `scripts/distribution/*secrets*` / `.gpg` leftovers exist. | 2026-11-25 |
| R12 | Offline E2E + Final Audit | 7% | NOT STARTED | No `reactivecircus/android-emulator-runner` CI job exists in `.github/workflows/workflow.yml`; `scripts/verify-sovereign-offline.sh` checks host runner `/proc/net/tcp` instead of emulator app UID via `adb` (`/proc/net/tcp`, `/proc/net/tcp6`, `logcat`). | 2026-11-25 |

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
- Release script v2+v3 verification and cert pin present (subject still needs `CN=Aparok` only)

**Still wrong (pre-R0 baseline):**
- (a) Membership references & `playBilling` in `gradle/libs.versions.toml`
- (b) `FileLimits`/`NodeUsageInfo` still present
- (c) `invite`, `ShareSpace`, `Unsplash`, `gallery`, `publish`, redirected `releases/latest` links
- (d) Kotlin files containing `"Anytype"`
- (e) `DefaultUserPermissionProvider` & chat permission handling
- (f) Signing: hardcoded `"aparok123456"` fallback; GPG file present; subject includes `O=Aparok Sovereign, C=US`
- (g) `verify-offline.sh`: conditional merged-manifest check; ignores `google-services`/`c2dm`
- (h) Middleware coordinates: `io.altercpre:anytype-heart-android`

### CORE (aparok-core @ 6f33e70)
**NOT DONE:**
- `go.mod`: `module github.com/anyproto/anytype-heart` with 0 altercpre references
- `go.mod` still requires upstream `any-sync v0.13.8`, `any-store v1.0.3`, `any-store/v2 v2.5.0`
- `bootstrap.go` registers ALL network components (`nodeconf`, `yamux`, `quic`, `pool`, etc.)
- Upstream workflows remain (`anyblock-proto-check.yml`, `build.yml`, `golangci-lint.yml`, `nightly.yml`, `smoke-tests.yml`, `test.yml`)
- README says "encrypted" with no encryption code
- R6–R9: NOT DONE

---

## Reset Baseline Note

This status tracker was reset to an honest `[00%]` baseline following an independent audit against `docs/agent/APAROK_SPEC.md` and the R3 evidence rules:
- Prior `PROVEN` claims relied on tautological unit tests (`AparokMediaAndChatOfflineTest`, `SovereignOfflineMatrixTest`) that do not exercise real `src/main` production paths, lacked mutation-and-revert CI proof, and lacked screen-reachable `file:line` call sites.
- `aparok-core/core/anytype/bootstrap.go` still registers upstream network, telemetry, discovery, payment, publish, push, Unsplash, and gallery components, while `aparok-core/scripts/verify-offline.sh` silently swallows `go list` failures (`2>/dev/null || true`).
- R7 (at-rest core/app encryption), R8 (storage probe/move/unmount/N2/N6/N7), R9 (`.aparok` backup/restore/N3/N5), and R12 (emulator CI with `/proc/net/tcp` + `/proc/net/tcp6` UID audit) are not yet genuinely implemented and CI-proven.
- R10 size targets (< 80 MB `arm64-v8a`, < 75 MB `armeabi-v7a`) are `UNVERIFIED` / failing: the `v1.4.0` GitHub Release APKs measure 156.25 MB and 149.11 MB.
- Remediation will proceed in Orders A–F (R3 -> R6 -> R7 -> R8/R9/N1–N10 -> R4/R5/Rebrand -> R12 + Signing `CN=Aparok`), with each phase promoted to `PROVEN` only after green CI, failing mutation + green revert CI runs, and verified screen-reachable `file:line` call sites.
