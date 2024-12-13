## iOS 18 Flashlight Demo

![image](https://github.com/fsfaysalcse/iOS18FlashlightDemo/raw/main/screenshot.png)

This is a replica of the iOS 18 flashlight animation demo, implemented using Jetpack Compose, Kotlin, and Kotlin Multiplatform. The project demonstrates a seamless flashlight animation with features like a flashlight beam and brightness icon, all rendered using Compose Canvas.

### Key Features:
- **Jetpack Compose for UI**: Fully utilizes Compose for modern UI development.
- **Kotlin Multiplatform**: Shares core logic and UI components across Android and iOS platforms.
- **Canvas Magic**: The flashlight beam, brightness icon, and animations are drawn using Compose Canvas.
- **iOS and Android Compatibility**: Common UI and platform-specific entry points for both ecosystems.

Download and check out the codebase! If you like it, please give the repository a ⭐️.

---

### Folder Structure

- `/composeApp`: Shared code for Compose Multiplatform applications.
  - **`commonMain`**: Contains code common to all targets.
  - **Platform-Specific Folders**: For example, `iosMain` is for iOS-specific implementations, such as calls to Apple’s CoreCrypto.

- `/iosApp`: Entry point for the iOS application.
  - Contains iOS-specific logic and code, including SwiftUI integrations if needed.

[Learn more about Kotlin Multiplatform.](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
