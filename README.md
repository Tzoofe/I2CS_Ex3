# Ex3 - Pac-Man Server Implementation
<img src="imgs/Screenshot 2026-03-15 014255.png">

### 2168 lines of code and counting!!


## How to Run

### Ex3Server (Full Game - Custom Server)
```bash
unzip Ex3Server.zip
cd Ex3Server
java -jar Ex3Server.jar
```

**Controls:** WASD to move Pac-Man

### Ex3Client (Client Only)
```bash
java -jar Ex3Client.jar
```

---

## Client Package (`src/`)

### Classes

| Class | Purpose |
|-------|---------|
| `Ex3Main` | Entry point - connects to game server, runs game loop |
| `Ex3Algo` | AI algorithm - decides Pac-Man movement |
| `Map` | Pathfinding using BFS |
| `Index2D` / `Pixel2D` | 2D coordinate helpers |
| `ManualAlgo` | Manual keyboard control |

### Ex3Algo - AI States

| State | Trigger | Behavior |
|-------|---------|----------|
| `NORMAL` | Default | Collect pink coins, then green |
| `PANIC` | Dangerous ghost within 2 cells | Run away from nearest ghost |
| `HUNTING` | Eatable ghost within 2 cells | Chase and eat the ghost |

### Ex3Algo Methods

| Method | Description |
|--------|-------------|
| `move(game)` | Main AI decision - returns direction |
| `updateState()` | Checks ghost proximity, sets state |
| `runFromGhost()` | Calculates escape direction |
| `chaseGhost()` | Finds path to nearest eatable ghost |
| `findNearestColor()` | Locates closest coin/power-up |
| `getDirection()` | Converts target position to direction |

### Map Methods

| Method | Description |
|--------|-------------|
| `shortestPath(from, to, obstacle)` | BFS pathfinding between two points |
| `allDistance(start, obstacle)` | Distance map from starting point |

---

## Server Package (`src/server/`)

### Classes

| Class | Purpose |
|-------|---------|
| `Ex3Main_Server` | Entry point - runs game loop, handles input |
| `MyGame` | Core game logic - movement, collision, rendering |
| `MyGhost` | Ghost entity - position, state, eatable timer |
| `BoardLoader` | Creates board layout and spawns ghosts |
| `StdDraw` | Graphics library for rendering |

### MyGame Methods

| Method | Description |
|--------|-------------|
| `init()` | Loads board, spawns ghosts, sets up display |
| `move(dir)` | Moves Pac-Man, collects coins, checks win |
| `moveGhost()` | Random ghost movement |
| `checkCollision()` | Handles ghost-Pacman collision |
| `tickGhosts()` | Decrements eatable timers |
| `draw()` | Renders board, Pac-Man, and ghosts |

### Game Features

- **Power-ups:** Cyan tiles make ghosts eatable for 5 seconds
- **Ghost removal:** Eating a ghost gives +100 points and removes it
- **Win condition:** Collect all coins and power-ups
- **Ghost delay:** Ghosts start moving after 5 seconds
