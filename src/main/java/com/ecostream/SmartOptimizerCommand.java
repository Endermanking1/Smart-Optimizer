package com.ecostream;

import com.ecostream.tasks.ServerOptimizationTask;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SmartOptimizerCommand implements CommandExecutor {
    
    private final EcoStream plugin;
    
    public SmartOptimizerCommand(EcoStream plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("smartoptimizer.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload":
                reloadConfig(sender);
                break;
            case "status":
                showStatus(sender);
                break;
            default:
                sendHelpMessage(sender);
                break;
        }
        
        return true;
    }
    
    private void reloadConfig(CommandSender sender) {
        plugin.getConfigManager().reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Smart Optimizer configuration reloaded successfully!");
    }
    
    private void showStatus(CommandSender sender) {
        ServerOptimizationTask task = plugin.getOptimizationTask();
        if (task == null) {
            sender.sendMessage(ChatColor.RED + "Optimization task is not running!");
            return;
        }
        
        ServerOptimizationTask.OptimizationLevel level = task.getCurrentLevel();
        ChatColor levelColor = getLevelColor(level);
        
        sender.sendMessage(ChatColor.GOLD + "=== Smart Optimizer Status ===");
        sender.sendMessage(ChatColor.YELLOW + "Current Optimization Level: " + levelColor + level.name());
        sender.sendMessage(ChatColor.YELLOW + "Debug Mode: " + (plugin.getConfigManager().isDebugMode() ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
        sender.sendMessage(ChatColor.YELLOW + "Check Interval: " + ChatColor.WHITE + plugin.getConfigManager().getCheckIntervalTicks() + " ticks");
        
        // Show thresholds
        sender.sendMessage(ChatColor.GRAY + "RAM Thresholds: Moderate=" + ChatColor.WHITE + 
                         plugin.getConfigManager().getRamThresholdModerate() + "%, " +
                         ChatColor.GRAY + "Critical=" + ChatColor.WHITE + 
                         plugin.getConfigManager().getRamThresholdCritical() + "%");
        
        sender.sendMessage(ChatColor.GRAY + "TPS Thresholds: Moderate=" + ChatColor.WHITE + 
                         plugin.getConfigManager().getTpsThresholdModerate() + ", " +
                         ChatColor.GRAY + "Critical=" + ChatColor.WHITE + 
                         plugin.getConfigManager().getTpsThresholdCritical());
    }
    
    private ChatColor getLevelColor(ServerOptimizationTask.OptimizationLevel level) {
        switch (level) {
            case HEALTHY: return ChatColor.GREEN;
            case MODERATE: return ChatColor.YELLOW;
            case CRITICAL: return ChatColor.RED;
            default: return ChatColor.WHITE;
        }
    }
    
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== Smart Optimizer Commands ===");
        sender.sendMessage(ChatColor.YELLOW + "/smartoptimizer reload " + ChatColor.GRAY + "- Reload configuration");
        sender.sendMessage(ChatColor.YELLOW + "/smartoptimizer status " + ChatColor.GRAY + "- Show current status");
    }
}
