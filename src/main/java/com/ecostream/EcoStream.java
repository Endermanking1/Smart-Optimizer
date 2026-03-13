package com.ecostream;

import com.ecostream.tasks.ServerOptimizationTask;
import com.ecostream.config.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EcoStream extends JavaPlugin {
    
    private ConfigManager configManager;
    private ServerOptimizationTask optimizationTask;
    
    @Override
    public void onEnable() {
        // Initialize config manager
        this.configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        // Initialize and start optimization task
        this.optimizationTask = new ServerOptimizationTask(this);
        optimizationTask.start();
        
        // Register commands
        getCommand("smartoptimizer").setExecutor(new SmartOptimizerCommand(this));
        
        getLogger().info("Smart Optimizer has been enabled successfully!");
    }
    
    @Override
    public void onDisable() {
        if (optimizationTask != null) {
            optimizationTask.stop();
        }
        getLogger().info("Smart Optimizer has been disabled.");
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public ServerOptimizationTask getOptimizationTask() {
        return optimizationTask;
    }
}
