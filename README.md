
<img width="2048" height="220" alt="minecraft_title" src="https://github.com/user-attachments/assets/025a481f-6a64-44bf-8e5d-d3c820faa0c7" />



A Bukkit/Paper plugin that dynamically optimizes your Minecraft server based on real-time RAM and TPS usage.

## Features

### Hardware Detection
- Monitors RAM usage using `Runtime.getRuntime()`
- Tracks TPS using `MinecraftServer.getServer().tickTimes`
- Real-time performance metrics collection

### Dynamic Scaling Levels

**Level 1 (Healthy):**
- Triggered when RAM usage < 70% and TPS is 20
- Maintains View Distance of 10 and Simulation Distance of 8
- All mob AI enabled

**Level 2 (Moderate):**
- Triggered when RAM usage > 80% or TPS drops to 18
- Reduces View Distance to 8 and Simulation Distance to 6
- Disables AI for distant mobs (>64 blocks away)

**Level 3 (Critical):**
- Triggered when RAM usage > 92% or TPS drops below 15
- Reduces View Distance to 5 and Simulation Distance to 4
- Pauses non-essential mob AI
- Clears ground items older than 2 minutes

### Visual Integrity
- Uses `entity.setAI(false)` instead of deleting entities
- World feels "full" to players even during optimization
- Only hard-clears items in Level 3 critical mode

## Configuration

All settings are customizable via `config.yml`:

```yaml
# Performance thresholds
thresholds:
  ram:
    moderate: 80.0
    critical: 92.0
  tps:
    moderate: 18.0
    critical: 15.0

# Distance settings
distances:
  view:
    min: 4
    max: 16
  simulation:
    min: 4
    max: 12

# Optimization settings
optimization:
  check_interval_ticks: 200        # 10 seconds
  chunk_unload_delay_minutes: 5
  ground_item_clear_minutes: 2
```

## Commands

- `/smartoptimizer reload` - Reload configuration
- `/smartoptimizer status` - Show current optimization status

## Installation

1. Download the latest `SmartOptimizer-{version}.jar`
2. Place it in your server's `plugins` folder
3. Restart the server or reload plugins
4. Customize `plugins/SmartOptimizer/config.yml` as needed

## Requirements

- Minecraft 1.19+ (Paper/Spigot/Purpur/Bukkit)
- Java 17+
- No additional dependencies

## Development

Built with Maven. Use `mvn clean package` to build the plugin.

## License

This project is open source. Feel free to contribute and improve!
