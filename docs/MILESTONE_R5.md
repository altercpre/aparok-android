# Milestone R5: No Invites, Sharing, Update Checks, Extras

## Overview
Phase R5 enforces total offline sovereignty by eliminating all remote web link handling, invite URL generation, space join request workflows, in-app update checks, and external media integrations (Unsplash).

## Verified Implementation Details

### 1. Zero Web Schemes in Manifest & Deep Links
- Manifest intent-filters are restricted strictly to app startup (`MAIN`), local custom deep-links (`aparok://`), and Android system content sharing (`ACTION_SEND`, `ACTION_SEND_MULTIPLE`).
- Zero `http` or `https` schemes or `anytype.io` host filters exist in the manifest.

### 2. Elimination of Remote Invites & Web URL Generation
- **Space Invites**: `DefaultSpaceInviteResolver.createInviteLink()` returns `""`, completely eliminating external web URL generation (including previous redirects to github releases).
- **1-1 Chat Deep Links**: `GenerateOneToOneChatLink` removed `https://hi.any.coop/` web URL construction, replacing it with local peer-to-peer URI format (`aparok://chat/{identity}#{metaDataKey}`).

### 3. Removal of Join Request Workflows
- **Fragments & UI**: Deleted `RequestJoinSpaceFragment.kt`, `SpaceJoinRequestFragment.kt`, and test classes. Removed `requestJoinSpaceScreen` and `spaceJoinRequestScreen` from `app/src/main/res/navigation/graph.xml`.
- **ViewModels & DI**: Deleted `RequestJoinSpaceViewModel.kt`, `SpaceJoinRequestViewModel.kt`, `RequestJoinSpaceDI.kt`, and `SpaceJoinRequestInject.kt`.
- **Domain Interactors**: Deleted `ApproveJoinSpaceRequest.kt`, `CancelJoinSpaceRequest.kt`, `DeclineSpaceJoinRequest.kt`, and `SendJoinSpaceRequest.kt`.
- **Component Cleanliness**: Removed all dependency declarations and component factory bindings from `MainComponent.kt` and `ComponentManager.kt`.

### 4. Removal of In-App Update Checks
- Deleted `AlertUpdateAppFragment.kt` and removed its destination from `app/src/main/res/navigation/graph.xml`.
- Made `Navigator.openUpdateAppScreen()` a no-op.
- Removed update check dialog trigger from `EditorFragment.kt` and `ObjectSetViewModel.kt`.

### 5. Unsplash & External Media Removal
- Disabled Unsplash UI in cover picker: set `android:visibility="gone"` on `btnUnsplash` in `fragment_doc_cover_gallery.xml` and removed click handlers in `SelectCoverGalleryFragment.kt`. Covers strictly use local bundled assets or user-picked images.
- Neutralized `UnsplashDataRepository`: searches return `emptyList()` and downloads return empty hash, ensuring zero remote network requests are attempted.
