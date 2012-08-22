package com.github.Icyene.Storm.Rain.Acid.Listeners;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.*;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.StormUtil;
import com.github.Icyene.Storm.MultiWorld.MultiWorldManager;
import com.github.Icyene.Storm.Rain.Acid.AcidRain;

public class AcidListener implements Listener
{

    private static Biome deteriorationBiome;
    private static Chunk chunkToDissolve;
    private static Chunk[] loadedChunk;
    private static int playerDamagerSchedulerId = -1;
    private static int dissolverSchedulerId = -1;
    private static final Random rand = new Random();
    private static Block toDeteriorate;
    private static Block highestBlock;
    private static Location damageeLocation;
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

	    if (rand.nextInt(100) <= GlobalVariables.Acid__Rain_Acid__Rain__Chance) {

		if (!MultiWorldManager.checkWorld(affectedWorld,

			GlobalVariables.Acid__Rain_Allowed__Worlds)) {

		    return;
		}
		AcidRain.acidicWorlds.put(affectedWorld, Boolean.TRUE);

		StormUtil
			.broadcast(GlobalVariables.Acid__Rain_Message__On__Acid__Rain__Start);

	    } else {
		return;
	    }
	}
	else if (!event.toWeatherState()) {

	    AcidRain.acidicWorlds.put(affectedWorld, Boolean.FALSE);

	    // Cancel damaging tasks for specific world
	    try {
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
	if (GlobalVariables.Features_Rain_Acid_Dissolving__Blocks) {
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

				    for (int blocksPerCalculationIndex = 0; blocksPerCalculationIndex <= GlobalVariables.Acid__Rain_Dissolver_Blocks__To__Deteriorate__Per__Calculation; ++blocksPerCalculationIndex)
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

						if (rand.nextInt(100) < GlobalVariables.Acid__Rain_Dissolver_Block__Deterioration__Chance)
						{
						    if (toDeteriorate
							    .getTypeId() != 0)
						    {

							StormUtil
								.transform(
									toDeteriorate,
									GlobalVariables.Acid__Rain_Dissolver_Block__Transformations);
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
			    GlobalVariables.Acid__Rain_Scheduler_Dissolver__Calculation__Intervals__In__Ticks);

	    dissolverMap.put(affectedWorld, dissolverSchedulerId);
	}

	if (GlobalVariables.Features_Rain_Acid_Player__Damaging) {
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
						GameMode.CREATIVE)
					)
					{

					    damageeLocation = damagee
						    .getLocation();

					    highestBlock = affectedWorld
						    .getHighestBlockAt(
							    (int) damageeLocation
								    .getX(),
							    (int) damageeLocation
								    .getZ())
						    .getLocation()
						    .subtract(0, 1, 0)
						    .getBlock();
					    if (highestBlock.getType() != Material.AIR)
					    {

						return;
					    }

					    damagee.setHealth(damagee
						    .getHealth()
						    - GlobalVariables.Acid__Rain_Player_Damage__From__Exposure);

					    damagee.addPotionEffect(new PotionEffect(
						    PotionEffectType.HUNGER,
						    rand.nextInt(600) + 300, 1));

					    StormUtil
						    .message(
							    damagee,
							    GlobalVariables.Acid__Rain_Damager_Message__On__Player__Damaged__By__Acid__Rain);

					}
				    }
				}

			    },
			    GlobalVariables.Acid__Rain_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks,
			    GlobalVariables.Acid__Rain_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks);

	    damagerMap.put(affectedWorld, playerDamagerSchedulerId);
	}

    }
}
