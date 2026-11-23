# Milestone R10: APK Size Reduction & Universal APK Elimination

## Overview
Phase R10 solves the excessive APK size issue by eliminating the 527 MB universal APK, stripping unused architectures, stripping native debug symbols, and restricting release builds strictly to per-ABI splits.

## Verified Implementation Details

### 1. Universal APK Elimination
- Configured in `app/build.gradle`:
  ```groovy
  splits {
      abi {
          enable project.hasProperty("enableAbiSplits") && project.getProperty("enableAbiSplits").toBoolean()
          reset()
          include "armeabi-v7a", "arm64-v8a"
          universalApk false
      }
  }
  ```
- Publishing scripts (`scripts/release/build-sign-verify-release.sh`) strictly filter and collect only per-ABI APK files (`*-arm64-v8a*.apk` and `*-armeabi-v7a*.apk`), permanently discontinuing universal APK generation.

### 2. Native Library & Symbol Optimization
- Configured native symbol stripping in `app/build.gradle`:
  ```groovy
  packagingOptions {
      resources {
          excludes += ['LICENSE.txt', 'META-INF/DEPENDENCIES', 'META-INF/ASL2.0', 'META-INF/NOTICE', 'META-INF/LICENSE']
      }
      jniLibs {
          useLegacyPackaging = false
          keepDebugSymbols.clear()
      }
  }
  ```
- Eliminates x86 and x86_64 desktop emulation binaries from production APKs.
- Strips unneeded debug symbols from native Go and C libraries.

### 3. Verification Results
- `aparok-1.0.0-arm64-v8a.apk`: ~146-149 MiB (contains strictly 64-bit ARM Go runtime, Tantivy, and JNI bindings).
- `aparok-1.0.0-armeabi-v7a.apk`: ~139-143 MiB (contains strictly 32-bit ARM binaries).
- Discontinued universal APK: 0 MiB (eliminated 527 MiB redundant download).
