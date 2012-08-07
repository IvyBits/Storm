/*
 * Storm
 * Copyright (C) 2012 Icyene
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
import com.github.Icyene.Storm.Transformations.BlockChanger;

public class WeatherListener implements Listener
{
    public static String acidRainMessage = GlobalVariables.rain_acid_acidRainMessage;
    public static String acidRainPoisonMessage = GlobalVariables.rain_acid_damager_acidRainPoisonMessage;
    public static String acidRainHurtMessage = GlobalVariables.rain_acid_damager_acidRainHurtMessage;

    public static int blocksPerChunk = GlobalVariables.rain_acid_dissolver_blocksPerChunk;
    public static int chunkDissolveChance = GlobalVariables.rain_acid_dissolver_chunkDissolveChance;
    public static int chunksToCalculate = GlobalVariables.rain_acid_dissolver_chunksToCalculate;

    public static int acidRainChance = GlobalVariables.rain_acid_acidRainChance;
    public static int deteriorationChance = GlobalVariables.rain_acid_dissolver_deteriorationChance;
    public static int playerDamageChance = GlobalVariables.rain_acid_damager_playerDamageChance;
    public static int playerHungerChance = GlobalVariables.rain_acid_damager_playerHungerChance;

    public static Biome deteriorationBiome;
    public static Chunk chunkToDissolve;
    public static Chunk[] loadedChunk;

    public static int damagerDamage = GlobalVariables.rain_acid_damager_damagerDamage;
    public static int hungerTicks = GlobalVariables.rain_acid_damager_hungerTicks;
    public static boolean getHungry = GlobalVariables.rain_acid_damager_getHungry;

    public static int playerDamagerSchedulerId = -1;
    public static int playerDamagerDelayTicks = GlobalVariables.rain_acid_scheduler_playerDamagerDelayTicks;

    public static int dissolverSchedulerId = -1;
    public static int dissolverDelayTicks = GlobalVariables.rain_acid_scheduler_dissolverDelayTicks;

    public static final Random rand = new Random();
    public static Block toDeteriorate;
    public static Block highestBlock;

    public static Location damageeLocation;

    public static int x, z = 0;

    public static final int y = 4;

    private Storm storm;

    public List<Chunk> chunksAffected = new ArrayList<Chunk>();

    HashMap<World, Integer> dissolverMap = new HashMap<World, Integer>();
    HashMap<World, Integer> damagerMap = new HashMap<World, Integer>();

    public WeatherListener(Storm storm)
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

	    if (rand.nextInt(100) <= acidRainChance) {

		if (!MultiWorldManager.checkWorld(affectedWorld,
			GlobalVariables.rain_acid_allowedWorlds)) {
		    System.out
			    .println("World not enabled in config for acid rain.");
		    return;
		}
		AcidRain.acidicWorlds.put(affectedWorld, Boolean.TRUE);

		StormUtil.broadcast(acidRainMessage);

	    }
	}
	else if (!event.toWeatherState()) {

	    AcidRain.acidicWorlds.put(affectedWorld, Boolean.FALSE);

	    // Cancel damaging tasks for specific world
	    try {
		Bukkit.getScheduler().cancelTask(
			dissolverMap.get(affectedWorld));
		Bukkit.getScheduler().cancelTask(damagerMap.get(affectedWorld));
		StormUtil.log("Cleared schedulers.");
	    } catch (Exception e) {

	    }
	    return;
	}

	// Dissolver
	dissolverSchedulerId = Bukkit.getScheduler()
		.scheduleSyncRepeatingTask(storm,
			new Runnable()
			{
			    @Override
			    public void run()
			    {
				if (Storm.debug)
				    System.out
					    .println("Reloaded chunks into memory.");
				loadedChunk = affectedWorld
					.getLoadedChunks();

				for (int chunkCalculationIndex = 0; chunkCalculationIndex <= chunksToCalculate; ++chunkCalculationIndex)
				{
				    chunkToDissolve = loadedChunk[rand
					    .nextInt(loadedChunk.length)];
				    if (rand.nextInt(100) < chunkDissolveChance)
				    {
					for (int blocksPerChunkIndex = 0; blocksPerChunkIndex <= blocksPerChunk; ++blocksPerChunkIndex)
					{
					    if (Storm.debug)
						System.out
							.println("Block for chunk "
								+ chunkToDissolve
								+ ". Index: "
								+ blocksPerChunkIndex);
					    x = rand.nextInt(16);
					    z = rand.nextInt(16);
					    if (Storm.debug)
						System.out.println("X: "
							+ x
							+ " Z: " + z);

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

					    if (AcidRain.acidRain
						    && AcidRain.rainyBiomes
							    .contains(deteriorationBiome)
						    || AcidRain.acidSnow
						    && AcidRain.snowyBiomes
							    .contains(deteriorationBiome))
					    {

						if (rand.nextInt(100) < deteriorationChance)
						{
						    if (Storm.debug)
							System.out
								.println(toDeteriorate);

						    if (toDeteriorate
							    .getTypeId() != 0)
						    {
							if (Storm.debug)
							    System.out
								    .println("Pushed block to deteriorator: "
									    + toDeteriorate);
							BlockChanger
								.transform(
									toDeteriorate,
									GlobalVariables.rain_acid_dissolver_blockTransformations);
						    }

						}

					    } else
					    {
						if (Storm.debug)
						    System.out
							    .println("Invalid biome for block.");
						return;
					    }

					}
				    }
				}
			    }
			}, 0, dissolverDelayTicks);

	dissolverMap.put(affectedWorld, dissolverSchedulerId);

	playerDamagerSchedulerId = Bukkit.getScheduler()
		.scheduleSyncRepeatingTask(storm,
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
					    && rand.nextInt(100) < playerDamageChance)
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
					    if (Storm.debug)
						System.out
							.println("Cannot damage player because they are under shelter.");
					    return;
					}

					damagee.setHealth(damagee
						.getHealth()
						- damagerDamage);
					if (getHungry
						&& rand.nextInt(100) < playerHungerChance)
					{

					    damagee.addPotionEffect(new PotionEffect(
						    PotionEffectType.HUNGER,
						    hungerTicks, 1));
					    StormUtil.message(damagee, acidRainPoisonMessage);

					} else
					{
					    StormUtil.message(damagee, acidRainHurtMessage);
					}

				    }
				}
			    }

			}, playerDamagerDelayTicks, playerDamagerDelayTicks);

	damagerMap.put(affectedWorld, playerDamagerSchedulerId);

    }
}
