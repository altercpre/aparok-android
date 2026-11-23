# Milestone R2: Local Profile, No Login (100% Sovereign Offline Profile)

## Overview
Phase R2 establishes a single-screen, local-first onboarding flow that eliminates all multi-screen wizards, seed phrase prompts, email collection, and remote network selection, while preserving local account creation, local cryptographic wallet generation, and offline profile editing.

## Verified Implementation Details

### 1. Hardwired Local Network Mode
- **File**: `persistence/src/main/java/com/anytypeio/anytype/persistence/networkmode/NetworkModeProvider.kt`
- `DefaultNetworkModeProvider.get()` unconditionally returns `NetworkModeConfig(NetworkMode.LOCAL)`.
- `DefaultNetworkModeProvider.set()` and `clear()` are hardened no-ops, preventing any transition to remote or custom networks.

### 2. Single Onboarding Screen
- **Files**:
  - `app/src/main/java/com/anytypeio/anytype/ui/onboarding/screens/OnboardingProfileScreen.kt`
  - `app/src/main/java/com/anytypeio/anytype/ui/onboarding/OnboardingFragment.kt`
  - `app/src/main/java/com/anytypeio/anytype/ui/onboarding/ProfileImageHelper.kt`
- Replaced the 6-screen flow (auth / selection / mnemonic / email / recovery / entering-the-void) with a single focused composable screen.
- **Name Input**: Required, auto-focused, single-line, trimmed, max 32 characters, IME Done action.
- **Avatar Picker**: Photo picker with `ProfileImageHelper.processAndSaveProfileImage` downscaling images to <= 512x512, re-encoding to PNG, and stripping EXIF/GPS metadata.
- **Actions**:
  - "Create profile" primary button: enabled only when name is non-blank.
  - "Restore from backup" secondary button: connects to local `.aparok` archive restoration (wired in R9).
  - No mnemonic phrase displayed, generated in UI, or prompted. No email input. No recovery phrase screen. No "entering the void" animation.

### 3. Local Wallet & Account Creation
- **File**: `presentation/src/main/java/com/anytypeio/anytype/presentation/onboarding/OnboardingStartViewModel.kt`
- Invokes `setupWallet` locally on device storage via `PathProvider`.
- Invokes `createAccount` locally with `Command.AccountCreate(name = pendingName, avatarPath = pendingAvatarPath, networkMode = LOCAL)`.
- Upon successful account initialization, updates initial space details, seeds get-started content, and routes immediately to `R.id.actionOpenVault` and `R.id.actionOpenSpaceFromVault`.

### 4. Settings -> Profile Editing
- **Files**:
  - `app/src/main/java/com/anytypeio/anytype/ui/settings/ProfileSettingsFragment.kt`
  - `feature-ui-settings/src/main/java/com/anytypeio/anytype/ui_settings/account/ProfileSettingsViewModel.kt`
- Profile name and photo can be viewed and edited at any time locally through `SetObjectDetails` and `SetDocumentImageIcon`.

### 5. Dead Code & Obsolete Components Removal
- **Deleted UI Screens**:
  - `OnboardingNetworkSetupScreen.kt`
  - `OnboardingEnteringVoidScreen.kt`
  - `OnboardingRecoveryPhraseLoginScreen.kt`
  - `OnboardingMnemonicPhraseScreen.kt`
  - `OnboardingWhatIsRecoveryPhraseScreen.kt`
  - `OnboardingEmailScreen.kt`
  - `OnboardingSelectionScreen.kt`
  - `OnboardingUsecaseScreen.kt`
  - `OnboardingSetProfileNameScreen.kt`
  - `OnboardingAuthScreen.kt`
  - `Previews.kt`
  - `MnemonicPhraseFormatter.kt`
  - `OnboardingNetworkSetupDialog.kt`
  - `OnboardingScreenContract.kt`
  - `Misc.kt`
  - `OnboardingNavigation.kt`
- **Deleted DI Components**:
  - `OnboardingMnemonicDI.kt`
  - `OnboardingSoulCreationDI.kt`
  - `OnboardingMnemonicLoginDI.kt`
  - Removed corresponding factory references and injections from `ComponentManager.kt` and `AppPreferencesDi.kt`.
