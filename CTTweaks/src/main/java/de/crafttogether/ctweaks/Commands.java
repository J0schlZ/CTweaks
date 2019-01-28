
package de.crafttogether.ctweaks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import de.crafttogether.CTweaks;
import net.md_5.bungee.api.ChatColor;

public class Commands implements TabExecutor
{
    private CTweaks plugin;
    
    public Commands() {
        this.plugin = CTweaks.getInstance();
    }
    
    public boolean onCommand( CommandSender sender,  Command cmd,  String st,  String[] args) {
    	if (!(cmd.getName().equalsIgnoreCase("ctweaks") || !cmd.getName().equalsIgnoreCase("listmobs")) || args.length > 2)
    		return true;
    	
        Player p = null;
        
        if (sender instanceof Player) {
            p = Bukkit.getPlayer(((Player) sender).getUniqueId());
        }
        
        if (p == null)
        	return true;
        
        if (args.length < 1) {
        	p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aHi :)"));
        	return true;
        }
        
        switch (args[0]) {
	    	case "enable": {
	    		if (!cmd.getName().equals("ctweaks"))
	    			break;
	    		
	    		if (args.length > 1 && args[1].equalsIgnoreCase("liquid")) {
	    			Config.isLiquidBlocked = false;
	    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6LiquidFlow &aaktiviert."));
	    		}
	    		if (args.length > 1 && args[1].equalsIgnoreCase("spawning")) {
	    			Config.isSpawningBlocked = false;
	    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6MobSpawning &aaktiviert."));
	    		}
	    		if (args.length > 1 && args[1].equalsIgnoreCase("redstone")) {
	    			Config.isRedstoneBlocked = false;
	    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Redstone &aaktiviert."));
	    		}
	    		if (args.length > 1 && args[1].equalsIgnoreCase("commandblock")) {
	    			Config.isCommandBlockBlocked = false;
	    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Befehlsblöcke &aaktiviert."));
	    		}
	    		break;
	    	}
	    	
	    	case "disable": {
	    		if (!cmd.getName().equals("ctweaks"))
	    			break;
	    		
	    		if (args.length > 1 && args[1].equalsIgnoreCase("liquid")) {
	    			Config.isLiquidBlocked = true;
	    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6LiquidFlow &cdeaktiviert."));
	    		}
	    		if (args.length > 1 && args[1].equalsIgnoreCase("spawning")) {
	    			Config.isSpawningBlocked = true;
	    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6MobSpawning &cdeaktiviert."));
	    		}
	    		if (args.length > 1 && args[1].equalsIgnoreCase("redstone")) {
	    			Config.isRedstoneBlocked = true;
	    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Redstone &cdeaktiviert."));
	    		}
	    		if (args.length > 1 && args[1].equalsIgnoreCase("commandblock")) {
	    			Config.isCommandBlockBlocked = true;
	    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Befehlsblöcke &cdeaktiviert."));
	    		}
	    		break;
	    	}
	    	
	    	case "info": {
	    		if (!cmd.getName().equals("ctweaks"))
	    			break;
	    		
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7-----------------------------------"));
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6LiquidFlow: " + (Config.isLiquidBlocked ? "&cGestoppt" : "&aAktiv")));
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6MobSpawning: " + (Config.isSpawningBlocked ? "&cGestoppt" : "&aAktiv")));
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Redstone: " + (Config.isRedstoneBlocked ? "&cGestoppt" : "&aAktiv")));
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6CommandBlocks: " + (Config.isCommandBlockBlocked ? "&cGestoppt" : "&aAktiv")));
	    		p.sendMessage("");
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eBlockFromToEvent: &a" + Data.firedLiquidEvents + "/s"));
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eEntitySpawnEvent: &a" + Data.firedSpawningEvents + "/s"));
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eBlockRedstoneEvent: &a" + Data.firedRedstoneEvents + "/s"));
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eCommandBlocksFired: &a" + Data.firedCommandBlocks + "/s"));
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7-----------------------------------"));
	    		break;
	    	}
	    	
	    	case "monitor": {
	    		if (!cmd.getName().equals("ctweaks"))
	    			break;
	    		
	    		if (!p.hasPermission("ctweaks.monitor"))
	    			p.sendMessage(ChatColor.RED + "Dazu hast du keine Berechtigung.");
	    		
	    		if (plugin.monitorPlayers.contains(p))
	    			plugin.monitorPlayers.remove(p);
	    		else
	    			plugin.monitorPlayers.add(p);	
	    		
	    		break;
	    	}
	    	
	    	case "listmobs": {
	    		HashMap<EntityType, List<LivingEntity>> found = null;
	    		World world = p.getWorld();
	    		String range = "world";
	    		String color;
	    		
	    		if (args.length > 1) {
	    			if (args[1].equalsIgnoreCase("all")) {
	    				range = "global";
	    				found = Methods.sortEntities(Methods.getEntities());
	    			}
	    			else if (args[1].matches("[0-9.-]+")) {
	    				found = Methods.sortEntities(Methods.getEntitiesAround(Methods.getEntities(p.getWorld()), p.getLocation(), Integer.parseInt(args[1])));
	    				range = "radius";
	    			}
	    		}
	    		
	    		if (found == null)
	    			found = Methods.sortEntities(Methods.getEntities(world));

	    		int total = 0;
	    		for (Entry<EntityType, List<LivingEntity>> entry : found.entrySet()) {
	    			EntityType entType = entry.getKey();
	    			List<LivingEntity> list = entry.getValue();
	    			
	    			if (list.size() > 45)
	    				color = "&6";
	    			if (list.size() > 49)
	    				color = "&4";
	    			else
	    				color = "&a";
	    			
	    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', color + list.size() + " &e" + entType.name() + "&6 gefunden!"));
	    			total = list.size() + total;
	    		}
	    		
	    		color = "&a";
	    		String rangeTxt;
	    		
	    		if (range.equals("world"))
	    			rangeTxt = " in &e" + world.getName();
	    		else if (range.equals("radius"))
	    			rangeTxt = " im Umkreis von &a" + Integer.parseInt(args[1]) + " &6Blöcken";
	    		else
	    			rangeTxt = "";
	    		
	    		p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Insgesamt wurden " + color + total + " &6Kreaturen" + rangeTxt + " &6gefunden!"));
	    		break;
	    	}
	    	
	    	case "redstone": {
	    		
	    	}
        }
        
