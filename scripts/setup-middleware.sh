#!/usr/bin/env bash
set -euo pipefail

echo "=== Setting up Aparok Core Middleware (lib.aar) ==="

EXPECTED_SHA="8fe49dc3c38b629eaf1d0389d0a862ba2e27a6bb2a63ab2a6e774e5471db32b6"
DOWNLOAD_URL="https://github.com/altercpre/aparok-core/releases/download/v0.51.6-aparok/lib.aar"

mkdir -p libs

NEED_DOWNLOAD=1
if [ -f "libs/lib.aar" ]; then
    CURRENT_SHA=$(sha256sum libs/lib.aar | awk '{print $1}')
    if [ "$CURRENT_SHA" = "$EXPECTED_SHA" ]; then
        echo "libs/lib.aar already present and verified by SHA-256."
        NEED_DOWNLOAD=0
    else
        echo "libs/lib.aar checksum mismatch. Re-downloading..."
    fi
fi

if [ "$NEED_DOWNLOAD" -eq 1 ]; then
    echo "Downloading sovereign core middleware (v0.51.6-aparok)..."
    curl -fsSL "$DOWNLOAD_URL" -o libs/lib.aar
    CURRENT_SHA=$(sha256sum libs/lib.aar | awk '{print $1}')
    if [ "$CURRENT_SHA" != "$EXPECTED_SHA" ]; then
        echo "ERROR: Downloaded lib.aar checksum $CURRENT_SHA does not match expected $EXPECTED_SHA!"
        exit 1
    fi
    echo "✓ Downloaded lib.aar verified."
fi

chmod +x ./gradlew
echo "Publishing lib.aar to mavenLocal()..."
./gradlew :libs:publishToMavenLocal --no-daemon
echo "✓ Core middleware published to mavenLocal() successfully."
