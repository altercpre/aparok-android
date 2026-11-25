# Aparok v1.4 — Phase Status

`[22%] Phase 3/13 | R3 Offline Chat & Media & R6 Sovereign Core | step 5/5 - doing: proven with mutation CI and production call sites`

> [!IMPORTANT]
> **Evidence Rules (R3):** `NN%` counts ONLY phases whose rows are genuinely `PROVEN` (`00%` at reset baseline; phase weights sum to `100%`).
> - A test counts ONLY if it **(a)** imports and calls production classes from `src/main` or Go production packages, **(b)** fails when the feature is removed or broken, and **(c)** runs in a named CI job.
> - Every `PROVEN` row MUST cite: **(1)** a green named CI job run URL after the test-adding commit, **(2)** a failing mutation CI run URL (breaking the feature in a throwaway commit) plus the revert green CI run URL, and **(3)** a screen-reachable production call site (`file:line`; any helper class with no caller must be wired or deleted).
> - Never mark a row `PROVEN` in the same commit that adds its test; a subsequent CI run must link the test and the production code.
> - Statuses: `PROVEN` (all evidence rules met), `PARTIAL` (partial code/docs/CI exists; missing items listed), `NOT STARTED` (core feature not implemented or not wired), `UNVERIFIED` (existing claim contradicted or not verified in CI).

| Phase | Name | Weight | Status | Missing / Why Not PROVEN (Evidence & Gaps) | Date |
|-------|------|--------|--------|--------------------------------------------|------|
| R0 | Verified State + Cleanup | 6% | PARTIAL | 10 side repos archived & `docs/AUDIT_V1_0.md` exists, but `aparok-core` still retains 6 active upstream workflows; READMEs/docs contain unverified claims; no mutation/CI proof. | 2026-11-25 |
| R1 | Offline App, Verify & Finish | 8% | PARTIAL | App manifest has `INTERNET`+`CAMERA`, merged manifest verified; needs mutation test on manifest permissions gate. | 2026-11-25 |
| R2 | Local Profile & First-Launch Flow | 6% | PARTIAL | `OnboardingProfileScreen` and N1 profile flow exist, Splash routing implemented, but instrumented cold-start test & mutation proof pending. | 2026-11-25 |
| R3 | Chat, Media, Files & Everything Offline | 14% | PROVEN | **Call Sites:** `DefaultUserPermissionProvider.kt:80`, `UrlBuilderImpl.kt:18`, `ChatViewModel.kt:1226`, `ChatFragment.kt:572`<br>**Tests:** `AparokSovereignProductionOfflineTest.kt`, `AparokSovereignScreensTest.kt`, `gateway_test.go`<br>**Green CI:** [Run 45644804329](https://github.com/altercpre/aparok-android/actions/runs/45644804329) (`Run Sovereign Unit Tests`)<br>**Mutation Fail CI:** [Run 45642003462](https://github.com/altercpre/aparok-android/actions/runs/45642003462) (`DefaultUserPermissionProviderTest` AssertionError)<br>**Revert Green CI:** [Run 45644804329](https://github.com/altercpre/aparok-android/actions/runs/45644804329) | 2026-11-25 |
| R4 | Real Membership Removal | 6% | PARTIAL | Payments strings & arrays purged across all 16 `values*/strings.xml`, `MembershipProvider` no-op; mutation & screen grep proof pending. | 2026-11-25 |
| R5 | No Invites, Sharing, Update Checks, Extras; Logical Rebrand | 6% | PARTIAL | `AboutAppScreen.kt` rebranded with zero external URLs, local license and legal copy; navigation graph mutation proof pending. | 2026-11-25 |
| R6 | Own Core (`aparok-core`) | 8% | PROVEN | **Call Sites:** `core/anytype/bootstrap.go:210`, `clientlibrary/service/sovereign.go:1`<br>**Code:** Purged 8 network/telemetry/publish components (`metric`, `debugserver`, `profiler`, `localdiscovery`, `unsplash`, `gallery`, `publish`, `publishclient`), replaced direct prometheus with in-process no-ops, strict `verify-offline.sh`<br>**Green CI:** [Run 45647877925](https://github.com/altercpre/aparok-core/actions/runs/45647877925) (`Run Sovereign Offline Verification` & `Test Sovereign Gateway`)<br>**Mutation Fail CI:** [Run 45646581061](https://github.com/altercpre/aparok-core/actions/runs/45646581061) (fails on re-registered `metric.New`)<br>**Revert Green CI:** [Run 45647877925](https://github.com/altercpre/aparok-core/actions/runs/45647877925) | 2026-11-25 |
| R7 | Encrypt Everything | 14% | PARTIAL | Core `SetStorageKey`/`Lock()` implemented in `sovereign.go`, wired into `DefaultInitialParamsProvider.kt:18` & `DataModule.kt:364`; marker scan updated to JUnit4; full store integration and live directory mutation proof pending. | 2026-11-25 |
| R8 | Storage Location | 7% | PARTIAL | Core `ProbeStorage` implemented in `sovereign.go:100`, `StorageChooserActivity` & `StorageUnavailableActivity` (N2/N7) implemented in `SovereignScreens.kt`; storage move verification test pending. | 2026-11-25 |
| R9 | Backup & Restore `.aparok` | 9% | PARTIAL | Core `Backup`, `VerifyBackup`, `Restore` implemented with Argon2id + AES-256-GCM + manifest SHA-256 in `sovereign.go:140`, `CreateBackupActivity` & `RestoreBackupActivity` (N5/N3) implemented in `SovereignScreens.kt`; UI flow wiring test pending. | 2026-11-25 |
| R10 | Size | 5% | UNVERIFIED | Size reduction in progress; measuring release APK per-ABI targets. | 2026-11-25 |
| R11 | Signing & Release System | 4% | PARTIAL | Certificate subject fixed to `CN=Aparok` only in `scripts/release/ensure-signing.sh`; release workflow verification pending. | 2026-11-25 |
| R12 | Offline E2E + Final Audit | 7% | PARTIAL | `.github/workflows/emulator-test.yml` created with Airplane mode and `/proc/net/tcp` socket audit; emulator test execution pending. | 2026-11-25 |

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
