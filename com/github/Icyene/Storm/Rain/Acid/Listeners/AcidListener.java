package com.github.Icyene.Storm.Rain.Acid.Listeners;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.*;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Events.AcidRainEvent;
import com.github.Icyene.Storm.MultiWorld.MultiWorldManager;
import com.github.Icyene.Storm.Rain.Acid.AcidRain;

public class AcidListener implements Listener
{

    private static Biome deteriorationBiome;
    private static Chunk chunkToDissolve;
    private static Chunk[] loadedChunk;
    private static int playerDamagerSchedulerId,
	    dissolverSchedulerId = playerDamagerSchedulerId = -1;
    private static final Random rand = new Random();
    private static Block toDeteriorate;
    private static int x, z = 0;
    private static final int y = 4;
    private Storm storm;

    HashMap<World, Integer> dissolverMap = new HashMap<World, Integer>();
    HashMap<World, Integer> damagerMap = new HashMap<World, Integer>();

    public AcidListener(Storm storm)
    {
	this.storm = storm;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void acidicWeatherListener(WeatherChangeEvent event)
    {
	if (event.isCancelled()) {
	    return;
	}

	final World affectedWorld = event.getWorld();

	if (event.toWeatherState()) {// gets if its set to raining

	    if (rand.nextInt(100) <= Storm.config.Acid__Rain_Acid__Rain__Chance) {

		if (!MultiWorldManager.checkWorld(affectedWorld,

			Storm.config.Acid__Rain_Allowed__Worlds)) {

		    return;
		}
		AcidRain.acidicWorlds.remove(affectedWorld);
		AcidRain.acidicWorlds.put(affectedWorld, Boolean.TRUE);

		AcidRainEvent startEvent = new AcidRainEvent(affectedWorld,
			true);
		Bukkit.getServer().getPluginManager().callEvent(startEvent);

		if (startEvent.isCancelled()) {
		    return;
		}

		Storm.util.broadcast(Storm.config.Acid__Rain_Message__On__Acid__Rain__Start);

	    } else {
		return;
	    }
	}
	else if (!event.toWeatherState()) {

	    AcidRain.acidicWorlds.remove(affectedWorld);
	    AcidRain.acidicWorlds.put(affectedWorld, Boolean.FALSE);

	    // Cancel damaging tasks for specific world
	    try {

		AcidRainEvent startEvent = new AcidRainEvent(affectedWorld,
			false);
		Bukkit.getServer().getPluginManager().callEvent(startEvent);

		Bukkit.getScheduler().cancelTask(
			dissolverMap.get(affectedWorld));
		Bukkit.getScheduler().cancelTask(damagerMap.get(affectedWorld));

		Bukkit.getScheduler().cancelTask(
			dissolverMap.get(affectedWorld));
		Bukkit.getScheduler().cancelTask(
			dissolverMap.get(affectedWorld));
	    } catch (Exception e) {

	    }
	    return;
	}

	// Dissolver
	if (Storm.config.Features_Acid__Rain_Dissolving__Blocks) {
	    dissolverSchedulerId = Bukkit.getScheduler()
		    .scheduleSyncRepeatingTask(
			    storm,
			    new Runnable()
			    {
				@Override
				public void run()
				{
				    loadedChunk = affectedWorld
					    .getLoadedChunks();

				    for (int blocksPerCalculationIndex = 0; blocksPerCalculationIndex <= Storm.config.Acid__Rain_Dissolver_Blocks__To__Deteriorate__Per__Calculation; ++blocksPerCalculationIndex)
				    {
					chunkToDissolve = loadedChunk[rand
						.nextInt(loadedChunk.length)];
					if (rand.nextInt(100) < 100)
					{
					    x = rand.nextInt(16);
					    z = rand.nextInt(16);

					    toDeteriorate = affectedWorld
						    .getHighestBlockAt(
							    chunkToDissolve
								    .getBlock(
									    x,
									    y,
									    z)
								    .getLocation())
						    .getLocation()
						    .subtract(0, 1, 0)
						    .getBlock();

					    deteriorationBiome = toDeteriorate
						    .getBiome();

					    if (AcidRain.rainyBiomes
						    .contains(deteriorationBiome))
					    {

						if (rand.nextInt(100) < Storm.config.Acid__Rain_Dissolver_Block__Deterioration__Chance)
						{
						    if (toDeteriorate
							    .getTypeId() != 0)
						    {

							Storm.util
								.transform(
									toDeteriorate,
									Storm.config.Acid__Rain_Dissolver_Block__Transformations);
						    }

						}

					    } else
					    {
						--blocksPerCalculationIndex;
					    }

					}
				    }

				}
			    },
			    0,
			    Storm.config.Acid__Rain_Scheduler_Dissolver__Calculation__Intervals__In__Ticks);

	    dissolverMap.put(affectedWorld, dissolverSchedulerId);
	}

	if (Storm.config.Features_Acid__Rain_Player__Damaging) {
	    playerDamagerSchedulerId = Bukkit.getScheduler()
		    .scheduleSyncRepeatingTask(
			    storm,
			    new Runnable()
			    {
				@Override
				public void run()
				{

				    for (Player damagee : affectedWorld
					    .getPlayers())
				    {
					if (!damagee.getGameMode().equals(
						GameMode.CREATIVE))
					{

					    if (!Storm.util
						    .isPlayerInRain(damagee)) {
						return;
					    } else {

					    }

					    damagee.damage(Storm.config.Acid__Rain_Player_Damage__From__Exposure*2);

					    damagee.addPotionEffect(new PotionEffect(
						    PotionEffectType.HUNGER,
						    rand.nextInt(600) + 300, 1));

					    Storm.util
						    .message(
							    damagee,
							    Storm.config.Acid__Rain_Damager_Message__On__Player__Damaged__By__Acid__Rain);

					}
				    }
				}

			    },
			    Storm.config.Acid__Rain_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks,
			    Storm.config.Acid__Rain_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks);

	    damagerMap.put(affectedWorld, playerDamagerSchedulerId);
	}

    }
}
