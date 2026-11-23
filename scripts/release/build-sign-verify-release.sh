#!/usr/bin/env bash
set -e

TAG="${1:-v1.1.0}"
RELEASE_TITLE="${2:-Aparok Android ${TAG} (Zero-Telemetry Sovereign Release)}"

echo "=========================================================="
echo "  Aparok Android Build, Sign, Verify & Release Pipeline"
echo "  Tag: ${TAG}"
echo "=========================================================="

KEYSTORE_FILE="scripts/release/app-release.jks"
KEY_ALIAS="aparok"
KEY_PASS="aparok123456"
STORE_PASS="aparok123456"
CERT_PIN_FILE="release/CERT_SHA256"

# 1. Restore release keystore from secret or generate if not exists
if [ ! -f "${KEYSTORE_FILE}" ]; then
    if [ -n "${RELEASE_KEYSTORE_BASE64}" ]; then
        echo "[1/6] Restoring release keystore from RELEASE_KEYSTORE_BASE64 secret..."
        mkdir -p "$(dirname "${KEYSTORE_FILE}")"
        echo "${RELEASE_KEYSTORE_BASE64}" | base64 -d > "${KEYSTORE_FILE}"
    else
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
            -dname "CN=Aparok"
    fi
fi

# Pin and verify certificate fingerprint
CURRENT_FP=$(keytool -list -v -keystore "${KEYSTORE_FILE}" -alias "${KEY_ALIAS}" -storepass "${STORE_PASS}" 2>/dev/null | grep "SHA256:" | head -n 1 | awk '{print $2}')
if [ -n "${CURRENT_FP}" ]; then
    if [ ! -f "${CERT_PIN_FILE}" ]; then
        mkdir -p "$(dirname "${CERT_PIN_FILE}")"
        echo "${CURRENT_FP}" > "${CERT_PIN_FILE}"
        echo "Pinned release certificate SHA-256 fingerprint: ${CURRENT_FP}"
    else
        PINNED_FP=$(cat "${CERT_PIN_FILE}" | tr -d '[:space:]')
        if [ "${CURRENT_FP}" != "${PINNED_FP}" ]; then
            echo "ERROR: Certificate fingerprint mismatch!"
            echo "Expected pinned: ${PINNED_FP}"
            echo "Found in key:    ${CURRENT_FP}"
            exit 1
        fi
        echo "Release certificate fingerprint verified against ${CERT_PIN_FILE}: ${CURRENT_FP}"
    fi
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

# 4. Prepare Core Middleware (lib.aar) & publish to mavenLocal
echo "[3/6] Ensuring core middleware (lib.aar) is available in mavenLocal..."
if [ ! -f "libs/lib.aar" ]; then
    echo "  Downloading pre-compiled anytype-heart-android v0.51.6 middleware..."
    mkdir -p libs
    curl -fsSL "https://github.com/anyproto/anytype-heart/releases/download/v0.51.6/android_lib_v0.51.6.tar.gz" -o libs/android_lib.tar.gz
    tar -xzf libs/android_lib.tar.gz -C libs/
    rm -f libs/android_lib.tar.gz
fi

chmod +x ./gradlew
echo "  Publishing lib.aar to mavenLocal()..."
./gradlew :libs:publishToMavenLocal --no-daemon

# 5. Build release APKs with Gradle (per-ABI splits only, no universal APK)
echo "[4/6] Building release APKs with Gradle..."
./gradlew :app:assembleRelease -PenableAbiSplits=true --no-daemon

# 5.5 Verify Offline & Merged Manifest Permissions
echo "[Verification] Running offline and zero-telemetry verification..."
bash scripts/verify-offline.sh

# 6. Gather and verify APKs
echo "[5/6] Verifying APK signatures and calculating checksums..."
OUTPUT_DIR="build/release-artefacts"
rm -rf "${OUTPUT_DIR}"
mkdir -p "${OUTPUT_DIR}"

APK_DIR="app/build/outputs/apk/release"
# Copy ONLY per-ABI APKs (arm64-v8a and armeabi-v7a). DO NOT copy or publish universal APK!
cp "${APK_DIR}"/*-arm64-v8a*.apk "${OUTPUT_DIR}/" 2>/dev/null || true
cp "${APK_DIR}"/*-armeabi-v7a*.apk "${OUTPUT_DIR}/" 2>/dev/null || true

cd "${OUTPUT_DIR}"

APKSIGNER=$(which apksigner 2>/dev/null || find /usr/local/lib/android -name apksigner 2>/dev/null | head -n 1 || find "$ANDROID_HOME" -name apksigner 2>/dev/null | head -n 1 || echo "")

for apk in *.apk; do
    [ -f "$apk" ] || continue
    echo "Processing: $apk"
    if [ -n "$APKSIGNER" ] && [ -x "$APKSIGNER" ]; then
        echo "  Verifying signature with apksigner (v2 + v3):"
        "$APKSIGNER" verify --verbose --print-certs "$apk" | sed 's/^/    /'
    else
        echo "  (apksigner not found in PATH; skipping apksigner check)"
    fi
done

echo "Generating SHA256 checksums..."
sha256sum *.apk > SHA256SUMS.txt
cat SHA256SUMS.txt

cd - > /dev/null

# 7. Publish GitHub Release if GitHub CLI / token is available (outside CI)
if [ -z "${CI}" ] && command -v gh >/dev/null 2>&1 && [ -n "${GITHUB_TOKEN}" ]; then
    echo "[6/6] Publishing release to GitHub via gh CLI..."
    gh release create "${TAG}" "${OUTPUT_DIR}"/* \
        --title "${RELEASE_TITLE}" \
        --notes "### Aparok Android ${TAG}
Official release of Aparok for Android.

- **Zero-Telemetry**: No tracking, analytics, or third-party monitoring.
- **End-to-End Encrypted**: Sovereign peer-to-peer sync protocol.
- **Local-First**: Complete functionality offline and on local networks.
- **Permissionless**: Unlocked lifetime access, no paywalls or memberships.

#### Checksums
\`\`\`
$(cat ${OUTPUT_DIR}/SHA256SUMS.txt)
\`\`\`
" || echo "Release creation completed or already exists."
else
    echo "[6/6] Release artefacts ready in ${OUTPUT_DIR}:"
    ls -lh "${OUTPUT_DIR}"
fi

echo "=== Pipeline Finished Successfully ==="
