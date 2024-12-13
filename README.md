# Members
- (MD) A393B4KY2290 – Lucky Alamsyah – Universitas Serang Raya
- (MD) A528B4KX0163 – Afrianti Fadillah –  Politeknik Negeri Medan

## Overview
Monisick is a mobile application designed to support medication adherence and health monitoring. The app provides features such as medication reminders, nutritional analysis, and condition journaling. It aims to assist patients and caregivers in maintaining health during a medication period, offering useful data for healthcare professionals to evaluate the effectiveness of treatments.

## Features
- Medication Scheduling and Reminders.
- Health Monitoring.
- Scan Nutrition Food.
- Daily Notes.

## Tech Stack
### Tools & IDEs
- Android Studio: IDE for development.
- Gradle: Dependency management and build process.
- Figma: UI/UX design.
- Postman: API testing.
- Git: Version control.

### Plugins & Libraries
- Kotlin Symbol Processing (KSP): For Room database and other libraries.
- Hilt (Dagger): Dependency injection.

### Dependencies
#### UI Components
- Material Components: For Material Design UI.

#### Navigation
- Navigation Component: For fragment transitions.

#### Data Management
- Room Database: Local data storage.
- DataStore: Simple user preferences storage.

#### Networking
- Retrofit: REST API consumption.
- OkHttp & Interceptor: HTTP client configuration.

#### Asynchronous Operations
- Kotlin Coroutines: Asynchronous thread management.

### Platforms & Services
- WorkManager: For local notifications and medication reminders.

## Installation
1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/monisick.git
   ```
2. Open the project in Android Studio.
3. Build the project using Gradle.
4. Run the application on an Android device or emulator.

## Project Scope & Deliverables
- Analysis & Planning: Conducted by the entire mobile development team.
- UI/UX Design & Development: Designed by Lucky Alamsyah.
- Key Feature Development: Implemented by all team members.
- API Integration: Handled by all team members.
- Reminder & Monitoring Features: Built by all team members.
- Testing and Refinement: Overseen by Afrianti Fadillah.
- Documentation & Release Preparation: Prepared by all team members.

## Limitations & Changes
- Comprehensive testing and detailed documentation were not fully implemented due to time constraints.
- Notification scheduling was adjusted to trigger every 6 hours after medication entry instead of fixed times.
- Graphical monitoring visuals were replaced with placeholder images.
- API integrations for Google Calendar, OpenWeather, and OpenFDA were excluded.
- The final application design was aligned with the modified Figma prototype.

## Contribution Guidelines
1. Fork the repository.
2. Create a new branch for your feature/bug fix:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Description of changes"
   ```
4. Push to the branch:
   ```bash
   git push origin feature-name
   ```
5. Open a pull request.

## License
This project is licensed under the MIT License. See the LICENSE file for details.
