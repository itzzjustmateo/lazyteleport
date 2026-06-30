# LazyTeleport

A production-ready Minecraft Paper/Folia teleportation plugin with warps, homes, spawn, and lobby systems.

## Features

- **Warps** - Unlimited named teleport points (`/warp`, `/setwarp`, `/delwarp`, `/listwarp`)
- **Homes** - Multiple homes per player (`/home`, `/sethome`, `/delhome`, `/listhome`)
- **Spawn** - Global spawn location (`/spawn`, `/setspawn`, `/delspawn`)
- **Lobby** - Global lobby location (`/lobby`, `/setlobby`, `/dellobby`)
- **Admin** - Help and reload commands (`/lazytp help`, `/lazytp reload`)
- **Configurable teleport** - Delay, cooldown, cancel-on-movement
- **Sounds & particles** - Configurable effects for every action
- **MiniMessage** - Rich text formatting with full MiniMessage support
- **Permission-based** - Every command has a dedicated permission node
- **Async** - Non-blocking teleportation and thread-safe storage
- **Paper & Folia** - Separate modules for each platform with shared logic

## Installation

1. Download the JAR for your platform from [Releases](https://github.com/itzzjustmateo/lazyteleport/releases)
2. Place it in your server's `plugins/` folder
3. Restart your server
4. Configure `plugins/LazyTeleport/config.yml` and `plugins/LazyTeleport/messages.yml` to your liking

## Building

```bash
git clone https://github.com/itzzjustmateo/lazyteleport.git
cd lazyteleport
./gradlew :paper:build    # Build Paper jar
./gradlew :folia:build    # Build Folia jar
```

JARs will be in `paper/build/libs/` and `folia/build/libs/`.

## Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/warp <name>` | Teleport to a warp | `lazyteleport.warp` |
| `/setwarp <name>` | Create a warp | `lazyteleport.setwarp` |
| `/delwarp <name>` | Delete a warp | `lazyteleport.delwarp` |
| `/listwarp` | List all warps | `lazyteleport.listwarp` |
| `/home [name]` | Teleport to your home | `lazyteleport.home` |
| `/sethome [name]` | Set your home | `lazyteleport.sethome` |
| `/delhome <name>` | Delete a home | `lazyteleport.delhome` |
| `/listhome` | List your homes | `lazyteleport.listhome` |
| `/spawn` | Teleport to spawn | `lazyteleport.spawn` |
| `/setspawn` | Set spawn location | `lazyteleport.setspawn` |
| `/delspawn` | Delete spawn location | `lazyteleport.delspawn` |
| `/lobby` | Teleport to lobby | `lazyteleport.lobby` |
| `/setlobby` | Set lobby location | `lazyteleport.setlobby` |
| `/dellobby` | Delete lobby location | `lazyteleport.dellobby` |
| `/lazytp help` | Show help | None |
| `/lazytp reload` | Reload config | `lazyteleport.admin` |
| `/lazytp version` | Show version | None |

## Permissions

| Permission | Default | Description |
|-----------|---------|-------------|
| `lazyteleport.warp` | `true` | Use `/warp` |
| `lazyteleport.setwarp` | `op` | Use `/setwarp` |
| `lazyteleport.delwarp` | `op` | Use `/delwarp` |
| `lazyteleport.listwarp` | `true` | Use `/listwarp` |
| `lazyteleport.home` | `true` | Use `/home` |
| `lazyteleport.sethome` | `true` | Use `/sethome` |
| `lazyteleport.delhome` | `true` | Use `/delhome` |
| `lazyteleport.listhome` | `true` | Use `/listhome` |
| `lazyteleport.spawn` | `true` | Use `/spawn` |
| `lazyteleport.setspawn` | `op` | Use `/setspawn` |
| `lazyteleport.delspawn` | `op` | Use `/delspawn` |
| `lazyteleport.lobby` | `true` | Use `/lobby` |
| `lazyteleport.setlobby` | `op` | Use `/setlobby` |
| `lazyteleport.dellobby` | `op` | Use `/dellobby` |
| `lazyteleport.admin` | `op` | Admin (inherits set/del perms) |
| `lazyteleport.bypass.cooldown` | `op` | Bypass teleport cooldown |
| `lazyteleport.bypass.delay` | `op` | Bypass teleport delay |

## Configuration

### config.yml

```yaml
teleport-delay: 3
teleport-cooldown: 10
cancel-on-movement: true
default-home-limit: 5

sounds:
  teleport-start: "ENTITY_ENDERMAN_TELEPORT"
  teleport-cancel: "BLOCK_NOTE_BLOCK_BASS"
  teleport-success: "ENTITY_ENDERMAN_TELEPORT"
  warp-created: "BLOCK_NOTE_BLOCK_PLING"
  warp-deleted: "BLOCK_NOTE_BLOCK_BASS"
  # ... see config.yml for full list

particles:
  teleport: "PORTAL"
  teleport-count: 30
  teleport-speed: 0.1
  warp-created: "VILLAGER_HAPPY"
  warp-deleted: "SMOKE"
  # ... see config.yml for full list
```

### messages.yml

All messages use MiniMessage format. Supports `<gradient>`, `<rainbow>`, colors, and placeholders like `<name>` and `<seconds>`.

## Project Structure

```
root
├── common/       # Shared logic (managers, commands, storage, config)
├── paper/        # Paper bootstrap and scheduler
└── folia/        # Folia bootstrap and scheduler
```

- `common` uses only cross-platform APIs and a scheduler abstraction
- `paper` implements the scheduler with BukkitScheduler
- `folia` implements the scheduler with RegionScheduler/EntityScheduler

## Requirements

- Java 21+
- Paper 1.21+ or Folia 1.21+

## License

MIT
