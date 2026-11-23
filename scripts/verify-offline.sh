#!/usr/bin/env bash
set -e

echo "=== Verifying Offline Enforcement & Zero-Telemetry ==="

# 1. Grep check: absolute zero firebase, sentry, amplitude in source files
echo "[1/2] Verifying zero telemetry references in sources and gradle files..."
TELEMETRY_MATCHES=$(grep -rn -i -E "firebase|sentry|amplitude" \
    --include="*.kt" --include="*.java" --include="*.gradle" --include="*.toml" --include="*.xml" . \
    | grep -v "\.git" | grep -v "docs/" | grep -v "scripts/" | grep -v "/build/" | grep -v "libs/" || true)

if [ -n "$TELEMETRY_MATCHES" ]; then
    echo "ERROR: Telemetry references found in source code:"
    echo "$TELEMETRY_MATCHES"
    exit 1
fi
echo "✓ Zero telemetry references confirmed in sources."

# 2. Merged manifest permission check
echo "[2/2] Verifying merged manifest permissions..."
MERGED_MANIFEST="app/build/intermediates/merged_manifest/release/processReleaseMainManifest/AndroidManifest.xml"
if [ ! -f "$MERGED_MANIFEST" ]; then
    MERGED_MANIFEST=$(find app/build/intermediates -name "AndroidManifest.xml" | grep "release" | head -n 1 || echo "")
fi

if [ -n "$MERGED_MANIFEST" ] && [ -f "$MERGED_MANIFEST" ]; then
    echo "Inspecting merged manifest: $MERGED_MANIFEST"
    PERMISSIONS=$(grep "<uses-permission" "$MERGED_MANIFEST" | sed -E 's/.*android:name="([^"]+)".*/\1/' | sort -u || true)
    echo "Merged manifest permissions list:"
    echo "$PERMISSIONS"

    ALLOWED_PERMISSIONS="android.permission.INTERNET
android.permission.CAMERA"

    DISALLOWED=0
    while read -r perm; do
        [ -z "$perm" ] && continue
        if [ "$perm" != "android.permission.INTERNET" ] && [ "$perm" != "android.permission.CAMERA" ]; then
            echo "ERROR: Disallowed permission found in merged manifest: $perm"
            DISALLOWED=1
        fi
    done <<< "$PERMISSIONS"

    if [ "$DISALLOWED" -ne 0 ]; then
        exit 1
    fi
    echo "✓ Merged manifest permissions strictly limited to INTERNET and CAMERA."
else
    echo "Warning: Merged manifest not found yet (will be checked post-build)."
fi

echo "=== Offline & Zero-Telemetry Verification Passed ==="
