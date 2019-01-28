package de.crafttogether.ctweaks;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.crafttogether.CTweaks;

public class Events implements Listener
{
    private CTweaks plugin;
    
    public Events() {
        this.plugin = CTweaks.getInstance();
    }
    
    @EventHandler
    public void onBlockFromTo(BlockFromToEvent e) {
    	if (Config.isLiquidBlocked)
    		e.setCancelled(true);
    	else
    		plugin.getMonitor().firedLiquidEvents++;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent e) {
    	World world = e.getEntity().getLocation().getWorld();
    	if (e.getEntityType().equals(EntityType.ENDER_DRAGON) && world.getName().toLowerCase().contains("the_end")) {
    		new BukkitRunnable() {
    			public void run() {
    	    		Location loc = Methods.getTopBlockLocation(new Location(world, 0, 0, 0));
	    	    	loc.setY(loc.getY()+1);
	    	    	Block block = world.getBlockAt(loc);
	    	    	block.setType(Material.DRAGON_EGG);
	    	    	this.cancel();
    			}
    		}.runTaskLater(plugin, 20L*20);
    	}
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent e)
    {
    	Entity ent = e.getEntity();
    	EntityType entType = ent.getType();
    	SpawnReason reason = e.getSpawnReason();

	  	if (Config.isSpawningBlocked) {
	  		e.setCancelled(true);
	  		return;
	  	}	  	

	  	// Reduce fishy spawnings
	  	if (entType.equals(EntityType.COD) || entType.equals(EntityType.TROPICAL_FISH) || entType.equals(EntityType.SALMON)) {
	  		List<LivingEntity> found = Methods.getEntitiesAround(Methods.getEntities(ent.getWorld(), entType), ent.getLocation(), 192);
	  		if (found.size() > 40 && reason.equals(SpawnReason.NATURAL) || reason.equals(SpawnReason.DEFAULT)) {
	  			e.setCancelled(true);
	  			return;
	  		}
	  	}	  	

	  	// Recude spawning of 
	  	if (entType.equals(EntityType.GUARDIAN) || entType.equals(EntityType.ENDERMAN) || entType.equals(EntityType.PIG_ZOMBIE)) {
	  		List<LivingEntity> found = Methods.getEntitiesAround(Methods.getEntities(ent.getWorld(), entType), ent.getLocation(), 128);
	  		if (found.size() > 40) {
	  			for (int i = 41; i > found.size(); i++)
	  				found.get(i).remove();
	  			
	  			if (reason.equals(SpawnReason.NATURAL) || reason.equals(SpawnReason.DEFAULT) || reason.equals(SpawnReason.NETHER_PORTAL)) {
	  				e.setCancelled(true);
	  				return;
	  			}
	  		}
	  	}
	  	
		if (!e.getLocation().getWorld().getName().equalsIgnoreCase("spawninsel")) {
			int range = 64;
			HashMap<EntityType, List<LivingEntity>> found = Methods.sortEntities(Methods.getEntitiesAround(Methods.getEntities(ent.getWorld()), ent.getLocation(), range));
		  	
		  	if (
	  			found.containsKey(entType)
	  			&& found.get(entType).size() > 49
	  			&& !reason.equals(SpawnReason.CURED)
	  			&& !reason.equals(SpawnReason.CUSTOM)
	  			&& !reason.equals(SpawnReason.DROWNED)
	  			&& !reason.equals(SpawnReason.INFECTION)
	  			&& !reason.equals(SpawnReason.SHEARED)
		  	) {
		  		e.setCancelled(true);
		  		
		  		if (
		  			reason.equals(SpawnReason.BREEDING)
			  		|| reason.equals(SpawnReason.BUILD_IRONGOLEM)
			  		|| reason.equals(SpawnReason.BUILD_SNOWMAN)
			  		|| reason.equals(SpawnReason.BUILD_WITHER)
			  		|| reason.equals(SpawnReason.EGG)
			  		|| reason.equals(SpawnReason.SPAWNER_EGG)
			  		|| reason.equals(SpawnReason.SPAWNER)
		  		) {
		  			int radius = 8;
		  			
		  			if (reason.equals(SpawnReason.SPAWNER))
		  				radius = 16;
		  			
		  			if (reason.equals(SpawnReason.EGG) || reason.equals(SpawnReason.BREEDING))
		  				radius = 32;
		  			
		  			List<Player> players = Methods.getPlayersAround(ent.getLocation(), radius);
		  			
		  			for (Player p : players) {
		  				if (reason.equals(SpawnReason.BREEDING))
		  					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEs konnte(n) kein(e) &e" + entType.name() + " &cgeboren werden, weil es schon zu viele in der Umgebung gibt."));
		  				else
		  					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEs konnte(n) kein(e) &e" + entType.name() + " &cgespawned werden, weil es schon zu viele in der Umgebung gibt."));
		  			}
			  	}		  		
		  		
		  		return;
		  	}
		}
	  	
	  	plugin.getMonitor().firedSpawningEvents++;
    }
    

	@EventHandler
    public void onRedstone(BlockRedstoneEvent e) {    	
    	if (Config.isRedstoneBlocked) {
    		e.setNewCurrent(0);
    		return;
		}
    	
    	plugin.getMonitor().firedRedstoneEvents++;
    }
    
    @EventHandler
    public void onCommandBlock(ServerCommandEvent e) {
        if(e.getSender() instanceof BlockCommandSender) {
        	if (Config.isCommandBlockBlocked)
        		e.setCancelled(true);
        	else
        		plugin.getMonitor().firedCommandBlocks++;
        }
    }
}
