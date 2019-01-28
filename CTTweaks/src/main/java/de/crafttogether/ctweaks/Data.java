package de.crafttogether.ctweaks;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public class Data {
	public static ConcurrentHashMap<World, List<LivingEntity>> livingEntities = new ConcurrentHashMap<World, List<LivingEntity>>();
	
	public static int firedLiquidEvents = 0;
	public static int firedSpawningEvents = 0;
	public static int firedRedstoneEvents = 0;
	public static int firedCommandBlocks = 0;
}
