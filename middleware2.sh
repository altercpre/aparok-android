#!/usr/bin/env bash
set -e

# Setup local properties for sovereign zero-telemetry build
GITHUB_USER_PROPERTY="gpr.usr"
GITHUB_KEY_PROPERTY="gpr.key"
AMPLITUDE_DEBUG_PROPERTY="amplitude.debug"
AMPLITUDE_RELEASE_PROPERTY="amplitude.release"
SENTRY_DSN_PROPERTY="sentry_dsn"

cat << EOF > github.properties
${GITHUB_USER_PROPERTY}=aparok
${GITHUB_KEY_PROPERTY}=public
EOF

cat << EOF > apikeys.properties
${AMPLITUDE_DEBUG_PROPERTY}=""
${AMPLITUDE_RELEASE_PROPERTY}=""
${SENTRY_DSN_PROPERTY}=""
EOF

echo "Configured zero-telemetry github.properties and apikeys.properties."

if [ ! -f "libs/lib.aar" ]; then
    echo "Downloading pre-compiled middleware lib.aar..."
    mkdir -p libs
    curl -fsSL "https://github.com/anyproto/anytype-heart/releases/download/v0.51.6/android_lib_v0.51.6.tar.gz" -o libs/android_lib.tar.gz
    tar -xzf libs/android_lib.tar.gz -C libs/
    rm -f libs/android_lib.tar.gz
fi

chmod +x ./gradlew
./gradlew :libs:publishToMavenLocal --no-daemon