        return true;
    }

    public List<String> onTabComplete( CommandSender sender,  Command cmd,  String alias,  String[] args) {
    	if (!(cmd.getName().equalsIgnoreCase("ctweaks") || !cmd.getName().equalsIgnoreCase("listmobs")))
    		return null;
    	
    	ArrayList<String> newList = new ArrayList<String>();
    	ArrayList<String> proposals = new ArrayList<String>();
    	
    	if (cmd.getName().equals("ctweaks")) {
    		if (args.length < 2) {
	    		proposals.add("info");
	    		proposals.add("monitor");
	    		proposals.add("enable");
	    		proposals.add("disable");
	    		proposals.add("listmobs");
    		}
    		
    		else if (args.length < 3 && (args[0].equals("enable") || args[0].equals("disable"))) {
	    		proposals.add("liquid");
	    		proposals.add("spawning");
	    		proposals.add("redstone");
	    		proposals.add("commandblock");
    		}
    		
    		else if (args.length < 3 && args[0].equals("listmobs")) {
	    		proposals.add("all");
	    		proposals.add("32");
	    		proposals.add("64");
	    		proposals.add("128");
	    		proposals.add("192");
    		}
    	}
    	
    	if (cmd.getName().equals("listmobs") && args.length < 2)
    		proposals.add("all");
    	
        if (args.length < 1 || args[args.length - 1].equals(""))
            newList = proposals;
        else {
            for (String value : proposals) {
                if (value.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                    newList.add(value);
            }
        }
        
        return newList;
    }
}
