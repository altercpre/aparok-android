# Milestone R3: Chat, Media, and Files Offline (100% Sovereign Offline Operation)

## Overview
Phase R3 guarantees sovereign offline functionality for chat messaging, media streaming (with HTTP Range video seeking), and file storage across the Android app and core middleware.

## Verified Implementation Details

### 1. Default Space & Object Permissions
- **File**: `domain/src/main/java/com/anytypeio/anytype/domain/multiplayer/DefaultUserPermissionProvider.kt`
- Hardwired local user as `SpaceMemberPermissions.OWNER` across `get(space)`, `observe(space)`, and `all()`.
- Eliminates any dependency on remote coordinator sync or external participant verification when creating objects, sending messages, or modifying spaces.

### 2. Immediate Local Chat Write & Synced Status
- **File**: `feature-chats/src/main/java/com/anytypeio/anytype/feature_chats/presentation/ChatViewModel.kt`
- Sent messages are written to the local database immediately via `addChatMessage.async()`.
- Set `isSynced = true` unconditionally in `buildMessageView`, eliminating clock icons, pending indicators, or retry spinners.
- Local write is recognized as definitive success.

### 3. Media Streaming & Video Seeking (HTTP Range Support)
- **App Side**: `domain/src/main/java/com/anytypeio/anytype/domain/misc/UrlBuilder.kt` and `GatewayProvider.kt` construct URLs to local loopback gateway (`/file/<id>`, `/image/<id>`).
- **Core Side**: `aparok-core/pkg/lib/gateway/gateway.go` serves files and images using Go standard library `http.ServeContent`, natively supporting `Range: bytes=start-end` and returning HTTP `206 Partial Content`.
- **Core Verification Test**: Added `TestGetFile_RangeSeeking` in `aparok-core/pkg/lib/gateway/gateway_test.go`:
  - Starts the gateway test fixture on local loopback.
  - Submits HTTP GET with header `Range: bytes=0-100`.
  - Asserts HTTP status `206 Partial Content`, `Content-Range: bytes 0-100/200`, `Content-Type: video/mp4`, and verifies byte slice integrity.

### 4. Zero Server Limits & Immediate File Completion
- **Core Side**: `aparok-core/core/files/filesync/upload.go`
  - In `AddFile(req AddFileRequest)`, under `IsLocalOnlyMode()`, immediately notifies all registered status callbacks with `filesyncstatus.Synced`.
  - Bypasses remote file node queues and avoids parking files in `PendingUpload`, `Uploading`, or `Limited` states.
