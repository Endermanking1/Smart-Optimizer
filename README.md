# Smart Optimizer - Dynamic Server Optimization Plugin

A multi-platform Minecraft server optimization plugin that dynamically adjusts performance based on real-time RAM and TPS usage.

![Minecraft Title](https://github.com/user-attachments/assets/025a481f-6a64-44bf-8e5d-d3c820faa0c7)

## 🚀 Available Versions

### ✅ PaperMC (Recommended - Full Support)
- **File:** `SmartOptimizer-Paper-26.1.jar`
- **Minecraft:** 1.21.4 (PaperMC 26.1)
- **Status:** **Production Ready** - All features implemented

### 🔨 Forge (Framework)
- **File:** `SmartOptimizer-Forge-26.1.jar`
- **Minecraft:** 1.21.4
- **Status:** **Framework Complete** - Requires Forge API integration

### 🧵 Fabric (Framework)
- **File:** `SmartOptimizer-Fabric-26.1.jar`
- **Minecraft:** 1.21.4
- **Status:** **Framework Complete** - Requires Fabric API integration

## Features

### 🔍 Hardware Detection
- **Real-time RAM monitoring** using `ManagementFactory.getMemoryMXBean()`
- **TPS tracking** using `Bukkit.getServer().getTickTimes()` (PaperMC)
- **Performance metrics collection** every 200 ticks (10 seconds)

### ⚡ Dynamic Scaling Levels

**🟢 Level 1 (Healthy):**
- **Trigger:** RAM < 80% and TPS ≥ 18.0
- **Settings:** View Distance 10, Simulation Distance 8
- **Features:** All mob AI enabled, maximum performance

**🟡 Level 2 (Moderate):**
- **Trigger:** RAM > 80% or TPS < 18.0
- **Settings:** View Distance 8, Simulation Distance 6
- **Features:** Disables AI for distant mobs (>64 blocks away)

**🔴 Level 3 (Critical):**
- **Trigger:** RAM > 92% or TPS < 15.0
- **Settings:** View Distance 5, Simulation Distance 4
- **Features:** Pauses non-essential mob AI, clears old ground items

### 🎮 Visual Integrity
- **Smart AI management** using `entity.setAI(false)` instead of deletion
- **Preserves world feel** - maintains entity presence
- **Only hard-clears items** in Level 3 critical mode (2+ minutes old)

### 🧠 Smart Unloading
- **Automatic chunk unloading** in empty dimensions (5+ minutes)
- **Spawn chunk protection** - never unloads spawn areas
- **Entity-aware unloading** - skips chunks with entities
- **Dimension tracking** - monitors player presence per world

### 🌐 Cross-Platform Compatibility
- **PaperMC:** Full API support with simulation distance control
- **Spigot/Bukkit:** Graceful degradation - simulation distance disabled
- **Purpur:** Full compatibility with enhanced performance
- **Forge/Fabric:** Framework ready for platform integration

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

### PaperMC (Recommended)
1. Download `SmartOptimizer-Paper-26.1.jar`
2. Place in `plugins/` folder
3. Restart server or reload plugins
4. Configure `plugins/SmartOptimizer/config.yml`

### Forge/Fabric
1. Download respective `SmartOptimizer-[Platform]-26.1.jar`
2. Place in `mods/` folder
3. **Note:** Framework version - requires platform-specific API integration

## Requirements

### PaperMC/Spigot/Bukkit
- **Minecraft:** 1.21.4+ (PaperMC 26.1 recommended)
- **Java:** 17+
- **Dependencies:** None (bundled)

### Forge/Fabric
- **Minecraft:** 1.21.4
- **Java:** 17+
- **Status:** Framework - requires additional development

## Development

### Building from Source

#### PaperMC Version
```bash
mvn clean package
```

#### Forge/Fabric Versions
```bash
# Forge
cd forge && ./build-forge.bat

# Fabric  
cd fabric && ./build-fabric.bat
```

## What's New in v26.1

### ✅ Major Updates
- **Multi-platform support** - PaperMC, Forge, and Fabric versions
- **Updated to Minecraft 1.21.4** - PaperMC 26.1 compatibility
- **Improved naming** - `SmartOptimizer-[Platform]-26.1.jar`
- **Enhanced compatibility** - Graceful degradation for Spigot/Bukkit

### 🔧 Technical Improvements
- **Simulation distance handling** - PaperMC API with Spigot fallback
- **Better error handling** - Robust platform detection
- **Optimized compilation** - Reduced JAR sizes
- **Framework architecture** - Ready for Forge/Fabric integration

### 🐛 Bug Fixes
- **Fixed plugin.yml location** - Now properly included in JAR
- **Resolved naming conflicts** - No spaces in plugin name
- **Corrected API calls** - Proper type handling for tick times
- **Platform detection** - Better handling of different server types

## License

MIT License - Open source and free to use, modify, and distribute.

## Contributing

GitHub: https://github.com/Endermanking1/Smart-Optimizer

Feel free to submit issues, feature requests, and pull requests!
