# Milestone R4: Real Membership & Paywall Removal

## Overview
Phase R4 achieves genuine, complete removal of all membership, billing, subscriptions, paywalls, and spoofed tiers across both `aparok-android` and `aparok-core`. No simulated paid states, no fake Co-Creator tiers, and no stub billing clients exist.

## Verified Implementation Details

### 1. Module & Dependency Deletion
- **Gradle**: Removed `include ':payments'` from `settings.gradle` and `implementation project(':payments')` from `app/build.gradle`.
- **Directory**: Permanently deleted the entire `payments/` module directory (including all billing lifecycle listeners, Play Store billing clients, payment viewmodels, and screens).

### 2. UI & Navigation Graph
- **Navigation**: Removed `paymentsScreen` (`MembershipFragment`) from `app/src/main/res/navigation/graph.xml`.
- **UI Screens**: Deleted `MembershipFragment.kt`. Removed all deep-link handlers and navigation calls to `paymentsScreen` across `MainActivity.kt`, `WidgetsScreenFragment.kt`, `VaultFragment.kt`, `ShareSpaceFragment.kt`, `SpaceJoinRequestFragment.kt`, `SpacesStorageFragment.kt`, and `SpaceSettingsFragment.kt`.
- **Profile UI**: Stripped `OptionMembership`, `AnyIdInfoSheet`, membership status badges, and `onMembershipClicked` handlers from `ProfileScreen.kt` and `ProfileSettingsFragment.kt`.

### 3. Dependency Injection (DI) Cleanliness
- **DI Modules**: Deleted `MembershipDI.kt` and `MembershipModule.kt`.
- **MainComponent**: Removed `MembershipComponentDependencies`, `MembershipModule`, and its `@Binds` dependency mappings.
- **ComponentManager**: Removed `DaggerMembershipComponent` and `membershipComponent`.
- **WorkspaceModule**: Provided a lightweight no-op `MembershipProvider` emitting empty flow, avoiding any fake or spoofed tier states while cleanly satisfying legacy caller interfaces.
- **ProfileDI**: Removed `MembershipProvider` injection from `provideViewModelFactory`.

### 4. Locale & Strings Sanitization
- Deleted 710 `membership_*`, `subscription_*`, `upgrade_*`, and `tier_*` string definitions across all 16 supported locale files (`values`, `values-uk`, `values-de`, `values-fr`, `values-es`, `values-ru`, `values-ja`, `values-zh`, etc.).
- Replaced dismiss buttons in `EditTypePropertiesFragment`, `ObjectTypeFragment`, `ChatFragment`, and `DateObjectFragment` with standard `android.R.string.ok`.

### 5. Middleware / Core Hygiene
- **aparok-core**: Undid the hardwired spoofed `Tier: 5` (Co-Creator) in `core/payments.go`, eliminating fake billing state responses in the core daemon.
