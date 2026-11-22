#!/usr/bin/env bash
set -e

echo "=== Setting up Aparok Android Build Environment in Codespaces ==="

ANDROID_HOME="/opt/android-sdk"
sudo mkdir -p "${ANDROID_HOME}/cmdline-tools"
sudo chown -R "$(whoami):$(whoami)" "${ANDROID_HOME}"

# Download Android commandline-tools if not present
if [ ! -d "${ANDROID_HOME}/cmdline-tools/latest" ]; then
    echo "Downloading Android SDK Command-line Tools..."
    CMDLINE_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip"
    curl -fsSL "${CMDLINE_TOOLS_URL}" -o /tmp/cmdline-tools.zip
    unzip -q /tmp/cmdline-tools.zip -d /tmp/cmdline-tools
    mv /tmp/cmdline-tools/cmdline-tools "${ANDROID_HOME}/cmdline-tools/latest"
    rm -rf /tmp/cmdline-tools.zip /tmp/cmdline-tools
fi

export PATH="${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools:${PATH}"

# Accept all licenses
echo "Accepting Android SDK licenses..."
yes | sdkmanager --licenses > /dev/null 2>&1 || true

# Install required platforms, build tools, and NDK
echo "Installing Android SDK packages (Platform 36, NDK 28, CMake)..."
sdkmanager "platform-tools" \
           "platforms;android-36" \
           "platforms;android-34" \
           "build-tools;34.0.0" \
           "cmake;3.22.1" \
           "ndk;28.1.13356709" > /dev/null 2>&1 || true

# Pre-generate zero-telemetry local config
touch apikeys.properties
echo 'amplitude.debug=""' > apikeys.properties
echo 'amplitude.release=""' >> apikeys.properties
echo 'sentry_dsn=""' >> apikeys.properties

touch github.properties
echo 'gpr.usr="aparok"' > github.properties
echo 'gpr.key="public"' >> github.properties

# Make scripts executable
chmod +x ./gradlew
chmod +x scripts/release/*.sh 2>/dev/null || true

echo "=== Aparok Android Codespaces Setup Complete ==="
echo "Run: bash scripts/release/build-sign-verify-release.sh [version_tag]"
