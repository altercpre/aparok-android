# Milestone R6: Build Own Core lib.aar in CI

## Overview
Phase R6 solves the core dependency sovereignty requirement. Instead of relying indefinitely on stock upstream binaries (`v0.51.6`), `aparok-core` is verified with Go hygiene and native CI compilation of `lib.aar` using `gomobile bind`.

## Verified Implementation Details

### 1. Go Module Hygiene & Resolution (R0 Item 8 Audit)
- Identified that an earlier global string replacement (`anyproto` → `altercpre`) across 2,500 files had broken Go module checksums and dependency resolution in `aparok-core`.
- Per prompt R0 item 8 ("if it does not build, revert it (module path rename is optional; build correctness is not)"), reverted the broken module paths back to valid, resolvable Go modules.
- Upgraded toolchain configuration to Go 1.26 to match dependencies and resolved all dependency download conflicts.

### 2. Core Functional Sovereignty
- **Immediate Local Synced Status**: Confirmed in `core/files/filesync/upload.go` that `IsLocalOnlyMode()` immediately triggers `filesyncstatus.Synced` callback for all file additions, preventing any pending upload or sync spinner states.
- **No Membership / Tier Spoofing**: Reverted the hardwired Tier 5 "Co-Creator" spoof in `core/payments.go`, leaving payments clean and un-spoofed.
- **HTTP Range Seeking**: Confirmed local loopback gateway Range request handling (`ServeContent`) verified by `TestGetFile_RangeSeeking` in `pkg/lib/gateway/gateway_test.go`.

### 3. Native Gomobile Build Workflow
- Created `.github/workflows/build-and-test-core.yml` in `altercpre/aparok-core`:
  - Automatically runs Go tests (`TestGetFile_RangeSeeking`).
  - Downloads arch-specific tantivy native static libraries via `make download-tantivy-all`.
  - Installs Android NDK 28 and gomobile tools.
  - Builds `lib.aar` targeting Android API 26 with tags:
    `nogrpcserver gomobile nowatchdog nosigar nomutexdeadlockdetector timetzdata rasterizesvg`
  - Packages and uploads `lib.aar` as a workflow artifact and publishes it to GitHub Release `v0.51.6-aparok`.

### 4. Android App Middleware Integration
- Pinned `middleware.lock` to release `v0.51.6-aparok` with SHA-256 `8fe49dc3c38b629eaf1d0389d0a862ba2e27a6bb2a63ab2a6e774e5471db32b6`.
- Updated `scripts/setup-middleware.sh` to enforce hash verification against `middleware.lock`.
- Verified core CI run [45421260261](https://github.com/altercpre/aparok-core/actions/runs/45421260261) passed with 100% green: `scripts/verify-offline.sh` passed, `TestGetFile_RangeSeeking` passed, and `lib.aar` published with targets `android/arm64,android/arm`.
