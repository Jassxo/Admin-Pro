# AdminTools

**AdminTools** is a high-performance, client-side moderation mod designed for Minecraft 1.21.x server staff. It provides an intuitive, modern animated GUI and powerful utilities to streamline moderation tasks without negatively impacting FPS.

## Features

- **Modern Animated GUI**: A sleek, dark-themed interface with rounded panels, smooth easing animations, and blur effects. Access it instantly from the Pause Menu or by pressing `Right Shift`.
- **Command Display**: Automatically renders executed moderation commands directly above players' nametags in the 3D world, allowing you to seamlessly track your own staff actions.
- **Chat Filter**: Quickly toggle between different chat filter modes (Normal, Server Only, Staff Chat Only, Custom Regex) to de-clutter your chat feed during busy situations.
- **Extensible Module System**: A solid architectural foundation that makes it incredibly easy for developers to add new moderation modules.
- **Performance First**: Zero unnecessary allocations. Invisible players are culled, and text is dynamically scaled based on distance.

## Installation

1. Download and install [Fabric Loader](https://fabricmc.net/) for Minecraft 1.21.x.
2. Download the [Fabric API](https://modrinth.com/mod/fabric-api) and place it in your `mods` folder.
3. Build the `AdminTools` mod (see Development Setup below) or download a compiled `.jar`.
4. Place the `admintools` `.jar` into your Minecraft `mods` folder.
5. Launch Minecraft!

## Usage

- **Open GUI**: Press `Right Shift` or click the "Admin" button in the standard Minecraft Pause Menu (`Esc`).
- **Configuration**: Changes made in the GUI are automatically saved to `config/admintools.json`.
- **Modules**: Enable or disable specific tools (like Command Display or Chat Filter) directly from the GUI.

## Development Setup

This project uses Gradle. To setup the workspace and compile the mod:

1. Clone the repository.
2. Generate the Fabric sources:
   ```bash
   ./gradlew genSources
   ```
3. Run the development client to test your changes:
   ```bash
   ./gradlew runClient
   ```
4. Build the final jar (will be generated in `build/libs`):
   ```bash
   ./gradlew build
   ```

## Architecture

- `admintools.AdminTools`: The main Client entrypoint.
- `admintools.modules`: Contains the `ModuleManager` and individual module implementations (e.g., `CommandDisplayModule`).
- `admintools.gui`: Contains the `AdminScreen` and modern UI elements.
- `admintools.mixins`: Injects custom rendering logic directly into vanilla Minecraft classes to achieve zero-overhead performance.

## License

All Rights Reserved.
