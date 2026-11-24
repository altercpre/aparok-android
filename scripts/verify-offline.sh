#!/usr/bin/env bash
set -euo pipefail

echo "=== Verifying Offline Enforcement & Zero-Telemetry ==="

FAIL=0

# 1. Grep check: absolute zero firebase, sentry, amplitude, google-services, or billingclient in source files
echo "[1/2] Verifying zero telemetry/billing references in sources and gradle files..."
TELEMETRY_MATCHES=$(grep -rn -i -E "firebase|sentry|amplitude|playBilling|billingclient" \
    --include="*.kt" --include="*.java" --include="*.gradle" --include="*.toml" --include="*.xml" . \
    | grep -v "\.git" | grep -v "docs/" | grep -v "scripts/" | grep -v "/build/" | grep -v "libs/" | grep -v 'tools:node="remove"' || true)

if [ -n "$TELEMETRY_MATCHES" ]; then
    echo "ERROR: Telemetry/billing references found in source code:"
    echo "$TELEMETRY_MATCHES"
    FAIL=1
fi

if [ "$FAIL" -eq 0 ]; then
    echo "✓ Zero telemetry/billing references confirmed in sources."
fi

# 2. Merged manifest permission check (STRICT: FAIL if manifest not found post-build)
echo "[2/2] Verifying merged manifest permissions..."
MERGED_MANIFEST="app/build/intermediates/merged_manifest/release/processReleaseMainManifest/AndroidManifest.xml"
if [ ! -f "$MERGED_MANIFEST" ]; then
    MERGED_MANIFEST=$(find app/build/intermediates -name "AndroidManifest.xml" 2>/dev/null | grep "release" | head -n 1 || echo "")
fi

if [ -z "$MERGED_MANIFEST" ] || [ ! -f "$MERGED_MANIFEST" ]; then
    # In CI after a build, the merged manifest MUST exist
    if [ -d "app/build" ]; then
        echo "ERROR: Merged manifest not found after build! Cannot verify permissions."
        FAIL=1
    else
        echo "SKIP: No build output yet (pre-build check). Manifest will be verified post-build."
    fi
else
    echo "Inspecting merged manifest: $MERGED_MANIFEST"
    PERMISSIONS=$(grep "<uses-permission" "$MERGED_MANIFEST" | sed -E 's/.*android:name="([^"]+)".*/\1/' | sort -u || true)
    echo "Merged manifest permissions list:"
    echo "$PERMISSIONS"

    while read -r perm; do
        [ -z "$perm" ] && continue
        if [ "$perm" != "android.permission.INTERNET" ] && \
           [ "$perm" != "android.permission.CAMERA" ] && \
           [[ "$perm" != *DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION* ]]; then
            echo "ERROR: Disallowed permission found in merged manifest: $perm"
            FAIL=1
        fi
    done <<< "$PERMISSIONS"

    if [ "$FAIL" -eq 0 ]; then
        echo "✓ Merged manifest permissions strictly limited to INTERNET and CAMERA (with standard dynamic receiver guard)."
    fi
fi

if [ "$FAIL" -ne 0 ]; then
    echo "=== VERIFICATION FAILED ==="
    exit 1
fi

echo "=== Offline & Zero-Telemetry Verification Passed ==="
