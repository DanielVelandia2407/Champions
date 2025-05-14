# Champions League Manager

A comprehensive Java application for managing Champions League data with an interactive GUI. This educational tool allows users to explore and visualize information about Champions League titles won by different teams and track Real Madrid's eliminations from the competition.

## Features

This application implements the following features:

### Champions Titles Management
- **View Teams and Titles**: Browse through teams that have won the Champions League and see details about their titles.
- **Sort Teams**: Sort teams by number of titles (ascending or descending) or alphabetically.
- **Search Teams**: Search for specific teams by name.
- **Add/Edit/Remove**: Manage teams and their Champions League titles.
- **Statistics**: View statistics about Champions League winners.

### Real Madrid Eliminations Tracker
- **View Eliminations**: Browse through Real Madrid's eliminations from the Champions League.
- **Sort Eliminations**: Sort eliminations by season, phase, or opponent.
- **Search Eliminations**: Search for specific eliminations by season, phase, opponent, or description.
- **Add/Edit/Remove**: Manage elimination records with details like results, descriptions, and images.
- **Stack Implementation**: Uses a stack data structure to manage eliminations chronologically.

## Project Structure

The project follows the Model-View-Controller (MVC) architecture pattern:

- **Model**: Contains the data structures and data management implementation.
  - `ChampionsTitlesModel.java`: Manages Champions League titles data.
  - `EliminationsRMModel.java`: Manages Real Madrid eliminations data.
  - Data classes:
    - `ChampionsTeam.java`: Represents a team with Champions League titles.
    - `Title.java`: Represents a Champions League title.
    - `Elimination.java`: Represents a Real Madrid elimination.
  - Data structures:
    - `CircularDoublyLinkedList.java`: Custom implementation for storing teams.

- **View**: Contains the GUI components.
  - `MainView.java`: The main application window.
  - `ChampionsTitlesView.java`: View for managing Champions League titles.
  - `EliminationsRMView.java`: View for managing Real Madrid eliminations.

- **Controller**: Contains the logic that connects the models and views.
  - `MainController.java`: Manages navigation between main views.
  - `ChampionsTitlesController.java`: Controls the Champions titles view.
  - `EliminationsRMController.java`: Controls the Real Madrid eliminations view.

- **Utilities**: Contains helper classes.
  - `FileManager.java`: Handles file operations for loading and saving data.
  - `ImageLoader.java`: Manages loading and displaying images.
  - `SortingUtility.java`: Provides sorting functionality.

## Requirements

- Java Development Kit (JDK) 8 or higher
- Java Swing (included in JDK)
- Gradle (for building the project)

## How to Run

1. Clone or download this repository.
2. Open the project in your preferred Java IDE (Eclipse, IntelliJ IDEA, etc.).
3. Build the project using Gradle.
4. Run the `src/main/java/Main.java` file.

Alternatively, you can run the compiled JAR file (if available) using:

```
java -jar champions.jar
```

## Usage

1. Launch the application.
2. From the main menu, select one of the two main options:
   - **Listado de Champions por Equipo** (Champions List by Team)
   - **Eliminaciones del Real Madrid** (Real Madrid Eliminations)
3. In the Champions List view:
   - Browse through teams and their titles
   - Use the search bar to find specific teams
   - Use the sorting options to organize teams
   - Add, edit, or remove teams and titles
4. In the Real Madrid Eliminations view:
   - Browse through elimination records
   - Use the search bar to find specific eliminations
   - Use the sorting options to organize eliminations
   - Add, edit, or remove elimination records

## Author

Daniel Velandia (Student ID: 20191020140)

## License

This project is for educational purposes. All rights reserved.
