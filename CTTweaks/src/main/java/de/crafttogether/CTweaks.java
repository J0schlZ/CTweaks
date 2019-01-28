package de.crafttogether;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;

import de.crafttogether.ctweaks.Commands;
import de.crafttogether.ctweaks.Events;
import de.crafttogether.ctweaks.MonitorTask;

public class CTweaks extends JavaPlugin
{
    private static CTweaks plugin;
    
    private MonitorTask monitor;
    public List<Player> monitorPlayers;

    private MultiverseCore multiverse;
    public boolean MVLoaded;
    
    public void onEnable() {
        CTweaks.plugin = this;
        
        PluginManager pm = this.getServer().getPluginManager();
        Plugin mvPlugin = null;

        this.MVLoaded = (pm.getPlugin("Multiverse-Core") != null);
        if (this.MVLoaded) {
        	mvPlugin = pm.getPlugin("Multiverse-Core");
            if (mvPlugin instanceof MultiverseCore)
                this.multiverse = (MultiverseCore) mvPlugin;
            else {
                this.getLogger().warning("Couln't find Multiverse-Core.");
                this.MVLoaded = false;
            }
        }
        
        this.monitorPlayers = new ArrayList<Player>();
        this.monitor = new MonitorTask();

        Commands cmdHandler = new Commands();
        this.registerCommand("ctweaks", cmdHandler);
        Bukkit.getPluginManager().registerEvents(new Events(), this);
    }
    
    public void onDisable() {
    	this.monitor.cancel();
    }
    
    public MonitorTask getMonitor() {
    	return this.monitor;
    }

    public void registerCommand(final String cmd, final TabExecutor executor) {
        this.getCommand(cmd).setExecutor(executor);
        this.getCommand(cmd).setTabCompleter(executor);
    }
    
    public MultiverseCore getMultiverseCore() {
        return this.multiverse.getCore();
    }
    
    public static CTweaks getInstance() {
        return plugin;
    }
}