# re;memory

This is a game made for the One Lone Coder Codejam 2023. The theme for the codejam was "Memory."
It was built on  [sola-game-engine](https://github.com/iamdudeman/sola-game-engine) using repo
template [sola-game-template](https://github.com/iamdudeman/sola-game-template)
to start the project.

Play it on [itch.io](https://iamdudeman.itch.io/rememory)!

## Game info

What is this place? What am I doing here? I need to remember...

### Controls

* WASD - move
* space / mouse click - cycle GUI text
* left / right arrows - cycle GUI text

### Gameplay hints

The blue circle asset is a Lapis Lazuli gemstone that will permanently increase one of your stats as you remember skills
you thought you'd lost!
Portals will always take you to a new room so explore a new room if there isn't anything interesting!

### About development

This game was built as a submission for the One Lone Coder Codejam 2023. It is the second real test of a custom
[Java 2D game engine](https://github.com/iamdudeman/sola-game-engine) I am working on. With this game I really wanted to
test out some new rendering features in the game engine like the lighting system, particle system and GUI elements.

## Credits

* @Eliteomnomnivore
    * All music
    * Player sprite
    * Library assets
* @Denise
    * All basement artwork
* @Lvl30Caterpie
    * All dialogue

## Project structure

* [Common game code](game/src)
* [Swing platform code](swing/src)
* [JavaFX platform code](javafx/src)
* [Browser platform code](browser/src)

## JSON Schema

[JSON schema definitions](https://github.com/iamdudeman/sola-game-engine/tree/master/json-schema) are provided for
various
asset types. These can assist you in creating valid assets for the sola game engine to load when manually creating or
updating them.

* SpriteSheet
    * https://raw.githubusercontent.com/iamdudeman/sola-game-engine/master/json-schema/SpriteSheet.schema.json
* Font
    * https://raw.githubusercontent.com/iamdudeman/sola-game-engine/master/json-schema/Font.schema.json
* GuiDocument
    * https://raw.githubusercontent.com/iamdudeman/sola-game-engine/master/json-schema/GuiDocument.schema.json
* ControlConfig
    * https://raw.githubusercontent.com/iamdudeman/sola-game-engine/master/json-schema/ControlConfig.schema.json

### IntelliJ setup

1. Open settings
2. Go to `Languages & Frameworks | Schemas and DTDs | JSON Schema Mappings`
3. Click `+` and select the schema file to add
4. Add by file path pattern (recommendations below)
    * SpriteSheet -> `*.sprites.json`
    * Font -> `*.font.json`
    * GuiDocument -> `*.gui.json`
    * ControlConfig -> `*.controls.json`

## Packaging for release

### Browser zip file

Run the following gradle command

```shell
.\gradlew.bat distWebZip
```

The output will be at `browser/build/<gameName>-browser-<version>.zip`.
This can be deployed to places like `itch.io` when using the "HTML" project type.

### Swing + JavaFx fat jar

Run the following gradle command

```shell
.\gradlew.bat distFatJar
```

The output will be at `swing/build/<gameName>-swing-<version>.jar`
and `javafx/build/<gameName>-javafx-<os>-<version>.jar`.
Your users will need to have Java 17 installed to run the jar.

### Swing + JavaFx .exe

You also have the option to use [jpackage](
https://docs.oracle.com/en/java/javase/17/jpackage/packaging-overview.html) to create an executable exe file.
Your users will not need to have Java installed.

1. Install [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. Update $JAVA_HOME path environment variable
    * ex. C:\Program Files\Java\jdk-17.0.5
    * To test configuration run: `jpackage --version`
        * Should see the current jdk version returned: `17.0.5`
3. Run the following gradle command

```shell
.\gradlew.bat distFatJarZip
```

4. Output will be in the `build/jpackage` directory
