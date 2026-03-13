package com.ecostream.tasks;

import com.ecostream.EcoStream;
import com.ecostream.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.inventory.ItemStack;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServerOptimizationTask {
    
    private final EcoStream plugin;
    private final ConfigManager config;
    private BukkitTask task;
    
    // Track last player presence per dimension
    private final Map<UUID, Long> lastPlayerPresence = new ConcurrentHashMap<>();
    
    // Current optimization level
    private OptimizationLevel currentLevel = OptimizationLevel.HEALTHY;
    
    public ServerOptimizationTask(EcoStream plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
    }
    
    public void start() {
        int interval = config.getCheckIntervalTicks();
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::optimizeServer, interval, interval);
    }
    
    public void stop() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
    
    private void optimizeServer() {
        try {
            // Get current server metrics
            ServerMetrics metrics = getServerMetrics();
            
            // Determine optimization level
            OptimizationLevel newLevel = determineOptimizationLevel(metrics);
            
            // Apply optimizations if level changed
            if (newLevel != currentLevel) {
                applyOptimizations(newLevel, metrics);
                currentLevel = newLevel;
                
                if (config.isDebugMode()) {
                    plugin.getLogger().info("Optimization level changed to: " + newLevel);
                    plugin.getLogger().info("RAM: " + String.format("%.1f%%", metrics.ramUsagePercent) + 
                                          ", TPS: " + String.format("%.1f", metrics.tps));
                }
            }
            
            // Always run maintenance tasks
            performMaintenanceTasks();
            
        } catch (Exception e) {
            plugin.getLogger().warning("Error during optimization: " + e.getMessage());
        }
    }
    
    private ServerMetrics getServerMetrics() {
        // RAM usage
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
        long maxMemory = memoryBean.getHeapMemoryUsage().getMax();
        double ramUsagePercent = (double) usedMemory / maxMemory * 100;
        
        // TPS calculation
        long[] recentTicks = Bukkit.getServer().getTickTimes();
        double totalTickTime = 0;
        for (long tickTime : recentTicks) {
            totalTickTime += tickTime;
        }
        double averageTickTime = totalTickTime / recentTicks.length / 1000000.0; // Convert to milliseconds
        double tps = Math.min(20.0, 1000.0 / Math.max(1.0, averageTickTime));
        
        return new ServerMetrics(ramUsagePercent, tps, usedMemory, maxMemory);
    }
    
    private OptimizationLevel determineOptimizationLevel(ServerMetrics metrics) {
        double ramThresholdModerate = config.getRamThresholdModerate();
        double ramThresholdCritical = config.getRamThresholdCritical();
        double tpsThresholdModerate = config.getTpsThresholdModerate();
        double tpsThresholdCritical = config.getTpsThresholdCritical();
        
        // Check critical conditions first
        if (metrics.ramUsagePercent > ramThresholdCritical || metrics.tps < tpsThresholdCritical) {
            return OptimizationLevel.CRITICAL;
        }
        
        // Check moderate conditions
        if (metrics.ramUsagePercent > ramThresholdModerate || metrics.tps < tpsThresholdModerate) {
            return OptimizationLevel.MODERATE;
        }
        
        return OptimizationLevel.HEALTHY;
    }
    
    private void applyOptimizations(OptimizationLevel level, ServerMetrics metrics) {
        switch (level) {
            case HEALTHY:
                applyHealthyOptimizations();
                break;
            case MODERATE:
                applyModerateOptimizations();
                break;
            case CRITICAL:
                applyCriticalOptimizations();
                break;
        }
    }
    
    private void applyHealthyOptimizations() {
        // Level 1: Maintain optimal performance
        int maxViewDistance = config.getMaxViewDistance();
        int maxSimulationDistance = config.getMaxSimulationDistance();
        
        // Set to maximum allowed values (capped at reasonable defaults)
        int viewDistance = Math.min(10, maxViewDistance);
        int simulationDistance = Math.min(8, maxSimulationDistance);
        
        applyDistanceSettings(viewDistance, simulationDistance);
        
        // Re-enable AI for any previously disabled mobs
        reEnableMobAI();
    }
    
    private void applyModerateOptimizations() {
        // Level 2: Reduce performance impact
        int minViewDistance = config.getMinViewDistance();
        int minSimulationDistance = config.getMinSimulationDistance();
        
        // Set to moderate values
        int viewDistance = Math.max(8, minViewDistance);
        int simulationDistance = Math.max(6, minSimulationDistance);
        
        applyDistanceSettings(viewDistance, simulationDistance);
        
        // Disable AI for distant mobs
        disableDistantMobAI(64); // 64 blocks away
    }
    
    private void applyCriticalOptimizations() {
        // Level 3: Aggressive optimization
        int minViewDistance = config.getMinViewDistance();
        int minSimulationDistance = config.getMinSimulationDistance();
        
        // Set to minimum values
        int viewDistance = Math.max(5, minViewDistance);
        int simulationDistance = Math.max(4, minSimulationDistance);
        
        applyDistanceSettings(viewDistance, simulationDistance);
        
        // Pause non-essential mob AI
        pauseNonEssentialMobAI();
        
        // Clear old ground items
        clearOldGroundItems();
    }
    
    private void applyDistanceSettings(int viewDistance, int simulationDistance) {
        for (World world : Bukkit.getWorlds()) {
            try {
                // Apply view distance
                if (world.getViewDistance() != viewDistance) {
                    world.setViewDistance(viewDistance);
                }
                
                // Apply simulation distance
                if (world.getSimulationDistance() != simulationDistance) {
                    world.setSimulationDistance(simulationDistance);
                }
            } catch (Exception e) {
                if (config.isDebugMode()) {
                    plugin.getLogger().warning("Failed to apply distance settings for world " + world.getName() + ": " + e.getMessage());
                }
            }
        }
    }
    
    private void disableDistantMobAI(int distance) {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (entity instanceof Monster || entity instanceof Animals) {
                    // Check if any player is within distance
                    boolean playerNearby = false;
                    for (Player player : world.getPlayers()) {
                        if (entity.getLocation().distance(player.getLocation()) <= distance) {
                            playerNearby = true;
                            break;
                        }
                    }
                    
                    if (!playerNearby && entity.hasAI()) {
                        entity.setAI(false);
                    }
                }
            }
        }
    }
    
    private void reEnableMobAI() {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                if (entity instanceof Monster || entity instanceof Animals) {
                    if (!entity.hasAI()) {
                        entity.setAI(true);
                    }
                }
            }
        }
    }
    
    private void pauseNonEssentialMobAI() {
        for (World world : Bukkit.getWorlds()) {
            for (LivingEntity entity : world.getLivingEntities()) {
                // Only disable AI for non-essential mobs (not bosses, not tamed animals)
                if ((entity instanceof Monster || entity instanceof Animals) && 
                    !(entity instanceof Tameable) && 
                    !isBossEntity(entity)) {
                    entity.setAI(false);
                }
            }
        }
    }
    
    private boolean isBossEntity(LivingEntity entity) {
        // Check for common boss entities
        return entity instanceof EnderDragon || 
               entity instanceof Wither || 
               entity.getType().name().contains("GIANT") ||
               entity.getType().name().contains("ELDER");
    }
    
    private void clearOldGroundItems() {
        long clearTime = System.currentTimeMillis() - (config.getGroundItemClearMinutes() * 60 * 1000L);
        
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Item) {
                    Item item = (Item) entity;
                    if (item.getTicksLived() * 50 > clearTime) { // Convert ticks to milliseconds
                        item.remove();
                    }
                }
            }
        }
    }
    
    private void performMaintenanceTasks() {
        // Track player presence for chunk unloading
        updatePlayerPresence();
        
        // Unload chunks in empty dimensions
        unloadChunksInEmptyDimensions();
    }
    
    private void updatePlayerPresence() {
        long currentTime = System.currentTimeMillis();
        
        for (World world : Bukkit.getWorlds()) {
            UUID worldUid = world.getUID();
            
            if (!world.getPlayers().isEmpty()) {
                // Players are present, update timestamp
                lastPlayerPresence.put(worldUid, currentTime);
            } else {
                // No players, check if we should remove this world from tracking
                if (lastPlayerPresence.containsKey(worldUid)) {
                    long lastPresence = lastPlayerPresence.get(worldUid);
                    long timeSinceLastPresence = currentTime - lastPresence;
                    
                    // Remove from tracking if no players for more than 30 minutes
                    if (timeSinceLastPresence > 30 * 60 * 1000L) {
                        lastPlayerPresence.remove(worldUid);
                    }
                }
            }
        }
    }
    
    private void unloadChunksInEmptyDimensions() {
        long currentTime = System.currentTimeMillis();
        long unloadDelay = config.getChunkUnloadDelayMinutes() * 60 * 1000L;
        
        for (World world : Bukkit.getWorlds()) {
            UUID worldUid = world.getUID();
            
            // Skip if players are currently in this world
            if (!world.getPlayers().isEmpty()) {
                continue;
            }
            
            // Check if enough time has passed since last player presence
            Long lastPresence = lastPlayerPresence.get(worldUid);
            if (lastPresence != null) {
                long timeSinceLastPresence = currentTime - lastPresence;
                
                if (timeSinceLastPresence > unloadDelay) {
                    // Unload chunks in this world
                    unloadChunksInWorld(world);
                }
            }
        }
    }
    
    private void unloadChunksInWorld(World world) {
        Chunk[] chunks = world.getLoadedChunks();
        int unloadedCount = 0;
        
        for (Chunk chunk : chunks) {
            try {
                // Only unload chunks that don't have entities or important blocks
                if (canUnloadChunk(chunk)) {
                    chunk.unload();
                    unloadedCount++;
                }
            } catch (Exception e) {
                if (config.isDebugMode()) {
                    plugin.getLogger().warning("Failed to unload chunk at " + chunk.getX() + "," + chunk.getZ() + ": " + e.getMessage());
                }
            }
        }
        
        if (unloadedCount > 0 && config.isDebugMode()) {
            plugin.getLogger().info("Unloaded " + unloadedCount + " chunks in world " + world.getName());
        }
    }
    
    private boolean canUnloadChunk(Chunk chunk) {
        // Don't unload chunks with entities
        if (chunk.getEntities().length > 0) {
            return false;
        }
        
        // Don't unload chunks near spawn
        World world = chunk.getWorld();
        if (chunk.getX() == world.getSpawnLocation().getChunk().getX() && 
            chunk.getZ() == world.getSpawnLocation().getChunk().getZ()) {
            return false;
        }
        
        return true;
    }
    
    public OptimizationLevel getCurrentLevel() {
        return currentLevel;
    }
    
    // Optimization levels enum
    public enum OptimizationLevel {
        HEALTHY,
        MODERATE,
        CRITICAL
    }
    
    // Server metrics data class
    private static class ServerMetrics {
        final double ramUsagePercent;
        final double tps;
        final long usedMemory;
        final long maxMemory;
        
        ServerMetrics(double ramUsagePercent, double tps, long usedMemory, long maxMemory) {
            this.ramUsagePercent = ramUsagePercent;
            this.tps = tps;
            this.usedMemory = usedMemory;
            this.maxMemory = maxMemory;
        }
    }
}
