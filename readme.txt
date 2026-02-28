# 🚀 Space Adventure

A 2D space shooter game built with **libGDX** and **Box2D** physics engine for Android. Navigate your spaceship, shoot rocks, collect power-ups, and survive as long as you can in an increasingly challenging asteroid field.

## Gameplay

- **Tap** anywhere to rotate the spaceship and fire bullets in that direction
- **Drag** for smooth real-time aiming
- Destroy rocks to earn points — larger rocks split into smaller ones
- Collect power-ups that drop randomly:
  - 🔵 **Rings** — activates a temporary force field shield
  - 🔫 **Bullets** — increases fire rate
  - ❤️ **Health** — restores health
- Difficulty scales dynamically with your score (rock speed, spawn rate, and rock levels increase)
- Spaceship blinks on hit, and explodes into fragments at zero health

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Game Engine | [libGDX](https://libgdx.com/) |
| Physics | Box2D (collision detection, body dynamics, contact listeners) |
| Platform | Android (minSdk 16, targetSdk 34) |
| Architecture | MVVM — `ViewModel` + `LiveData` for game state |
| UI | Android Fragments + libGDX `AndroidFragmentApplication` |
| Language | Java |
| Build | Gradle multi-module (`android` + `core`) |

## Project Structure

```
├── android/                    # Android platform module
│   ├── src/
│   │   └── com/hesham0_0/spaceship/
│   │       └── ui/
│   │           ├── game/
│   │           │   ├── GameActivity.java       # Game screen with score & health bar
│   │           │   ├── GameFragment.java        # Hosts libGDX game view
│   │           │   ├── GameViewModel.java       # Game state (points, health, pause)
│   │           │   └── GameState.java           # PLAYING / PAUSED / STOPPED
│   │           └── menu/
│   │               ├── MenuActivity.java        # Main menu (fullscreen immersive)
│   │               ├── MenuFragment.java        # Start button → launches game
│   │               ├── MenuViewModel.java
│   │               └── GameOverFragment.java    # Game over screen with score
│   ├── res/                    # Android layouts, drawables, strings
│   ├── AndroidManifest.xml
│   └── build. gradle
│
├── core/                       # Platform-independent game logic
│   └── src/
│       └── com/hesham0_0/spaceship/
│           ├── SpaceshipGame.java              # Main game loop (create, render, input)
│           ├── SpaceshipContactListener.java   # Box2D collision handling
│           ├── SpaceshipContactCallback.java   # Collision event interface
│           ├── SpaceshipUtils.java             # Constants, colors, difficulty configs
│           ├── SpaceshipState.java             # STABLE / DESTROYED / BLINKS
│           ├── PointsUpdateListener.java       # Game → Android UI bridge
│           ├── PowerType.java                  # RINGS / BULLETS / HEALTH
│           └── models/
│               ├── Spaceship.java              # Player ship (rotation, pieces, sensor)
│               ├── SpaceshipBullet.java        # Projectiles with Box2D bodies
│               ├── SpaceshipRock.java          # Asteroids (multi-level, splittable)
│               ├── SpaceshipRing.java          # Force field shield rings
│               ├── SpaceshipExplosion.java     # Explosion VFX with score popup
│               ├── RockFragments.java          # Rock destruction particle effect
│               ├── PowerItem.java              # Collectible power-up items
│               └── Wall.java                   # Invisible boundary walls
│
├── assets/                     # Game textures, backgrounds, sprites
│   └── spaceshipGame/
├── build. gradle                # Root build config
└── settings.gradle
```

## Key Features

### Physics & Collision
- Full **Box2D** integration with dynamic, kinematic, and static bodies
- `ContactListener` dispatching collisions between bullets, rocks, rings, walls, and power-ups
- Polygon-based sensor shape on the spaceship for precise overlap detection via `Intersector.`

### Game Mechanics
- **Rock splitting** — level 3 rocks break into level 2 + level 1; level 2 breaks into two level 1s
- **Adaptive difficulty** — rock speed, spawn interval, and max rock level scale with player score
- **Force field rings** — sinusoidal pressure-based ring activation with 3 concentric levels
- **Destruction VFX** — spaceship and rocks fragment into pieces with velocity dispersion and alpha fade

### Android Integration
- **MVVM architecture** — `GameViewModel` holds score, health, and game state as `MutableLiveData.`
- **Fragment-based** — libGDX game runs inside `AndroidFragmentApplication`, observed by activity UI
- **Immersive mode** — fullscreen with hidden status bar and navigation bar
- **Health bar** — animated `ProgressBar` with `ObjectAnimator.`

## Build & Run

**Requirements:** Android Studio, JDK 8+, Android SDK 34

```bash
# Clone
git clone https://github.com/hesham0_0/SpaceAdventure.git

# Open in Android Studio and run on device/emulator
# Or via command line:
./gradlew android:installDebug
```

## License

This project is open source. Feel free to use it for learning and reference.

---

**Built by [Mohamed Hesham](https://www.mohamedhesham.dev)** — Android Developer
