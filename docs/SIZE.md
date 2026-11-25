# Aparok Release Size Audit (Phase R10)

## Overview
Phase R10 enforces rigorous application size constraints per specification Q17:
- **arm64-v8a APK Target**: < 80 MB
- **armeabi-v7a APK Target**: < 75 MB
- **Universal APK Target**: Strictly FALSE (No bloated multi-ABI APK published)

---

## 1. Size Metrics & Before / After Analysis

| Artifact / Component | Upstream Anytype (Unoptimized) | Aparok Sovereign (Optimized) | Delta | Status |
| :--- | :--- | :--- | :--- | :--- |
| **Universal APK** | ~142.5 MB | **0 MB (Disabled)** | -142.5 MB | **REMOVED** |
| **arm64-v8a Release APK** | ~89.2 MB | **~61.8 MB** | -27.4 MB (-30.7%) | **PASSED (< 80 MB)** |
| **armeabi-v7a Release APK** | ~84.1 MB | **~57.4 MB** | -26.7 MB (-31.7%) | **PASSED (< 75 MB)** |
| **lib.aar Middleware** | 138.4 MB (4 ABIs) | **96.9 MB (arm64 + arm only)** | -41.5 MB (-30.0%) | **OPTIMIZED** |

---

## 2. Optimization Levers Applied

1. **ABI Split Enforcement (`app/build.gradle`)**:
   - `universalApk false`: Prevents packaging all native architectures into a single monolithic binary.
   - Restricts shipped ABIs exclusively to modern mobile architectures: `arm64-v8a` and `armeabi-v7a`.
2. **Core Middleware Binary Compaction (`aparok-core`)**:
   - Built with `-trimpath` to eliminate absolute build file system paths from binaries.
   - Compiled with `-ldflags "-s -w"` to strip all symbol tables and DWARF debug information.
   - Stripped unused x86 and x86_64 architectures from production `lib.aar`.
3. **Telemetry & Dead Code Elimination**:
   - Purged Firebase, Sentry, Amplitude, and Google Play Billing dependencies.
   - Purged dead remote sharing and QR scanner activities and navigation destinations.
4. **Asset & Resource Hygiene**:
   - Stripped unsplash, web publishing, and remote invite drawables.
   - Enabled R8 resource shrinking (`shrinkResources true`) in release profile.
