package de.crafttogether.ctweaks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import de.crafttogether.CTweaks;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class MonitorTask extends BukkitRunnable {
	private CTweaks plugin;

	public int firedLiquidEvents = 0;
	public int firedSpawningEvents = 0;
	public int firedRedstoneEvents = 0;
	public int firedCommandBlocks = 0;
	
	public MonitorTask() {
		this.plugin = CTweaks.getInstance();
		this.runTaskTimerAsynchronously(plugin, 20L, 20L);
	}

	public void run() {		
		Data.firedLiquidEvents = this.firedLiquidEvents;
		Data.firedSpawningEvents = this.firedSpawningEvents;
		Data.firedRedstoneEvents = this.firedRedstoneEvents;
		Data.firedCommandBlocks = this.firedCommandBlocks;
		
		this.firedLiquidEvents = 0;
		this.firedSpawningEvents = 0;
		this.firedRedstoneEvents = 0;
		this.firedCommandBlocks = 0;
			
		int range = 64;
		String output;
		
		Boolean actionBarAlreadySend = false;
		
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			World world = p.getWorld();
			
	        MultiverseCore multiverse = plugin.getMultiverseCore();
	        if (multiverse != null) {
	        	multiverse = multiverse.getCore();
	            final Collection<MultiverseWorld> MVWorlds = (Collection<MultiverseWorld>)multiverse.getMVWorldManager().getMVWorlds();
	            for (final MultiverseWorld MVWorld : MVWorlds) {
	            	World cbWorld = MVWorld.getCBWorld();
	            	List<LivingEntity> list = null;
					try {
			    		list = cbWorld.getLivingEntities();
			    	}
			    	catch (ConcurrentModificationException e) {}
					
					if (list != null)
						Data.livingEntities.put(cbWorld, list);
	            }
	        }
			
			// Get entities in Range of Player
			HashMap<EntityType, List<LivingEntity>> found = Methods.sortEntities(Methods.getEntitiesAround(Methods.getEntities(p.getLocation().getWorld()), p.getLocation(), 192));
			
			for (Entry<EntityType, List<LivingEntity>> entry : found.entrySet()) {
				EntityType entType = entry.getKey();
				List<LivingEntity> entities = entry.getValue();
				
				if (!entType.equals(EntityType.COD) && !entType.equals(EntityType.SALMON) && !entType.equals(EntityType.TROPICAL_FISH))
					continue;
				
				if (entities.size() > 35) {
					actionBarAlreadySend = true;
					List<LivingEntity> unnamed = new ArrayList<LivingEntity>();
					
					for (LivingEntity entity : entities) {
						if (entity.getCustomName() != null)
							continue;
						
						unnamed.add(entity);
					}
					
					for (int i = 0; (i < entities.size() - 35 && i < unnamed.size()); i++) {
						if (unnamed.get(i).getCustomName() != null)
							continue;
						
						Location loc = unnamed.get(i).getLocation();
						Biome biome = loc.getWorld().getBlockAt(loc).getBiome();
						
						if (!biome.toString().toUpperCase().contains("OCEAN"))
							continue;
						
						unnamed.get(i).remove();
					}
				}
			}
	        
			// Get entities in Range of Player
			HashMap<EntityType, List<LivingEntity>> found2 = Methods.sortEntities(Methods.getEntitiesAround(Methods.getEntities(p.getLocation().getWorld()), p.getLocation(), range));
			
			for (Entry<EntityType, List<LivingEntity>> entry2 : found2.entrySet()) {
				EntityType entType2 = entry2.getKey();
				List<LivingEntity> entities2 = entry2.getValue();
				
				if (world.getName().equalsIgnoreCase("spawninsel") && entType2.equals(EntityType.VILLAGER))
					continue;
				
				if (entities2.size() > 54 && !actionBarAlreadySend) {
					output = "&eIm Umkreis von &6" + range + " &eBlöcken sind über &455 &c" + entType2.name() + "&4! &eBitte unternimm etwas dagegen.";
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', output)));
					actionBarAlreadySend = true;
				}
			}
			
			if (actionBarAlreadySend || !plugin.monitorPlayers.contains(p) || !p.hasPermission("ctweaks.monitor"))
				continue;

			output = "&6Liquid: " + (Data.firedLiquidEvents > 399 ? "&c" : "&a") + Data.firedLiquidEvents + "/s &e- &6Spawned: &a" + (Data.firedSpawningEvents > 199 ? "&c" : "&a") + Data.firedSpawningEvents + "/s &e- &6Redstone: " + (Data.firedRedstoneEvents > 499 ? "&c" : "&a") + Data.firedRedstoneEvents + "/s &e- &6CommandBlocks: " + (Data.firedCommandBlocks > 999 ? "&c" : "&a") + Data.firedCommandBlocks + "/s";
			p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', output)));
		}
	}
}
