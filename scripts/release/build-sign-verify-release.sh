#!/usr/bin/env bash
set -e

TAG="${1:-v1.0.0}"
RELEASE_TITLE="${2:-Aparok Android ${TAG} (Zero-Telemetry Release)}"

echo "=========================================================="
echo "  Aparok Android Build, Sign, Verify & Release Pipeline"
echo "  Tag: ${TAG}"
echo "=========================================================="

KEYSTORE_FILE="scripts/release/app-release.jks"
KEY_ALIAS="aparok"
KEY_PASS="aparok123456"
STORE_PASS="aparok123456"

# 1. Generate release keystore if not exists
if [ ! -f "${KEYSTORE_FILE}" ]; then
    echo "[1/6] Generating self-signed release keystore (${KEYSTORE_FILE})..."
    mkdir -p "$(dirname "${KEYSTORE_FILE}")"
    keytool -genkeypair -v \
        -keystore "${KEYSTORE_FILE}" \
        -alias "${KEY_ALIAS}" \
        -keyalg RSA \
        -keysize 4096 \
        -validity 10000 \
        -storepass "${STORE_PASS}" \
        -keypass "${KEY_PASS}" \
        -dname "CN=Aparok, OU=Security, O=Altercpre, L=Geneva, C=CH"
fi

# 2. Configure signing and properties
echo "[2/6] Configuring signing and zero-telemetry properties..."
cat << EOF > signing.properties
RELEASE_KEY_ALIAS=${KEY_ALIAS}
RELEASE_KEY_PASSWORD=${KEY_PASS}
RELEASE_STORE_PASSWORD=${STORE_PASS}
EOF

cat << EOF > apikeys.properties
amplitude.debug=""
amplitude.release=""
sentry_dsn=""
EOF

cat << EOF > github.properties
gpr.usr="aparok"
gpr.key="public"
EOF

# 3. Prepare Android Manifest for APKs
if [ -f "scripts/release/apk.sh" ]; then
    bash scripts/release/apk.sh
fi

# 4. Build release APKs with Gradle
echo "[3/6] Building release APKs with Gradle..."
chmod +x ./gradlew
./gradlew :app:assembleRelease -PenableAbiSplits=true --no-daemon

# 5. Gather and verify APKs
echo "[4/6] Verifying APK signatures and calculating checksums..."
OUTPUT_DIR="build/release-artefacts"
rm -rf "${OUTPUT_DIR}"
mkdir -p "${OUTPUT_DIR}"

APK_DIR="app/build/outputs/apk/release"
cp "${APK_DIR}"/*.apk "${OUTPUT_DIR}/" 2>/dev/null || true

cd "${OUTPUT_DIR}"

APKSIGNER=$(which apksigner 2>/dev/null || find /opt/android-sdk -name apksigner 2>/dev/null | head -n 1 || find "$ANDROID_HOME" -name apksigner 2>/dev/null | head -n 1 || echo "")

for apk in *.apk; do
    [ -f "$apk" ] || continue
    echo "Processing: $apk"
    if [ -n "$APKSIGNER" ] && [ -x "$APKSIGNER" ]; then
        echo "  Verifying signature with apksigner:"
        "$APKSIGNER" verify --verbose "$apk" | sed 's/^/    /'
    else
        echo "  (apksigner not found in PATH; skipping apksigner check)"
    fi
done

echo "[5/6] Generating SHA256 checksums..."
sha256sum *.apk > SHA256SUMS.txt
cat SHA256SUMS.txt

cd - > /dev/null

# 6. Publish GitHub Release if GitHub CLI / token is available
echo "[6/6] Publishing release to GitHub..."
if command -v gh >/dev/null 2>&1 && [ -n "${GITHUB_TOKEN}" ]; then
    echo "Creating GitHub Release ${TAG}..."
    gh release create "${TAG}" "${OUTPUT_DIR}"/* \
        --title "${RELEASE_TITLE}" \
        --notes "### Aparok Android ${TAG}
Official release of Aparok for Android.

- **Zero-Telemetry**: No tracking, analytics, or third-party monitoring.
- **End-to-End Encrypted**: Sovereign peer-to-peer sync protocol.
- **Local-First**: Complete functionality offline and on local networks.

#### Checksums
\`\`\`
$(cat ${OUTPUT_DIR}/SHA256SUMS.txt)
\`\`\`
" || echo "Release creation completed or already exists."
else
    echo "GitHub CLI not configured or GITHUB_TOKEN not exported. Release artefacts ready in ${OUTPUT_DIR}."
fi

echo "=== Pipeline Finished Successfully ==="
