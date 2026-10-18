# Android POC Lab

A collection of small, runnable Android proof-of-concepts (POCs). The app's home screen uses a `RecyclerView` as a launcher, with each list item opening a focused experiment or feature demo.

This repository is intended for learning, testing, and keeping Android examples in one place. Individual POCs may be experimental and should not automatically be treated as production-ready implementations.

## POCs and examples

| Area | Examples |
|---|---|
| Lists and UI | RecyclerView, Grid RecyclerView, reusable list adapters |
| Networking | Retrofit, OkHttp, HTTP/API requests, API-driven banners |
| App communication | Fragments, data passing, callbacks, click listeners |
| Architecture and data | MVC, MVVM, ViewModel/repository examples, Room database with API data |
| Android platform | Notifications, runtime permissions, login/signup flow |
| Dynamic UI | Custom fonts, downloaded fonts, dynamic buttons, floating action buttons |
| Camera and media | Camera aspect-ratio experiments, image capture ratios, CameraX experiments |
| Modern UI | Jetpack Compose sample |
| Animation and rendering | Flip-book/page-flip experiment |
| Core Java practice | OOP, callbacks, services, exceptions, and threading examples |

## Tech stack

- Java and Kotlin
- Android SDK and AndroidX
- RecyclerView and Material Components
- Retrofit and OkHttp
- Room
- Camera2 / CameraX
- Jetpack Compose
- Gradle with Kotlin DSL

## Project structure

```text
Android_POC/
├── app/          # Main Android app and RecyclerView POC launcher
├── lib/          # Java/Kotlin library and language experiments
├── CLI/          # Java OOP and threading examples
├── FoodDelivery/ # Java service/model/payment/exception example
├── gradle/       # Gradle wrapper and dependency version catalog
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
└── README.md
```

## Run the Android app

1. Install Android Studio and a compatible Android SDK. This project uses compile SDK 36 and Android Gradle Plugin 8.13.x; JDK 17 is recommended for running Gradle.
2. Clone this repository, or open the existing project in Android Studio.
3. Open the project root — the directory containing `settings.gradle.kts` and `gradlew` — and allow Gradle sync to finish.
4. Select the `app` run configuration and launch it on an emulator or Android device.

From a terminal at the project root, you can also build the debug APK with:

```bash
./gradlew :app:assembleDebug
```

On Windows, use `gradlew.bat :app:assembleDebug`.

## Add another POC

1. Create the new Activity or screen and its layout/resources.
2. Register the Activity in `app/src/main/AndroidManifest.xml` when required.
3. Add a menu entry in `MainActivity#createContactList()` using the existing `AndroidModel` / `BaseAndroidModel` pattern.
4. Run the app and confirm that the new item opens the correct POC.

## Screenshots

Add screenshots of the home screen and selected POCs to a folder such as `docs/screenshots/`, then embed them here using relative paths. Avoid screenshots containing personal information, API keys, or private data.

## Notes

- `local.properties`, SDK paths, build outputs, IDE workspace files, signing keys, and credentials should stay out of version control.
- Before making this repository public, confirm that every source file, image, API endpoint, and other asset is yours to share and does not contain employer/client code or secrets.
