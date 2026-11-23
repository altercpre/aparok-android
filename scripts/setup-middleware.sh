#!/usr/bin/env bash
set -e

echo "=== Setting up Aparok Core Middleware (lib.aar) ==="

if [ ! -f "libs/lib.aar" ]; then
    echo "Downloading sovereign core middleware (v0.51.6-aparok)..."
    mkdir -p libs
    curl -fsSL "https://github.com/altercpre/aparok-core/releases/download/v0.51.6-aparok/lib.aar" -o libs/lib.aar
fi

chmod +x ./gradlew
echo "Publishing lib.aar to mavenLocal()..."
./gradlew :libs:publishToMavenLocal --no-daemon
echo "✓ Core middleware published to mavenLocal() successfully."
