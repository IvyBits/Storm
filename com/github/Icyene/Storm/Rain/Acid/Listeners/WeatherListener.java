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
import com.github.Icyene.Storm.Rain.Acid.AcidRain;

public class WeatherListener implements Listener
{
    public static String acidRainMessage = "Acid has started to fall from the sky!";
    public static String acidRainPoisonMessage = "You have been damaged and poisoned by the acidic downfall!";
    public static String acidRainHurtMessage = "You have been damaged by the acidic downfall!";

    public static int blocksPerChunk = 10;
    public static int chunkDissolveChance = 100;
    public static int chunksToCalculate = 4;

    public static int acidRainChance = 100;
    public static int deteriorationChance = 100;
    public static int playerDamageChance = 100;
    public static int playerHungerChance = 100;
    public static int entityDamageChance = 100;

    public static Biome deteriorationBiome;
    public static Chunk chunkToDissolve;
    public static Chunk[] loadedChunk;

    public static int damagerDamage = 2;
    public static int hungerTicks = 300;
    public static boolean getHungry = true;

    public static int playerDamagerSchedulerId = -1;
    public static int playerDamagerDelayTicks = 500;

    public static int dissolverSchedulerId = -1;
    public static int dissolverDelayTicks = 100;

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
	final World affectedWorld = event.getWorld();

	if ((event.toWeatherState())) {

	    if (rand.nextInt(100) <= acidRainChance) {

		AcidRain.acidicWorlds.put(affectedWorld, Boolean.TRUE);

		storm.getServer().broadcastMessage(
			ChatColor.GRAY + acidRainMessage);

		// Has acid rain

	    }
	}
	else if (!event.toWeatherState()) {
	    AcidRain.acidicWorlds.put(affectedWorld, Boolean.FALSE);

	    // Cancel damaging tasks for specific world
	    try {
		Bukkit.getScheduler().cancelTask(
			dissolverMap.get(affectedWorld));
		Bukkit.getScheduler().cancelTask(damagerMap.get(affectedWorld));
		System.out.println("Cleared schedulers.");
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

					    } else
					    {
						if (Storm.debug)
						    System.out
							    .println("Invalid biome for block.");
						return;
					    }

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
						    AcidRain.dissolve(toDeteriorate);
						}

					    }
					}
				    }
				}
			    }
			}, 0, dissolverDelayTicks);

	dissolverMap.put(affectedWorld, dissolverSchedulerId);

	// DISSOLVER END

	/*
	 * 
	 * This thread takes charge of hurting players.
	 * 
	 * DAMAGER START
	 */

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
					    damagee.sendMessage(ChatColor.GRAY
						    + acidRainPoisonMessage);

					} else
					{
					    damagee.sendMessage(ChatColor.GRAY
						    + acidRainHurtMessage);
					}

				    }
				}
			    }

			}, playerDamagerDelayTicks, playerDamagerDelayTicks);

	damagerMap.put(affectedWorld, playerDamagerSchedulerId);

    }
}
