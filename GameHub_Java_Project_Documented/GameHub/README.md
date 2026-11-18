# GameHub — Smart Gaming Controller Management System

Java console application for OOP Assignment 2. Extends the GamingController
from Assignment 1, retaining its seven attributes and original methods.
No dependencies or physical gaming hardware required. Data is in memory only.
Battery usage, prices and controller features are illustrative simulation data.

## Project contents

- src/GamingController.java — concrete parent class, default and parameterized constructors, seven private fields, setters/getters, common operations.
- src/Playable.java — common interface, gameplay and battery usage signatures.
- src/PlayStationController.java — child with adaptive triggers, haptics and touchpad.
- src/XboxController.java — child with vibration, custom mapping and impulse triggers.
- src/Player.java — profile and total gaming minutes.
- src/GamingSession.java — checks power/battery, runs gameplay and updates statistics.
- src/ControllerManager.java — list, selection and comparison of controllers.
- src/Main.java — entry point, interactive menu and input validation.
- tests/GameHubTest.java — automated tests without external libraries.
- tests/test-results.txt — actual test output.
- tests/demo-input.txt and demo-output.txt — recorded end-to-end example.

## Run from a terminal

Install a JDK (Java 8 or later). Open a terminal in the extracted GameHub folder.

    mkdir out
    javac -d out src/*.java
    java -cp out Main

In an IDE (NetBeans, IntelliJ IDEA, Eclipse or VS Code), create/open a Java
project using `src` as the source folder, then run `Main.java`. All classes
are in the default package. Do not add a package to only one file.

## Run automated tests (from the GameHub folder)

    javac -d out src/*.java tests/*.java
    java -cp out GameHubTest

Tested using OpenJDK 21, compiling with `--release 8`; 27 checks passed.
A recorded command-line demonstration is also included.

## Quick demonstration for Q&A

1. Select 1 to view the three controllers, including the instantiated parent.
2. Select 2 and enter a player name and favourite game.
3. Select 4 to switch on the default DualSense controller.
4. Select 6, enter a game and 60 minutes. Default battery usage is 15%.
5. Select 3, then 2 for Xbox. Select 4 to power it on.
6. Select 6 again, enter a game and 60 minutes. Default battery usage is 11%.
7. Select 9 for session history and the player's cumulative gaming time.
8. Select 10 to compare controllers, including different hourly estimates.
9. Use 5 to change controller settings, 7 to check battery, 8 to recharge,
   11 to select a registered player, and 0 to exit.

## Rubric mapping

- Encapsulation: private instance fields; validated setters; immutable fields
  where appropriate; ControllerManager returns a defensive copy of its list.
- Inheritance: both specialized controller classes extend GamingController
  and call its constructor using super(...).
- Polymorphism: Playable references in GamingSession and ControllerManager
  dispatch to different overridden methods at runtime. The generic parent
  also implements these methods and can be instantiated.
- Abstraction: Playable specifies the operations without exposing their logic.
- Additional classes: Player, GamingSession, ControllerManager interact in Main.
- Control structures: switch for menu choices, while for repeated menu and
  validation, for loops for collections, if/else for power, battery and settings.
- Documentation: all eight application source files have expanded JavaDoc for classes,
  fields, explicit constructors and methods (including parameter, return and exception
  tags where applicable). Inline comments explain control structures and design decisions.

Changes from Assignment 1: invalid values now throw an exception rather than
being silently ignored; the menu handles these exceptions. An empty battery
forces the controller off. The three additional classes and interface are new.

## Documentation update

This edition expands source comments only; program logic is unchanged. The original
27 automated checks and the recorded menu demonstration have been rerun.

For Windows PowerShell, if your terminal starts in the outer extracted ZIP folder,
use `cd GameHub/src`, then `javac *.java` and `java Main`. If you are already in
GameHub, use `cd src` instead. Enter menu input in the Terminal, not Output.
