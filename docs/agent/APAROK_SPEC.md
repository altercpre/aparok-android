# APAROK MASTER SPEC (v2.0) - read this file, then work phase by phase

This file is the single source of truth for the agent. It lists EVERYTHING the owner asked for (section 2), every screen (section 5), every feature that must work offline (section 6) and the phases (section 7). If something is in this file, it must be done and proven. If you think something is missing, add it to docs/STATUS.md instead of dropping it.

## 0. AGENT PROTOCOL (for a small/fast model: follow exactly)

1. Work on ONE phase at a time. At the start of each phase re-read: section 1 (rules), the ledger rows of that phase (section 2), the phase text (section 7) and the screens it touches (section 5). Do not rely on memory of earlier phases.
2. Keep `docs/STATUS.md` (in the app repo): one row per phase R0-R12 with status PROVEN / PARTIAL / NOT STARTED / BLOCKED, evidence (CI run URL, test name and output, file:line) and date. PROVEN only with a link to a green CI run that executes the check. Code that was only read or compiled is PARTIAL.
3. Every message you write starts with a status line, then "Result:" (exact command and a short output summary) and "Next:":
   `[NN%] Phase X/13 | <name> | step K/N - doing: <what and why>`
   Weights: R0 6, R1 8, R2 6, R3 14, R4 6, R5 6, R6 8, R7 14, R8 7, R9 9, R10 5, R11 4, R12 7 (sum 100). NN = finished phase weights + current share. Raise NN only for verified work. Say plainly when something failed.
4. Split work into small steps. Commit after every step with a plain message and push. If a step fails 3 times, write the cause in docs/STATUS.md, mark it BLOCKED, take the next-best fallback and continue with the next independent step. Never loop forever and never stop to ask the owner.
5. Keep your context small: use grep and line ranges, `git diff --stat`, tails of logs. Never print or paste whole large files or logs.
6. Do the heavy work in your own sandbox and in GitHub Actions. Never ask the owner to build, run or edit anything on a phone.
7. Never claim "done", "100%", "zero telemetry", "encrypted" or "offline" without evidence from a run. Your earlier summary listed repositories and no evidence: do not repeat that.

## 1. HARD RULES

