package com.ecostream.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
    
    private final Plugin plugin;
    private FileConfiguration config;
    
    // Default configuration values
    private static final double DEFAULT_RAM_THRESHOLD_MODERATE = 80.0;
    private static final double DEFAULT_RAM_THRESHOLD_CRITICAL = 92.0;
    private static final double DEFAULT_TPS_THRESHOLD_MODERATE = 18.0;
    private static final double DEFAULT_TPS_THRESHOLD_CRITICAL = 15.0;
    
    private static final int DEFAULT_MIN_VIEW_DISTANCE = 4;
    private static final int DEFAULT_MAX_VIEW_DISTANCE = 16;
    private static final int DEFAULT_MIN_SIMULATION_DISTANCE = 4;
    private static final int DEFAULT_MAX_SIMULATION_DISTANCE = 12;
    
    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        this.plugin.saveDefaultConfig();
    }
    
    public void loadConfig() {
        this.config = plugin.getConfig();
        setDefaults();
    }
    
    private void setDefaults() {
        config.addDefault("thresholds.ram.moderate", DEFAULT_RAM_THRESHOLD_MODERATE);
        config.addDefault("thresholds.ram.critical", DEFAULT_RAM_THRESHOLD_CRITICAL);
        config.addDefault("thresholds.tps.moderate", DEFAULT_TPS_THRESHOLD_MODERATE);
        config.addDefault("thresholds.tps.critical", DEFAULT_TPS_THRESHOLD_CRITICAL);
        
        config.addDefault("distances.view.min", DEFAULT_MIN_VIEW_DISTANCE);
        config.addDefault("distances.view.max", DEFAULT_MAX_VIEW_DISTANCE);
        config.addDefault("distances.simulation.min", DEFAULT_MIN_SIMULATION_DISTANCE);
        config.addDefault("distances.simulation.max", DEFAULT_MAX_SIMULATION_DISTANCE);
        
        config.addDefault("optimization.check_interval_ticks", 200);
        config.addDefault("optimization.chunk_unload_delay_minutes", 5);
        config.addDefault("optimization.ground_item_clear_minutes", 2);
        
        config.addDefault("settings.debug", false);
        
        plugin.saveConfig();
    }
    
    // Threshold getters
    public double getRamThresholdModerate() {
        return config.getDouble("thresholds.ram.moderate", DEFAULT_RAM_THRESHOLD_MODERATE);
    }
    
    public double getRamThresholdCritical() {
        return config.getDouble("thresholds.ram.critical", DEFAULT_RAM_THRESHOLD_CRITICAL);
    }
    
    public double getTpsThresholdModerate() {
        return config.getDouble("thresholds.tps.moderate", DEFAULT_TPS_THRESHOLD_MODERATE);
    }
    
    public double getTpsThresholdCritical() {
        return config.getDouble("thresholds.tps.critical", DEFAULT_TPS_THRESHOLD_CRITICAL);
    }
    
    // Distance getters
    public int getMinViewDistance() {
        return config.getInt("distances.view.min", DEFAULT_MIN_VIEW_DISTANCE);
    }
    
    public int getMaxViewDistance() {
        return config.getInt("distances.view.max", DEFAULT_MAX_VIEW_DISTANCE);
    }
    
    public int getMinSimulationDistance() {
        return config.getInt("distances.simulation.min", DEFAULT_MIN_SIMULATION_DISTANCE);
    }
    
    public int getMaxSimulationDistance() {
        return config.getInt("distances.simulation.max", DEFAULT_MAX_SIMULATION_DISTANCE);
    }
    
    // Optimization settings
    public int getCheckIntervalTicks() {
        return config.getInt("optimization.check_interval_ticks", 200);
    }
    
    public int getChunkUnloadDelayMinutes() {
        return config.getInt("optimization.chunk_unload_delay_minutes", 5);
    }
    
    public int getGroundItemClearMinutes() {
        return config.getInt("optimization.ground_item_clear_minutes", 2);
    }
    
    // Debug setting
    public boolean isDebugMode() {
        return config.getBoolean("settings.debug", false);
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
}
