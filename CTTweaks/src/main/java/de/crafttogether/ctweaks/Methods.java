package de.crafttogether.ctweaks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Methods {
    public static HashMap<EntityType, List<LivingEntity>> sortEntities(List<LivingEntity> entities) {
    	HashMap<EntityType, List<LivingEntity>> sorted = new HashMap<EntityType, List<LivingEntity>>();
    	
		for (LivingEntity entity : entities) {			
    		if (entity.getType().equals(EntityType.PLAYER) || entity.getType().equals(EntityType.ARMOR_STAND))
    			continue;
			
			if (!sorted.containsKey(entity.getType()))
				sorted.put(entity.getType(), new ArrayList<LivingEntity>());
				
			sorted.get(entity.getType()).add(entity);
		}
		
		return sorted;
	}

    public static List<LivingEntity> getEntitiesAround(List<LivingEntity> entities, Location loc, int range) {
    	List<LivingEntity> found = new ArrayList<LivingEntity>();
    	
    	int locX = ((int) loc.getX());
    	int locY = ((int) loc.getY());
    	int locZ = ((int) loc.getZ());
    	
		int startX = locX - range;
		int startY = locY - range;
		int startZ = locZ - range;
		
		int endX = locX + range + 1;
		int endY = locY + range + 1;
		int endZ = locZ + range + 1;
		
    	for (LivingEntity entity : entities) {
    		Location entLoc = entity.getLocation();
    		EntityType entName = entity.getType();
    		
    		if (!loc.getWorld().equals(entLoc.getWorld()) ||entName.equals(EntityType.PLAYER) || entName.equals(EntityType.ARMOR_STAND))
    			continue;
    		
    		int x = (int) entLoc.getX();
    		int y = (int) entLoc.getY();
    		int z = (int) entLoc.getZ();
    		
    		Boolean xMatch = (x > startX-1) && (x < endX+1);
    		Boolean yMatch = (y > startY-1) && (y < endY+1);
    		Boolean zMatch = (z > startZ-1) && (z < endZ+1);
    		
    		if (xMatch && yMatch && zMatch)
    			found.add(entity);
    	}
    	
    	return found;
	}
    
	public static List<Player> getPlayersAround(Location loc, int range) {
		List<Player> found = new ArrayList<Player>();
    	
    	int locX = ((int) loc.getX());
    	int locY = ((int) loc.getY());
    	int locZ = ((int) loc.getZ());
    	
		int startX = locX - range;
		int startY = locY - range;
		int startZ = locZ - range;
		
		int endX = locX + range + 1;
		int endY = locY + range + 1;
		int endZ = locZ + range + 1;
		
    	for (Player player : Bukkit.getServer().getOnlinePlayers()) {
    		Location pLoc = player.getLocation();
    		
    		if (!loc.getWorld().equals(pLoc.getWorld()))
    			continue;
    		
    		int x = (int) pLoc.getX();
    		int y = (int) pLoc.getY();
    		int z = (int) pLoc.getZ();
    		
    		Boolean xMatch = (x > startX-1) && (x < endX+1);
    		Boolean yMatch = (y > startY-1) && (y < endY+1);
    		Boolean zMatch = (z > startZ-1) && (z < endZ+1);
    		
    		if (xMatch && yMatch && zMatch)
    			found.add(player);
    	}
    	
    	return found;
	}
    
    public static List<LivingEntity> getEntities() {
    	List<LivingEntity> entities = new ArrayList<LivingEntity>();
    	
    	for (World world : Data.livingEntities.keySet())
    		entities.addAll(Data.livingEntities.get(world));
    	
    	return entities;
    }
    
    public static List<LivingEntity> getEntities(World world) {
    	List<LivingEntity> entities = new ArrayList<LivingEntity>();
    	
    	if (!Data.livingEntities.containsKey(world))
    		return entities;
    	
    	for (LivingEntity entity : Data.livingEntities.get(world)) {
    		if (entity.getWorld().equals(world))
    			entities.add(entity);
    	}
    	
    	return entities;
    }
    
    public static List<LivingEntity> getEntities(World world, EntityType type) {
    	List<LivingEntity> entities = new ArrayList<LivingEntity>();
    	
    	if (!Data.livingEntities.containsKey(world))
    		return entities;
    	
    	for (LivingEntity entity : Data.livingEntities.get(world)) {
    		if (entity.getWorld().equals(world) && entity.getType().equals(type))
    			entities.add(entity);
    	}
    	
    	return entities;
    }
    
    public static Location getTopBlockLocation(Location loc) {
        loc.setY(loc.getWorld().getHighestBlockYAt((int) loc.getX(), (int) loc.getZ()));
        return loc;
    }
}
