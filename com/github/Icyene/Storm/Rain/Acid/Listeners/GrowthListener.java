package com.github.Icyene.Storm.Rain.Acid.Listeners;

import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Rain.Acid.AcidRain;

public class GrowthListener implements Listener
{

    public GrowthListener()
    {
    }

    @EventHandler
    public static void acidicGrowthListener(BlockGrowEvent growth)
    {
	if (growth.isCancelled())
	    return;

	
	final Biome eventBiome = growth.getBlock().getBiome();

	if (AcidRain.acidicWorlds.containsKey(growth.getBlock().getWorld())
		
		&& AcidRain.acidSnow
		&& AcidRain.snowyBiomes.contains(eventBiome)
		|| AcidRain.acidRain
		&& AcidRain.rainyBiomes.contains(eventBiome))
	{
	    if (Storm.debug)
		System.out.println("Cancelled block growing event.");
	    growth.setCancelled(true);
	}

    }
}
