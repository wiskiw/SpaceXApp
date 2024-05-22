
# SpaceX App

This is an Android project called SpaceX App, developed as a test assignment. The project follows the MVI (Model-View-Intent) pattern and Clean Architecture approach. Apollo is used for networking to fetch data from a GraphQL API.

## Features

- **MVI Architecture**: Implements Model-View-Intent pattern for better state management and UI updates.
- **Clean Architecture**: Ensures a scalable and maintainable codebase.
- **Apollo GraphQL**: Utilizes Apollo for networking to interact with GraphQL API.
- **Jetpack Compose**: Uses Jetpack Compose for modern, declarative UI development.
- **Dependency Injection**: Implements Koin for dependency injection.
- **Image Loading**: Utilizes Coil for efficient image loading.
- **Testing**: Comprehensive testing setup with various testing libraries.

## Libraries and Tools

### Image Loading
- Coil

### Navigation and Lifecycle
- Jetpack Compose Navigation
- Jetpack Compose Lifecycle

### Dependency Injection
- Koin

### Networking
- Apollo

### Testing
- JUnit
- KotlinX Coroutines Test
- Google Truth
- MockK
- Robolectric
- Turbine

## Network

Apollo is used for networking to fetch data from the following GraphQL endpoint:
`https://apollo-fullstack-tutorial.herokuapp.com/graphql`

## Architecture

The project follows the MVI and Clean Architecture principles:

- **Presentation Layer**: Contains UI components and ViewModels.
- **Domain Layer**: Contains use cases and business logic.
- **Data Layer**: Contains repositories, data sources, and models.

## Getting Started

### Prerequisites

- Android Studio
- Gradle

### Installation

1. Clone the repository:
   ```sh
   git clone git@github.com:wiskiw/SpaceXApp.git
