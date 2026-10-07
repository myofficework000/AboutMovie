# About Movie

A modern Android movie-discovery app powered by the [OMDb API](https://www.omdbapi.com/). Version 2 is a complete rewrite of the original XML/MVVM sample into a Compose-first, clean-architecture application with MVI state management.

## What’s new

- **100% Jetpack Compose UI** with Material 3, dark cinema-inspired styling, Coil image loading, and Compose Navigation.
- **MVI per feature**: immutable `UiState`, explicit `Intent`s, one-shot navigation/message `Effect`s, and lifecycle-aware `StateFlow` collection.
- **Clean, feature-oriented architecture**: remote/data, domain contracts/models, dependency-injection, and independent `search` and `detail` features.
- **Dagger Hilt** replaces manual Kodein setup and ViewModel factories.
- **Coroutines and Kotlin Flow** replace LiveData/RxJava and keep screen state and persisted favorites reactive.
- **Modern OMDb integration**: typed Retrofit endpoints for paged title searches and full plot/details looked up by IMDb ID.
- **Useful product features**: curated quick-search chips, server-side pagination, retry/error states, persistent favorites using DataStore, and a rich detail screen for cast, crew, ratings, release metadata, and awards.

## Architecture

```text
Compose route → MVI ViewModel → Repository contract → Retrofit / DataStore
                    ↑                  ↓
               StateFlow           domain models
```

Each ViewModel accepts only feature intents, reduces them into a single immutable state flow, and emits transient effects separately. That keeps UI rendering deterministic and configuration-change safe.

## Tech stack

- Kotlin 2.0, Coroutines, Flow
- Jetpack Compose + Material 3 + Navigation Compose
- Dagger Hilt
- Retrofit, OkHttp, Gson, Coil
- DataStore Preferences
- JUnit and Gradle 8 / Android Gradle Plugin 8

## Build

The project defaults to the legacy demo key so it starts immediately. For your own OMDb key, add this to `~/.gradle/gradle.properties` or pass it on the command line:

```properties
OMDB_API_KEY=your_omdb_key
```

Then build the debug app:

```bash
./gradlew testDebugUnitTest assembleDebug
```

The generated APK is at `app/build/outputs/apk/debug/app-debug.apk`.
