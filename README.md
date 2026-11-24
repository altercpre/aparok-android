# Aparok

Aparok client for Android — private, sovereign, local-first.

## Build project

### Prerequisites

- Android SDK (API 34)
- JDK 17
- Git

### Building

```bash
./gradlew :app:assembleRelease
```

### IDE setup (optional) 

Based on your IDE setup, you might experience problems while accessing/importing auto-generated Kotlin classes from `protocol` module. These classes are currently declared in very large files, which IDE might not process as expected. If this is your case, go to your Android Studio _Help_ section, select _Edit Custom Properties_ option. There you should set `idea.max.intellisense.filesize` property as follows:

```
idea.max.intellisense.filesize=3500
```

## Useful links

[Tech change log](https://github.com/altercpre/aparok-android/blob/main/CHANGELOG.md)

[Aparok Android app dependencies](https://github.com/altercpre/aparok-android/blob/main/gradle/libs.versions.toml)

## Conventions

### PR naming
```
{TASK-ID} {APP AREA} | {NATURE OF CHANGE: Fix, Enhancement, Feature, Design, Documentation} | {CONCISE DESCRIPTION OF WHAT HAS BEEN DONE}
```

App area can be `App`, `Editor`, `Sets`, `Relations`, `Auth`, `Settings`, `Analytics`, `Tech` (CI, DI, scripting, etc.), etc.

Example: *Editor | Fix | Show meaningful message when failed to open file by an existing application*

### Git branch naming

```
{TASK_ID or TASK_NUMBER}-ConciseDescription
```

## Updating aparok-core (middleware)

Prerequisite: `brew install jq`

1. Run the following command in Terminal or Makefile:

    ```
    make update_mw
    ```

2. Make sure your proto files located in `protocol/main/proto/` compile.
3. Make sure to update `middlewareVersion` version in `libs.versions.toml`.

## Contribution
Thank you for your desire to develop Aparok together!

❤️ This project and everyone involved in it is governed by the [Code of Conduct](docs/CODE_OF_CONDUCT.md).

🧑‍💻 Check out our [contributing guide](docs/CONTRIBUTING.md) to learn about asking questions, creating issues, or submitting pull requests.

🫢 For security findings, please submit via [GitHub Private Vulnerability Reporting](https://github.com/altercpre/aparok-android/security/advisories/new) and refer to our [security guide](docs/SECURITY.md) for more information.

🤝 Follow us on [Github](https://github.com/altercpre) and join the [Contributors Community](https://github.com/orgs/altercpre/discussions).

---
Made by Altercpre — privacy-first, security-focused 🔒

Licensed for free, non-commercial use under the [Any Source Available License 1.0](./LICENSE.md). See [NOTICE.md](./NOTICE.md) for upstream attribution and copyright notices.
