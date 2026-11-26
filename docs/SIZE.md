# Aparok Release Size Audit (Phase R10)

## Overview
Phase R10 enforces application size constraints per `docs/agent/APAROK_SPEC.md`:
- **arm64-v8a APK Target**: `< 80 MB`
- **armeabi-v7a APK Target**: `< 75 MB`
- **Universal APK Target**: Strictly `false` (no multi-ABI universal APK published)

> **Current Status:** `UNVERIFIED / FAILING` (per `docs/STATUS.md` — `v1.4.0` release APKs exceed targets).

---

## 1. Size Metrics & Baseline Analysis

| Artifact / Component | Measured Baseline (`v1.4.0`) | Target | Status |
| :--- | :--- | :--- | :--- |
| **Universal APK** | **0 MB (Disabled)** | Disabled (`universalApk false`) | **DISABLED (`app/build.gradle:183`)** |
| **arm64-v8a Release APK** | **156.25 MB** | `< 80 MB` | **UNVERIFIED / FAILING** |
| **armeabi-v7a Release APK** | **149.11 MB** | `< 75 MB` | **UNVERIFIED / FAILING** |
| **lib.aar Middleware** | **Pending R10 measurement** (`arm64` + `arm` release) | Stripped release ABIs only | **UNVERIFIED** |

---

## 2. Optimization Levers Applied vs Pending

1. **ABI Split Enforcement (`app/build.gradle:176-185`) — Applied**:
   - `universalApk false`: Prevents packaging all native architectures into a single monolithic binary.
   - Restricts shipped release ABIs to `arm64-v8a` and `armeabi-v7a` when `-PenableAbiSplits=true` is passed.
2. **Core Middleware Binary Compaction (`aparok-core`) — Applied**:
   - Built with `-trimpath` and `-ldflags "-s -w"` for `android/arm64,android/arm`.
3. **Code Shrinking, Obfuscation & Dependency Cleanup — PENDING (R5/R10)**:
   - `app/proguard-rules.pro:22` still sets `-dontobfuscate` (R8 full obfuscation/shrinking tuning pending).
   - `app/build.gradle:277` still includes `libs.mlkit.barcode.scanning` (and transitive ML Kit / DataTransport components; scheduled for removal in R10).
   - Firebase, Sentry, Amplitude, and Play Billing SDKs are purged from Gradle dependencies, while remaining R5/R10 unused assets, shaders, fonts, and drawables still need cleanup to bring APKs below `< 80 MB` (`arm64-v8a`) and `< 75 MB` (`armeabi-v7a`).
