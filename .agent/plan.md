# Project Plan

Build a Football Manager UI app (fm2027) with a series of screens: Intro, Loading (animated FAB), Manager Profile Creation, Selection Carousels (Country, League, Team), Home Dashboard (Grid Layout), Career Mode, Transfer Market, Sponsor, Settings, and About. The app should use Material Design 3 and a vibrant color scheme.

## Project Brief

# Project Brief: fm2027

A Football Manager-style Android application designed with a vibrant, energetic aesthetic using Material Design 3. This MVP focuses on providing the core loop of club management and career progression.

## Features

- **Intro & Loading**: 
    - Intro screen displaying "FM 2027".
    - Loading screen with an animated floating action button (Start).
- **Manager Onboarding & Setup**: 
    - Manager profile creation (name, age, avatar).
    - Multi-step selection for country, league, and team using carousel-style sliders.
- **Home Menu**: 
    - TopBar with settings, manager profile info (name, avatar), club logo, and about link.
    - Main menu grid (2x2):
        1. Career Mode (Squad, My Team, Transfer, Training)
        2. Single Mode (Squad, My Team)
        3. Transfer Market (Detailed player attributes and avatars)
        4. Sponsor (Club budget, Bonus)
- **Secondary Screens**:
    - About Screen (Credits, Update, Beta info).
    - Settings Screen (Audio, etc.).

## High-Level Technical Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material Design 3)
- **Architecture**: MVVM (ViewModel, StateFlow)
- **Navigation**: Jetpack Compose Navigation
- **Concurrency**: Kotlin Coroutines
- **Image Loading**: Coil
- **UX Features**: Edge-to-Edge display, Vibrant Material 3 Theme.

## Implementation Steps
**Total Duration:** 30m 15s

### Task_1_Initial_Setup_and_Intro: Initialize the Material 3 theme with a vibrant color scheme, enable Edge-to-Edge, and implement the Intro and animated Loading screens with Compose Navigation.
- **Status:** COMPLETED
- **Updates:** Initialized Material 3 theme with a vibrant color scheme, enabled Edge-to-Edge, and implemented Intro and Loading screens with Compose Navigation. Created custom adaptive icon. Added navigation graph. Intro navigates to Loading, and Loading screen has an animated FAB to proceed.
- **Acceptance Criteria:**
  - Vibrant M3 theme and Edge-to-Edge configured
  - Navigation graph defined
  - Intro screen and Loading screen with animated FAB are functional
- **Duration:** 18m 24s

### Task_2_Onboarding_Flow: Implement the Manager Profile Creation screen and the carousel-based selection screens for Country, League, and Team.
- **Status:** COMPLETED
- **Updates:** Implemented the full Manager Onboarding flow. Created OnboardingViewModel to manage state. Developed Manager Profile screen (name, age, avatar), and Carousel-style selection screens for Country, League, and Team using HorizontalPager with scale/alpha animations. Updated NavGraph and added a placeholder Home screen. maintained M3 vibrant theme and edge-to-edge support.
- **Acceptance Criteria:**
  - Profile creation saves manager data to state
  - Carousel sliders for selection are implemented and interactive
  - Data persists across onboarding steps
- **Duration:** 11m 51s

### Task_3_Home_and_Management: Develop the Home Dashboard grid menu and the core management screens: Career Mode, Transfer Market, and Sponsor.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Home Dashboard grid (2x2) correctly navigates to modules
  - Transfer Market displays player attributes and avatars using Coil
  - Sponsor screen displays club budget details
- **StartTime:** 2026-05-10 15:35:21 ICT

### Task_4_Secondary_Screens_and_Assets: Implement Settings and About screens, design an adaptive app icon, and perform a final stability check.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Settings and About screens are functional
  - Adaptive App Icon is implemented
  - Build passes and app runs without crashes
  - UI adheres to M3 guidelines
  - All existing tests pass

