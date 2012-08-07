package com.github.Icyene.Storm.Rain.Acid.Listeners;

import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Rain.Acid.AcidRain;

public class FormListener implements Listener
{
    public FormListener()
    {
    }

    @EventHandler
    public static void acidicFormListener(BlockFormEvent form)
    {
	if (form.isCancelled())
	    return;

	final Biome eventBiome = form.getBlock().getBiome();

	if (AcidRain.acidicWorlds.containsKey(form.getBlock().getWorld())
		&& AcidRain.acidSnow
		&& AcidRain.snowyBiomes.contains(eventBiome)
		|| AcidRain.acidRain
		&& AcidRain.rainyBiomes.contains(eventBiome))
	{
	    if (Storm.debug)
		System.out.println("Cancelled snow forming event.");
	    form.setCancelled(true);
	}

    }
}