- Logical changes, not renames. Understand what a feature does, then remove, change or keep it deliberately.
- Delete removed features completely: code, DI bindings, view models, strings in ALL locales, drawables, layouts, tests, Gradle dependencies, manifest entries, docs.
- No TODO, no stubs, no placeholders, no commented-out code.
- Never fake a state to hide a feature (no spoofed membership tiers, no fake "synced" for a feature that does not exist).
- Never weaken a check to make it pass (never empty a banned list, never skip a gate).
- KEEP CHAT. Chat text, attachments, media, files, reactions, everything.
- No secrets in the repo, in logs or in messages. Passwords and keys only as GitHub secrets.
- Licensing: the Android app and the core are under the Any Source Available License 1.0 (use, modify and redistribute ONLY for Non-Commercial Use or in Allowed Networks). Keep LICENSE.md verbatim, keep NOTICE.md crediting Anytype / Any Association, never claim MIT or Apache for the app or the core (read each repo's own LICENSE before describing it), state "free, non-commercial" in the README.
- Never use the middleware's built-in export for backups (it reportedly drops synced media).

## 2. REQUIREMENT LEDGER (everything the owner asked for)

| ID | Requirement (owner's words, made precise) | Phase | Proof |
|---|---|---|---|
| Q01 | 100% offline: all online and remote-server features removed or disabled, including server sync, LAN/P2P discovery, remote telemetry and crash reporting (Amplitude, Sentry), push, update checks, Publish to Web | R1 R5 R6 | verify-offline scripts + merged manifest + network proof in R12 |
| Q02 | The app works fully with NO internet and NO mobile data: no waiting, no retry loops, no "offline" banners, no spinners | R3 R12 | emulator test in Airplane mode |
| Q03 | Local profile creation: no sign-up, no login, no email. First launch asks only for a name and a profile picture (picture optional) | R2 | instrumented test |
| Q04 | CHAT STAYS and works offline: messages send, save, show at once and are marked sent/saved | R3 | unit + Go + emulator tests |
| Q05 | Chat images, videos, files, audio, links, camera photo/video work offline (send, view, play, open, save) | R3 | emulator test |
| Q06 | Everything else works offline too: editor (all blocks and media), sets/collections, search, templates, widgets, spaces, import/export, share-in | R3 R12 | emulator test |
| Q07 | Remove membership completely (no tiers, no paywalls, no billing, no badges, no "upgrade" prompts) | R4 | grep proof + CI |
| Q08 | Remove invites, space sharing, QR invites, publish to web, update checks, Unsplash/gallery/AI, server storage quotas | R5 | grep proof + CI |
| Q09 | Encrypt EVERYTHING at rest: messages, text, images, videos, files, search index, caches, thumbnails, temp files, logs, backups, preferences, local databases | R7 | marker scan test |
| Q10 | Backup and restore with a `.aparok` file: encrypted with a password; everything included (chats, notes, images, videos, files, settings) | R9 | round-trip tests |
| Q11 | Custom storage location: internal storage OR external storage OR SD card for ALL app data, chosen by the user, changeable later with a safe move | R8 | probe + move tests |
| Q12 | Permissions: only what is needed. The old list (shared storage, network state, Wi-Fi state, Wi-Fi multicast, wake lock, run at startup, foreground service, c2dm push, ad id) must disappear. Allowed: INTERNET only if proven necessary for the on-device loopback file server, CAMERA runtime-only | R1 R6 | merged-manifest gate in CI |
| Q13 | Privacy and security first: app lock, screenshot protection, no cloud backup, no exported components, no logging of content | R7 | tests + manifest gate |
| Q14 | Rebrand logically, not just a rename: name, identity, copy, onboarding, About, notifications, widgets, icons, colors, deep links, file authorities, package, middleware coordinates, strings in all locales; nothing may describe a removed feature | R0 R5 | grep + screen review |
| Q15 | Fork and rebrand all that is needed, including the Go middleware (aparok-core); archive what is not needed | R0 R6 | repo list + CI |
| Q16 | Own middleware build: the app must use a lib.aar built from aparok-core by CI, not the stock upstream binary | R6 | workflow URL + unzip -l |
| Q17 | Smaller APK: remove junk assets (unused videos, lottie, onboarding media), unused sample code, unused libraries; per-ABI APKs only | R10 | docs/SIZE.md with numbers |
| Q18 | Automated release workflow: merges to main build signed release APKs and publish them to GitHub Releases | R11 | release run URL |
| Q19 | A permanent signing and verification system: keystore kept only as secrets plus a private recovery repo, fingerprint pinned, signature verified (v2+v3), checksums and provenance on every release, never rotated silently | R11 | gates in workflow |
| Q20 | Progress reporting: every message shows percent, phase, step and what is being done | all | status line |
| Q21 | Everything is done in the cloud (agent sandbox + GitHub Actions); nothing is built on the owner's phone | all | n/a |
| Q22 | Complete, production-ready code with no placeholders | all | review |
| Q23 | Think logically: when a rule here conflicts with how the code really works, follow the code, explain in docs/DECISIONS.md and keep the goal | all | docs |

## 3. VERIFIED STATE OF THE REPOS (independent review of the owner's zips)

APP (altercpre/aparok-android, commit a2d44a0). Genuinely done, keep and prove with CI: package io.aparok.app; merged permissions INTERNET + CAMERA only; Firebase, Sentry, Amplitude and push removed; MDNSResolver is a no-op; NetworkConnectionStatus reports NOT_CONNECTED; NetworkMode forced LOCAL; one OnboardingProfileScreen; MembershipProvider is a no-op emptyFlow (not spoofed); modules payments, analytics, crash-reporting, core-ai, gallery-experience, sample removed; release script uses CN=Aparok, verifies v2 and v3, pins a certificate fingerprint, publishes no universal APK.

APP, still wrong:
- 68 files still reference membership (core_models/membership, presentation/membership, Middleware.kt billingId, "Co-Creator" strings); playBilling remains in gradle/libs.versions.toml.
- FileLimits, FileLimitsEvent, NodeUsageInfo live in 24 files (EditorViewModel, FilesStorageViewModel, SpacesStorageViewModel, core-models).
- invite (85 files), ShareSpace (23), Unsplash (28), gallery (76), publish (45) still present; hi.any.coop and object.any.coop URLs remain; 37 literals of https://github.com/altercpre/aparok-android/releases/latest were put where web links used to be (a blind replace). Remove the features, do not redirect links.
- 79 Kotlin files still contain "Anytype" (decide per case: user-visible text and log text become Aparok; internal database names only with a migration).
- DefaultUserPermissionProvider returns the stored permission and falls back to OWNER only when none exists, so a participant with READER or NO_PERMISSIONS still blocks chat. No unit tests exist for it.
- Signing script: the store and key password "aparok123456" is hardcoded in a public repo; without the RELEASE_KEYSTORE_BASE64 secret it generates a NEW keystore on every run (installed users could never update); the pin file is written on the runner and never committed; scripts/release/app-release.gpg and the upstream encrypt/decrypt-secrets scripts are leftovers.
- scripts/verify-offline.sh only warns when the merged manifest is missing and its telemetry grep ignores "billing".
- Middleware coordinates are still io.altercpre:anytype-heart-android (use io.aparok:middleware).
- README: still tells users to create github.properties (PAT) and apikeys.properties; the security contact is the broken address mailto:security@github.com/altercpre/aparok-android/releases/latest; the repo website still points to anytype.io; it claims "encrypted" and "official" with no proof.

CORE (altercpre/aparok-core, commit 6f33e70): NOT DONE. It differs from upstream anytype-heart in exactly 3 files (README.md, core/files/filesync/upload.go which marks files Synced in local-only mode, and a Range test in pkg/lib/gateway/gateway_test.go). go.mod is still `module github.com/anyproto/anytype-heart` with 0 altercpre references; core/anytype/bootstrap.go registers every network component exactly as upstream; makefiles/ci-compile-android-lib.mk still ends the gomobile bind line with `|| true` (hides build failures); no -javapkg; upstream workflows (cla, mirror-any-block, perftests, self-hosted mac mini) remain; the README claims "encrypted" with no encryption code. So the "v0.51.6-aparok" lib.aar is stock middleware plus one line. R6, R7, R8, R9 are NOT STARTED. Never claim otherwise.

SIDE REPOS: aparok-sync, aparok-store/v2, aparok-block, aparok-sync-node, -filenode, -coordinator, -consensusnode, -tools, -docker, aparok-crypto-kotlin have renamed module paths but nothing uses them: aparok-core's go.mod still requires github.com/anyproto/any-sync v0.13.8, any-store v1.0.3 and any-store/v2 v2.5.0. Archive all of them except aparok-sync and aparok-store, which stay only if R7 needs a patch. A `replace` directive needs the replacement module path to equal the replaced one, so revert those two forks to the upstream module paths (or rewrite imports in aparok-core) and prove it with go build.

## 4. BRAND SYSTEM (logical rebrand)

- Name: Aparok. Tagline: "Private notes and chats that never leave your device." One sentence of copy for the whole app: "Everything stays on this device, encrypted."
- Identity already present: the privacy-shield mark (cyan and navy vector). Keep ONE mark everywhere: launcher (adaptive + monochrome), splash, onboarding, About, widget, notification small icon, QR/logo drawables (delete QR logo if QR is removed). Remove every Anytype-named drawable and replace with the Aparok mark.
- Palette tokens in ONE place in core-ui (Compose Color + XML colors): ink #0B1F33, surface dark, accent cyan #19C3D6, danger red, success green. Light and dark themes both use the accent. Do not leave Anytype brand colors as the accent.
- Identifiers: applicationId io.aparok.app (debug io.aparok.app.dev), deep link scheme `aparok://` (main, object, os-widget only), FileProvider authority ${applicationId}.provider, notification channel names, shortcut labels, widget labels, share-intent label "Aparok", middleware coordinates io.aparok:middleware, gomobile Java package io.aparok.mw.
- Copy rules: nothing may mention sync, network, accounts, backup node, keychain phrase, recovery phrase, membership, upgrade, storage quota, invite, share link, publish, "Anytype". Terms stay only where the feature stays (Spaces, Objects, Types, Properties, Sets, Collections, Widgets, Chats).
- Legal credit stays: About and NOTICE.md say "Based on Anytype by Any Association, under the Any Source Available License 1.0. Aparok is free and non-commercial."

## 5. SCREEN-BY-SCREEN SPEC

Status of each screen: NEW (build it), KEEP (verify it works offline), CHANGE (edit as written), REMOVE (delete the class, its view model, DI, strings, layouts, navigation, tests). The app has 118 fragments plus Compose screens; the lists below cover all of them. Use grep to find the real class names and any screen not named here, and decide with the same rules.

### 5.1 NEW screens (exact behavior and English copy; translate the strings into the existing locales or fall back to English)

**N1 Create profile** (first launch, replaces the old onboarding; class OnboardingProfileScreen exists: extend it).
- Step 1 of 3. Title "Create your profile". Body "Your profile stays on this device. No account, no email, no server."
- Name field: required, trimmed, 1-40 characters, letters, digits, spaces, emoji; inline error "Enter a name". Photo: optional circular preview with "Choose photo" and "Remove photo". Use the system photo picker (ActivityResultContracts.PickVisualMedia): NO storage permission. Downscale to at most 512 px and re-encode (JPEG or WebP) so EXIF and GPS are stripped.
- Buttons: "Continue" (disabled until the name is valid), link "Restore from a backup" (goes to N3).
- Flow: Splash -> N1 -> N2 (storage) -> N4 (app lock setup) -> "Creating your encrypted space..." progress -> Home. CreateAccount already takes name, avatarPath and iconGradientValue; call it with NetworkMode.LOCAL.

**N2 Storage location** (first launch step 2 of 3, and Settings -> Storage -> Change location).
- Title "Where should Aparok keep your data?" Radio list, each row shows free / total space and a speed/health result from ProbeStorage:
  1. "This phone" (internal storage, recommended, default).
  2. "Phone storage, app folder" (getExternalFilesDirs()[0] when it is different from internal).
  3. "SD card / USB drive" (every other getExternalFilesDirs() entry that is mounted; show the volume label from StorageManager). Hidden when none is mounted.
- Rules: NO MANAGE_EXTERNAL_STORAGE, NO SAF for live data (the core needs real file paths). Run ProbeStorage (write, fsync, atomic rename, file lock, mmap, free space) before accepting; if it fails show "This storage can't be used for the database: <reason>" and block it. Warnings on non-internal choices: "SD cards can be slower. If you remove the card, Aparok locks until you put it back." and "Uninstalling Aparok deletes data stored in its app folders. Make a backup first."
- Button "Use this location". Settings variant adds a safe MOVE: space check (size + 20%), stop the core, copy, verify the SHA-256 manifest, atomic switch of the stored pointer, secure-wipe the old copy, progress bar with percent, Cancel (before the switch only), resumable after a crash, requires unlock. The pointer lives in internal storage (read by DefaultInitialParamsProvider, which today hardcodes workDir = context.filesDir).

**N3 Restore from backup** (first launch link on N1, and Settings -> Backup & restore -> Restore).
- Step A: "Choose your backup file" (SAF ACTION_OPEN_DOCUMENT; accept files ending in .aparok; show name, size, date). Step B: "Enter the backup password". Step C: choose storage (N2). Step D: progress with named stages and percent: Decrypting, Verifying, Restoring files, Opening.
- Errors (exact): "Wrong password or damaged file." (never say which); "This backup was made by a newer version of Aparok."; "Not enough space on the chosen storage (needs X, has Y)." Nothing is changed on failure.
- Settings variant warns "This replaces everything on this device. Your current data is kept until the restored data is verified." and requires typing RESTORE to continue. On success: re-wrap the key under this device's Keystore, ask for app lock setup (N4), open Home.

**N4 App lock** (setup during first launch and in Settings -> Security; unlock screen at every launch and after auto-lock).
- Setup: "Protect Aparok" with PIN (6-12 digits) or passphrase; optional biometric toggle (BiometricPrompt with CryptoObject); "Skip" shows a warning "Without a lock, anyone who opens your unlocked phone can read your data." Auto-lock: Immediately / 30 seconds / 1 minute / 5 minutes (default 1 minute) and "Lock when the app goes to the background" (default on).
- Unlock screen: PIN or passphrase entry and a biometric button; after 5 wrong attempts back off 30 s, 1 min, 5 min, 15 min. "Forgot PIN?" -> "There is no recovery. You can erase this device's data and restore from a backup." with a typed ERASE confirmation.
- Key release: the storage key is unwrapped only after unlock; on lock the core's Lock() zeroizes it and the UI shows the unlock screen.

**N5 Settings -> Backup & restore -> Create backup**
- Info: "A backup contains everything: chats, notes, images, videos, files and settings. It is encrypted with the password you choose." Fields: password and confirm with a strength meter (minimum 10 characters or a 4-word passphrase), checkbox "I understand the password can't be recovered". Destination: SAF ACTION_CREATE_DOCUMENT with suggested name aparok-backup-YYYYMMDD-HHmm.aparok.
- Progress with percent and Cancel. After writing, verify automatically by decrypting and checking every hash; show "Backup verified" with size, date and the first 8 characters of its SHA-256. Show "Last backup: <date>" in the screen and, if older than the chosen reminder (Off / 7 / 14 / 30 days), an in-app banner on Home "Last backup was N days ago. Back up now". NO notifications, NO scheduled background backups (they would need a stored password), NO intent-filter for .aparok files.

**N6 Settings -> Storage** (replaces FilesStorage and SpacesStorage quota screens). Shows the current location, a usage breakdown in MB (Chats and notes, Images, Videos, Files, Search index, Cache), buttons "Change location" (N2), "Clear cache", "Export log" (SAF, the local crash log), no quotas, no "upgrade".

**N7 Storage unavailable** (shown by Splash when the chosen volume is not mounted). Title "Storage not available". Body "Aparok's data is on <volume name>, which isn't connected. Reconnect it and tap Retry. Your data is safe; nothing was changed." Buttons "Retry" and "Restore from a backup instead". NEVER create an empty profile in another location.

**N8 About and legal** (replaces AboutAppFragment/AboutAppScreen content): name, version, build commit, release certificate SHA-256 fingerprint, "Free and non-commercial. Based on Anytype by Any Association, licensed under the Any Source Available License 1.0." with the license text bundled locally (no web link), open-source notices list (bundled), privacy statement "Aparok has no accounts, no analytics and no network features." Links open nothing on the web.

**N9 Security settings**: App lock (N4), Auto-lock, Block screenshots and hide the recents preview (FLAG_SECURE, default ON), Clipboard auto-clear after copy (default 30 s; mark copies sensitive), Erase all data on this device (typed ERASE).

**N10 Status indicator**: wherever the old sync status icons were (editor header, chat header, space header, vault cards) show a static "Saved on this device" (lock icon). No spinners, no "offline", no "syncing", no "not synced".

### 5.2 KEEP and verify offline (these must pass the R12 emulator test)
ChatFragment, ChatReactionFragment, SelectChatIconFragment, SelectChatReactionFragment, CreateChatObjectFragment, DiscussionFragment (check what it is; keep if it is local), EditorFragment and its modals (CreateObjectFragment, EditorModalFragment, SetBlockTextValueFragment, TextBlockIconPickerFragment, ObjectIconPickerFragment, ObjectMenuFragment minus web actions), FullScreenPictureFragment, MediaActivity (image zoom/swipe, video with seeking, audio), AllContentFragment, GlobalSearchFragment, SearchV2Fragment, ObjectSetFragment and all sets/* screens (filters, sorts, view settings, create-record), all relations/* screens, all primitives/* (types, properties), all templates/*, all widgets/* and WidgetsScreenFragment, WidgetOverlayFragment, DateObjectFragment, linking/* (LinkToObject..., BacklinkOrAddToObject), MoveToFragment, VersionHistoryFragment (local history), quickcapture/*, VaultFragment and its cards (feature-vault), SpaceListFragment, CreateSpaceFragment, SpaceSettingsFragment (remove members/sharing sections), ManageSectionsFragment, AppearanceFragment, PreferenceFragment (remove network sections), ProfileSettingsFragment (name and photo only), ParticipantFragment (own profile only, no invite/permissions), the four OS widget config activities, SettingsActivity, MainActivity.

### 5.3 CHANGE
- SplashFragment: route no profile -> N1; storage missing -> N7; locked -> N4 unlock; otherwise Home. No network checks, no update check, no deep-link web hosts.
- OnboardingFragment: becomes the 3-step flow N1 -> N2 -> N4.
- ProfileSettingsFragment and ProfileScreen: name and photo editing; remove membership row, identity link, recovery phrase, delete account, logout, network section, "backup node".
- AboutAppFragment: N8. FilesStorageFragment and SpacesStorageFragment: N6. NotificationSettingsScreen and NotificationsFragment: remove (no push; local notifications are not used).
- CreateBookmarkFragment, SetObjectCreateBookmarkRecordFragment, bookmark block: offline = store the URL, show host/URL as title, no fetch, no preview spinner (core/block/bookmark and util/linkpreview must not hit the web).
- SelectCoverGalleryFragment: covers from bundled gradients/colors and a picked local image only.
- ObjectMenu: remove Publish, Share, Copy link, Open in web, Invite.
- PreferenceFragment: remove network mode, custom network config, sync and storage-quota rows; add Security (N9) and Backup & restore entries.

### 5.4 REMOVE (class, view model, DI, strings, layouts, navigation, tests)
DeletedAccountFragment, BaseMnemonicFragment and every recovery-phrase / keychain UI, LogoutWarningFragment, HowToShareSpaceFragment, ShareSpaceFragment and the whole multiplayer/invite flow (view models, use cases, links, deep links), PublishToWebFragment, MySitesFragment, NotificationsFragment, QrScannerActivity (QR invites), UnsplashBaseFragment and all Unsplash code, RemoteFilesManageFragment/Screen, DebugFragment, DebugSettingsFragment, ExperimentalFeaturesFragment (keep only a local "Export log" inside N6), every membership screen and badge, every FileLimits/quota UI, every login/sign-in/mnemonic/email/invite-code/network-picker screen left in presentation/onboarding. Re-run the fragment list after removals: no unreachable navigation destinations may remain.

## 6. FEATURE MATRIX (all must work with Airplane mode ON, from a cold start, after process death and reboot; encrypted at rest; included in backups; stored under the chosen storage location)

1 CHAT TEXT: send, edit, delete, reply, reactions, mentions of objects, unread counters, scroll/pagination, in-chat search, several chats, chats per space. A sent message is saved, shown at once and marked sent/saved (never pending, failed or a clock).
2 CHAT ATTACHMENTS: images and videos from the system photo picker AND the camera (take photo/video), any file from the system file picker (documents, PDF, audio, archives), objects as links, several attachments per message. Upload progress finishes locally; no "limited", "queued" or "not synced" state. Links offline: store the URL, show host/URL as the title, no fetch.
3 VIEWING MEDIA: full-screen image viewer (zoom, swipe), video playback WITH seeking, audio playback, thumbnails and previews in chat and editor, open/share a file to another app on user action (FileProvider), save to the device via SAF.
4 EDITOR: every block type (text, lists, toggles, callouts, code, tables, dividers); image, video, audio, file and PDF blocks (picker, camera, paste, drag-drop, Android share-in); bookmarks; links between objects, relations, types, templates, sets/collections (views, filters, sorts, queries), covers and icons from local images, archive/trash/restore, undo/redo.
5 SEARCH: global and in-set search including full-text, working from a cold start after unlock.
6 IMPORT/EXPORT: import files/markdown, export markdown/pb to a user-chosen local file via SAF.
7 SPACES/WIDGETS: create, rename, delete spaces; home widgets; no invite/join/share UI.
8 ANDROID INTEGRATION: ACTION_SEND and SEND_MULTIPLE of text, images, videos and files into the app (into a chat or a new object); no web link intents; no push.
9 SETTINGS: profile name/photo, theme, language, app lock, security, Storage location, Backup and restore; no network, sync, membership, quota or account-server settings.
10 STATUS UI: no offline/no-connection/syncing banners, spinners, toasts or dialogs anywhere (N10).
11 NO SERVER LIMITS: remove FileLimits, NodeUsageInfo, FileLimitsEvent and their UI and use cases (EditorViewModel.observeFileLimitsEvents, presentation/settings FilesStorageViewModel and SpacesStorageViewModel, middleware FileLimitsMiddlewareChannel). In the core, core/files/filesync has states PendingUpload, Uploading, Limited, Done and returns early under IsLocalOnlyMode: every file must report stored/Done at once.
12 LIFECYCLE: cold start, resume, rotation, low memory, airplane mode toggled at any time.

Anchors: media URLs come from UrlBuilder (domain/.../misc/UrlBuilder.kt; interface core-models/.../UBuilder.kt: large, original, thumbnail, medium, file, video) on top of the gateway base string from data/.../config/GatewayProvider.kt. Go routes /file/ and /image/ in pkg/lib/gateway/gateway.go use ServeContent, so Range/seek works. Put the per-session token in the URL query inside UrlBuilder so image loaders, ExoPlayer and WebView need no headers. Players: core-ui/.../editor/holders/media/Video.kt and app/.../ui/media/screens/MediaScreen.kt. Chat uses UploadFile and CreateObjectFromUrl (feature-chats ChatViewModel). Bookmarks: CreateBookmarkObject, presentation SharingViewModel, ObjectSetCreateBookmarkRecordViewModel and in Go core/block/bookmark and util/linkpreview.

## 7. PHASES (each ends with a "Done when" proof; update docs/STATUS.md)

### R0 VERIFIED STATE + CLEANUP (6%) - ledger Q14 Q15 Q23
Do: write docs/AUDIT_V1_0.md from section 3 (claim, true/false, evidence file:line, fix). Archive aparok-sync-node, -filenode, -coordinator, -consensusnode, -docker, -tools, aparok-block, aparok-crypto-kotlin. Remove upstream workflows that cannot run (cla, mirror-any-block, perftests, self-hosted mac mini). Fix the READMEs (no github.properties/apikeys steps; GitHub private vulnerability reporting as the contact; clear the website field; no "encrypted" or "official" until R7 is PROVEN). Create docs/STATUS.md with every phase marked honestly (R6-R9 NOT STARTED).
Done when: AUDIT and STATUS exist, repo list matches, READMEs are accurate.

### R1 OFFLINE APP, VERIFY AND FINISH (8%) - Q01 Q12 Q13
Do: keep the manifest as is (app/src/main/AndroidManifest.xml, device/src/main/AndroidManifest.xml): final merged permissions INTERNET (only because the on-device middleware serves files over loopback; re-test in R6) and CAMERA (runtime only). Make scripts/verify-offline.sh strict: run AFTER the build, FAIL if the merged manifest is missing or any permission besides INTERNET and CAMERA appears (also check every service, receiver and provider in the merged manifest), and grep for firebase, sentry, amplitude, billing, google-services, c2dm in gradle files, version catalog and sources. Remove dead code left by earlier removals (analytics/crash interfaces with no users, unused DI modules, unused onboarding strings and view models). Rename middleware coordinates to io.aparok:middleware.
Done when: CI prints the merged-manifest permission list, the gate fails when a forbidden permission is added in a test commit, and a clean checkout builds.

### R2 LOCAL PROFILE AND FIRST-LAUNCH FLOW (6%) - Q03
Do: persistence/.../networkmode/NetworkModeProvider.kt must always return LOCAL (set() no-op, no preference). Build screens N1, N2 (first-launch part), N4 (setup) per section 5.1 and the Splash routing in 5.3; remove every leftover login, mnemonic, email, invite-code and network-picker class, string and test (OnboardingNetworkSetupDialog, signin/signup screens, OnboardingMnemonicViewModel, OnboardingEmailAndSelectionViewModel, BaseMnemonicFragment). Settings -> Profile edits name and photo. Add "Restore from a backup" to N1 once R9 exists.
Done when: an instrumented test goes cold start -> N1 -> N2 -> N4 -> Home, relaunch goes to unlock/Home, photo is <=512 px with EXIF/GPS stripped.

### R3 CHAT, MEDIA, FILES AND EVERYTHING ELSE OFFLINE (14%) - Q02 Q04 Q05 Q06
Root cause first: feature-chats ChatViewModel sets ChatBoxMode.ReadOnly when spacePermissionProvider.observe(space) is READER or NO_PERMISSIONS; domain/src/main/java/io/aparok/app/domain/multiplayer/DefaultUserPermissionProvider.kt derives permission from a PARTICIPANT object that can be missing offline. Single-user app: get() and observe() ALWAYS return SpaceMemberPermissions.OWNER, all() maps every space view's targetSpaceId to OWNER (keep getCurrent()). Then review every other permission and sync-status check (canEdit, isOwnerOrEditor, sync status icons) so nothing blocks or waits.
Implement the whole FEATURE MATRIX (section 6) and screens N10, the bookmark change, the cover gallery change and the ObjectMenu cleanup (5.3). Remove FileLimits and quota code (matrix 11). Core: core/block/chats (+ chatrepository, chatsubscription), file objects and the gateway must work with no network and no peers; fix the local-only image bug in pkg/lib/gateway (serve from the local file store, bounded retries with backoff, bind 127.0.0.1, require the per-session token); make bookmark creation offline-safe; files report Done at once.
Tests: unit (permission provider, chat box mode, UrlBuilder token), Go (chat add/read with network components stubbed; file add/read through the gateway with no network interface; Range request returns 206).
Done when: unit and Go tests are green in CI and the R12 test covers every item of the matrix.

### R4 REAL MEMBERSHIP REMOVAL (6%) - Q07
Do: delete every remaining membership reference (68 files: Membership* in presentation, domain, middleware, data, core-models; ProfileScreen and ProfileSettingsViewModel showMembershipState; SpacesStorage upgrade prompts; ShareSpace limits; membership deep link; Play Billing; "Co-Creator" strings, badges, drawables; the MembershipProvider itself). No tier concept: where a limit was read use "unlimited". Delete playBilling from gradle/libs.versions.toml. Core side in R6.
Done when: grep for membership, tier, billing, paywall, upgrade in sources, strings and gradle finds nothing but the license text; CI green.

### R5 NO INVITES, SHARING, UPDATE CHECKS, EXTRAS; LOGICAL REBRAND (6%) - Q08 Q14
Do: remove section 5.4 screens and all code behind them: invite (85 files), ShareSpace (23), publish (45), Unsplash (28), gallery (76), the in-app update check, QR scanner, any code that opens a web URL on its own. http(s) literals may remain only in docs and credits and are never fetched (remove hi.any.coop, object.any.coop and the 37 GitHub releases/latest literals). Rebrand per section 4: rewrite every user-visible string in all locales (localization/src/main/res/values*/strings.xml), About (N8), onboarding, notification channels, widgets, shortcuts so nothing describes a removed feature; decide the 79 Kotlin files that still say "Anytype" case by case; replace Anytype-named drawables and launcher assets with the Aparok mark; set the accent color token.
Done when: a script lists zero "Anytype", "any.coop", "anytype.io" outside LICENSE.md, NOTICE.md and docs; navigation graph has no dead destinations; CI green.

### R6 OWN CORE (aparok-core) (8%) - Q01 Q15 Q16 Q12
Build: Makefile -> makefiles/android.mk (gomobile bind -tags "$(TAGS)" -target=android -androidapi 26 -o lib.aar .../clientlibrary/service .../core). makefiles/ci-compile-android-lib.mk ends with "|| true": remove it so failures fail the build. Tags: nogrpcserver gomobile nowatchdog nosigar nomutexdeadlockdetector timetzdata rasterizesvg; run make download-tantivy-all-force; add -javapkg io.aparok.mw, -target=android/arm64,android/arm, -trimpath, -ldflags "-s -w"; add a debug-only x86_64 build for the emulator test.
Components are registered in core/anytype/bootstrap.go (about lines 230-395). Config.IsLocalOnlyMode() (core/anytype/config/config.go) already gates indexer/reindex.go, core/files/filesync/*, core/files/fileobject/service.go, core/workspace.go, core/pushnotification/service.go, and nodeconf is empty in local-only. Make that the ONLY mode and replace with in-process no-ops everything that connects or talks to servers: nodeconf sources/store, coordinatorclient, nodeclient, yamux, quic, quicdemotion, clientserver, pool, peerservice, peermanager, localdiscovery (+ clientlibrary/service/discovery.go), rpcstore/filestorage sync, paymentserviceclient and paymentserviceclient2, paymentscache, payments, nameservice (+client), emailcollector, pushclient, pushnotification, subscribeclient, anysyncinboxclient, inboxclient, inboxservice, publish, publishclient, linkpreview, unsplash, gallery, core/api, metric, debugserver, profiler, remote identity lookups. Drop prometheus, jaeger, opengraph, zeroconf dependencies. Remove membership/payments from the core (the protobuf RPCs may stay as stubs only if the generated API requires it; no behavior).
scripts/verify-offline.sh with a NON-EMPTY banned list (go list -deps of clientlibrary/service and core; outbound HTTP/dial call sites) runs in CI. CI builds on push and publishes lib.aar, Java protos and SHA-256 as public Release assets; the app downloads that pinned asset (hash in middleware.lock) instead of the stock v0.51.6 and copies the matching protos into the protocol module. Remove the upstream workflows. Then try removing INTERNET: if the gateway breaks, keep it and document the evidence in docs/DECISIONS.md.
Done when: workflow run URL, commit SHA, Go version, tags, `unzip -l` of lib.aar and verify-offline output are in STATUS.md, and `git diff --stat` against upstream shows the real changes.

### R7 ENCRYPT EVERYTHING (14%) - Q09 Q13
Facts: go.mod uses BOTH any-store v1.0.3 (space/spacecore/storage/anystorage, space/clientspace, spacecore, any-sync) and any-store/v2 v2.5.0 (util/keyvaluestore, persistentqueue, typeprovider, identity, reindex, filesync, spacecore/storage/migrator). v2 has Config.Encryption {Passphrase | Codec} (AES-256-GCM, ChaCha20-Poly1305, XChaCha20-Poly1305); plain DBs stay plain (migration needed); v1 has none. Full-text uses tantivy-go (plaintext index). Data root is Config.RepoPath.
Key: a random 32-byte storage key created at profile creation, wrapped by an Android Keystore key, passed to the core by a new gomobile-exported function taking bytes (no proto change); never persisted by the core; HKDF subkeys; zeroize on Lock(); fail closed without the key.
Encrypt: (1) every v2 database via Config.Encryption with a custom Codec; (2) the v1 space storage by upgrading anystorage to v2 or by patching aparok-store/aparok-sync (go mod edit -replace in aparok-core; keep module paths equal to the replaced ones; include transitive deps); (3) file blocks with an encrypting wrapper (per-block XChaCha20-Poly1305, random nonce, AAD = block id) so chat images, videos, files and editor media are covered; (4) the tantivy index in a RAM directory rebuilt after unlock, or an encrypted alternative; (5) caches, thumbnails, temp files, logs, WAL/shm/lock files. Migration: copy plain data into new encrypted databases, verify counts and checksums, atomic swap, secure-delete the plaintext, crash-safe and resumable.
App: Keystore master key (AES-256-GCM, StrongBox when available) with Tink (NOT the deprecated androidx security-crypto); app lock N4; Room -> SQLCipher; DataStore and SharedPreferences -> Tink; every non-core file (picked media, camera temp, share temp, thumbnails, crash logs, exports) with Tink StreamingAead inside noBackupFilesDir, wiped on lock; allowBackup=false and empty dataExtractionRules; FLAG_SECURE default ON; hide the recents preview; no exported component except the launcher activity; narrow short-lived FileProvider grants; R8 with Log.* stripped; sensitive clipboard flag and auto-clear; no autofill or keyboard learning on sensitive fields. Screens N4 and N9.
Tests: write the marker APAROK_MARKER through every layer (chat text, chat attachment bytes, editor text, image/video/file bytes, relation values), then scan every byte of the whole data directory (raw, UTF-16, base64): it must not be found; a wrong key fails closed; database integrity check passes. docs/ENCRYPTION.md with a protection table and honest residual risks (RAM, GC, metadata such as sizes and timestamps).
Done when: the marker test and the wrong-key test are green in CI.

### R8 STORAGE LOCATION (7%) - Q11
Do: core: the data directory is a parameter and EVERYTHING lives under it (nothing under hardcoded or home paths); add ProbeStorage(dir). App: DefaultInitialParamsProvider.kt reads the pointer file from internal storage instead of context.filesDir; build N2 (both variants), N6, N7 and the volume events: on unmount/eject/bad removal close the core cleanly (flush, WAL checkpoint, lock) and show N7; at launch with a missing volume show N7 and NEVER create an empty profile elsewhere.
Tests: probe pass/fail, move with SHA-256 verification, crash in the middle of a move resumes, missing volume handling.
Done when: the tests are green and the R12 test sets a non-default storage location where one exists on the emulator.

### R9 BACKUP AND RESTORE .aparok (9%) - Q10
Core: Backup, VerifyBackup and Restore, each with progress and cancel. Quiesce (flush, WAL checkpoint, stop writers), archive the on-disk (already encrypted) files plus the storage key; the key travels ONLY inside the password envelope; restore re-wraps it under the new device's Keystore key. Format: header (magic APAROK, format version, KDF parameters) + zstd tar + manifest.json (format versions, per-file SHA-256, created-at) + the key, encrypted with a vetted streaming AEAD: filippo.io/age with a scrypt recipient (about 64 MiB) OR Argon2id (>= 64 MiB, t >= 3) with STREAM chunked XChaCha20-Poly1305 and an authenticated header and final-chunk flag. No other hand-rolled cryptography. Restore: decrypt into a staging directory on the chosen storage, validate hashes, reject path traversal, symlinks and oversized entries, integrity-check the databases, open-check, atomic swap, roll back on failure; wrong password or corruption = a clear error and nothing partial; refuse newer formats.
App: screens N5 and N3/N6 variants per section 5.1; "Restore from a backup" on N1; SAF only; no intent-filter; no scheduled backups; in-app reminder banner.
Tests: round trip with chats, images, videos and files; wrong password; bit flip; truncation; path traversal archive; version mismatch; huge file. docs/BACKUP_FORMAT.md with the exact spec.
Done when: all tests are green in CI and the R12 test does a backup and a restore on the emulator.

### R10 SIZE (5%) - Q17
Targets: arm64 APK < 80 MB, armeabi-v7a APK < 75 MB, NO universal APK. Measure first (apkanalyzer or unzip -l, largest entries). Then: AAR ABIs arm64-v8a and armeabi-v7a only (x86_64 only in the debug emulator build), Go -ldflags "-s -w" and -trimpath, tantivy libs only for shipped ABIs, delete unused res/raw, assets, videos, lottie, onboarding media, the sample code, unused libraries and unused locale assets, PNG -> WebP, R8 full mode with shrinkResources, no debug-only libraries in release. docs/SIZE.md with before and after numbers.
Done when: the release APK sizes meet the targets or the file explains exactly why not.

### R11 SIGNING AND RELEASE SYSTEM (4%) - Q18 Q19
Do: scripts/release/ensure-signing.sh (idempotent): if the signing secrets are missing, generate a keystore (RSA 4096, 10000 days, subject CN=Aparok) with RANDOM 32+ character passwords, store them as GitHub secrets (KEYSTORE_BASE64, KEYSTORE_PASSWORD, KEY_ALIAS, KEY_PASSWORD), copy the keystore and passwords to a PRIVATE recovery repo, commit release/CERT_SHA256; never print secrets. The release workflow (on push to main and workflow_dispatch) FAILS when the keystore secret is missing and never generates a new key silently. Gates before publishing: apksigner verify --print-certs (v2 and v3); fingerprint equals release/CERT_SHA256; zipalign check; merged-manifest permissions within the allowed list; no banned strings; versionCode strictly increasing from the run number; size report. Then per-ABI APKs, SHA256SUMS.txt, build provenance attestation, tag v<versionName>-<run>, GitHub Release with notes from commits, the fingerprint and verification instructions. Delete the hardcoded password, scripts/release/app-release.gpg and the upstream encrypt/decrypt-secrets scripts. docs/RELEASE_SYSTEM.md: recovery from the private repo and the key-loss risk.
Done when: a release run URL shows every gate passing and the release page lists the APKs and checksums.

### R12 OFFLINE END-TO-END TEST AND FINAL AUDIT (7%) - Q02 Q06 Q20
CI job on an Android emulator (reactivecircus/android-emulator-runner, x86_64 debug build) with Airplane mode ON (adb shell cmd connectivity airplane-mode enable) running a scripted UI Automator/Espresso test: create profile (N1, N2, N4); send text, reply, react, edit, delete; attach an image, a video, a PDF, an audio file and a link (picker and share-in); open each (image viewer, video plays and seeks, file opens); kill and relaunch the app, unlock, and verify everything persisted; create an editor object with text, image, video and file blocks; search finds the text; export and import; change the storage location where possible; create a backup, restore it, verify the data. Network proof: after the run read /proc/net/tcp and /proc/net/tcp6 through adb for the app UID and FAIL on any non-loopback address; logcat must show no UnknownHostException or connect timeout. docs/AUDIT.md: every verify script, marker-scan output, green CI links, merged-manifest permissions, leftover http(s) literals, APK sizes, what ran versus what was only inspected, open risks. Final message: 100%, release links, certificate fingerprint.

## 8. DEFINITION OF DONE
Every ledger row Q01-Q23 is PROVEN in docs/STATUS.md with a link; the R12 test is green; the release exists with signed per-ABI APKs and checksums; aparok-core's lib.aar is built by CI from the fork and differs from upstream in the ways listed in R6; READMEs are truthful; unused repositories are archived.
