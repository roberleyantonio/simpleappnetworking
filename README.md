# Simple Application Networking: Clean Architecture & Manual DI

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)
![Compose](https://img.shields.io/badge/Compose-4285F4?logo=jetpackcompose&logoColor=white)

A modern Android application built to demonstrate solid software engineering principles using the **Rick and Morty API**.

This project intentionally avoids third-party Dependency Injection frameworks (like Hilt, Dagger, or Koin) and heavy external libraries to showcase a deep, fundamental understanding of **Manual Dependency Injection**, object lifecycles, and architectural boundaries.

## 🎯 The Goal
The primary objective of this repository is to serve as a portfolio piece demonstrating:
* Mastery of **Clean Architecture** concepts.
* Proficiency in **Manual Dependency Injection**.
* Implementation of a robust **Offline-First (Caching)** strategy without relying on paging libraries.
* Advanced usage of modern Android UI toolkits and reactive state management.

## 🛠 Tech Stack
* **Language:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material 3)
* **Asynchrony:** Kotlin Coroutines & StateFlow
* **Networking:** Retrofit
* **Local Persistence:** Room Database
* **Architecture:** Clean Architecture + MVVM + MVI-style UI State
* **Dependency Injection:** Pure Manual DI (`AppContainer` Pattern)

---

## 🏗 Architectural Highlights & Technical Decisions

### 1. Manual Dependency Injection (`AppContainer`)
Instead of relying on heavy annotations and code generation, this project uses a pure Kotlin `AppContainer` pattern initialized at the Application level.
* **Benefits:** Zero reflection, faster compilation times, a highly readable and explicit dependency graph, and complete control over component scoping (e.g., Application vs. Screen scoped dependencies).
* **Implementation:** Dependencies are lazily constructed in the `DefaultAppContainer` and injected into the UI layer via a custom `ViewModelProvider.Factory`.

### 2. Offline-First Caching Strategy & Custom Pagination
The `CharacterListRepositoryImpl` acts as a true **Single Source of Truth (SSOT)**, handling pagination manually:
1.  **Local Check First:** The repository calculates offsets based on the requested page and queries the local **Room database** first.
2.  **Cache Validation:** It verifies if the requested page is fully cached. *Note: The logic accounts for standard page sizes (20 items) while also handling edge cases like the final page where the item count might be lower.*
3.  **Network Fetch & Sync:** If data is missing or incomplete, it fetches the specific page from the **Retrofit remote API**. The remote data is then immediately saved (mapped to Entities) into the local database.
4.  **Reactive Return:** The database is queried again, mapped to Domain models, and returned to the UI layer.

### 3. Reactive UI & State Management
* **MVI-Style State:** The UI observes a single, immutable `CharactersUiState` data class. This class cleanly encapsulates the data list, distinct loading flags (initial loading vs. paginated loading), and error states.
* **Compose Pagination:** Instead of using the Jetpack Paging library, pagination is handled dynamically in Compose. A custom `snapshotFlow` observes the `LazyListState`, calculating the user's scroll position and triggering new page loads seamlessly before they hit the bottom of the list.

---

## 📂 Project Structure

The project is modularized adhering strictly to Clean Architecture boundaries:

```text
├── app                             # Application level setup, DI entry point, and Navigation
│   ├── navigation
│   └── presentation                
├── core                            # Shared infrastructure and utilities
│   ├── database                    # Room DB, DAOs, Entities
│   ├── network                     # Retrofit configuration
│   ├── shared                      # Common classes (e.g., Exception/Failure handling)
│   │   └── exception
│   ├── ui                          # Core UI components and Theme
│   │   └── theme
│   └── wrapper                     # Utility wrappers
└── feature                         # Feature modules
    └── characterlist               # Rick & Morty characters feature
        ├── data                    # Repository implementations and Data Sources
        │   ├── api                 
        │   ├── local               
        │   ├── mapper              
        │   ├── model               
        │   └── remote              
        ├── di                      # Manual DI wiring for this specific feature
        ├── domain                  # Core business logic and interfaces
        │   └── model                         
        └── presentation            # ViewModels and UI States
            └── components          # Jetpack Compose UI elements