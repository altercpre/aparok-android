#!/usr/bin/env bash
set -euo pipefail

echo "=== Aparok Idempotent Signing Keystore & Pinning Setup ==="

KEYSTORE_FILE="scripts/release/app-release.jks"
KEY_ALIAS="aparok"
CERT_PIN_FILE="release/CERT_SHA256"

mkdir -p scripts/release release

if [ ! -f "${KEYSTORE_FILE}" ]; then
    if [ -n "${RELEASE_KEYSTORE_BASE64:-}" ]; then
        echo "Restoring keystore from RELEASE_KEYSTORE_BASE64 environment secret..."
        echo "${RELEASE_KEYSTORE_BASE64}" | base64 -d > "${KEYSTORE_FILE}"
    else
        echo "Generating production-grade sovereign keystore (RSA 4096, 10000 days, CN=Aparok)..."
        STORE_PASS="${RELEASE_STORE_PASS:-$(openssl rand -base64 32)}"
        KEY_PASS="${RELEASE_KEY_PASS:-$(openssl rand -base64 32)}"

        keytool -genkeypair -v \
            -keystore "${KEYSTORE_FILE}" \
            -alias "${KEY_ALIAS}" \
            -keyalg RSA \
            -keysize 4096 \
            -validity 10000 \
            -storepass "${STORE_PASS}" \
            -keypass "${KEY_PASS}" \
            -dname "CN=Aparok, O=Aparok Sovereign, C=US" \
            -storetype JKS

        echo "✓ Generated ${KEYSTORE_FILE}."
    fi
fi

# Extract and pin SHA256 fingerprint
CURRENT_FP=$(keytool -list -v -keystore "${KEYSTORE_FILE}" -alias "${KEY_ALIAS}" -storepass "${RELEASE_STORE_PASS:-$STORE_PASS}" 2>/dev/null | grep "SHA256:" | head -n 1 | awk '{print $NF}' || true)

if [ -n "${CURRENT_FP}" ]; then
    if [ ! -f "${CERT_PIN_FILE}" ]; then
        echo "${CURRENT_FP}" > "${CERT_PIN_FILE}"
        echo "Pinned certificate SHA-256 fingerprint to ${CERT_PIN_FILE}: ${CURRENT_FP}"
    else
        PINNED_FP=$(cat "${CERT_PIN_FILE}" | tr -d '[:space:]')
        if [ "${CURRENT_FP}" != "${PINNED_FP}" ]; then
            echo "ERROR: Keystore fingerprint mismatch! Expected: ${PINNED_FP}, Got: ${CURRENT_FP}"
            exit 1
        fi
        echo "✓ Certificate fingerprint matches pinned value: ${CURRENT_FP}"
    fi
fi

echo "=== Signing verification completed successfully ==="
