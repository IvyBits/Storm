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

public class WeatherListener implements Listener
{
    public static String acidRainMessage = GlobalVariables.Rain_Acid_Message__On__Acid__Rain__Start;
    public static String acidRainHurtMessage = GlobalVariables.Rain_Acid_Damager_Message__On__Player__Damaged__By__Acid__Rain;

    public static int blocksPerCalculation = GlobalVariables.Rain_Acid_Dissolver_Blocks__To__Deteriorate__Per__Calculation;

    public static int acidRainChance = GlobalVariables.Rain_Acid_Acid__Rain__Chance;
    public static int deteriorationChance = GlobalVariables.Rain_Acid_Dissolver_Block__Deterioration__Chance;

    public static Biome deteriorationBiome;
    public static Chunk chunkToDissolve;
    public static Chunk[] loadedChunk;

    public static int damagerDamage = GlobalVariables.Rain_Acid_Player_Damage__From__Exposure;

    public static int playerDamagerSchedulerId = -1;
    public static int playerDamagerDelayTicks = GlobalVariables.Rain_Acid_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks;

    public static int dissolverSchedulerId = -1;
    public static int dissolverDelayTicks = GlobalVariables.Rain_Acid_Scheduler_Dissolver__Calculation__Intervals__In__Ticks;

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

			GlobalVariables.Rain_Acid_Allowed__Worlds)) {

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
				loadedChunk = affectedWorld
					.getLoadedChunks();

				for (int blocksPerCalculationIndex = 0; blocksPerCalculationIndex <= blocksPerCalculation; ++blocksPerCalculationIndex)
				{
				    chunkToDissolve = loadedChunk[rand
					    .nextInt(loadedChunk.length)];
				    if (rand.nextInt(100) < 100)
				    {
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

					if (AcidRain.rainyBiomes
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

						    StormUtil
							    .transform(
								    toDeteriorate,
								    GlobalVariables.Rain_Acid_Dissolver_Block__Transformations);
						}

					    }

					} else
					{
					    --blocksPerCalculationIndex;
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
					    if (Storm.debug)
						System.out
							.println("Cannot damage player because they are under shelter.");
					    return;
					}

					damagee.setHealth(damagee
						.getHealth()
						- damagerDamage);

					damagee.addPotionEffect(new PotionEffect(
						PotionEffectType.HUNGER,
						rand.nextInt(600) + 300, 1));

					StormUtil.message(damagee,
						acidRainHurtMessage);

				    }
				}
			    }

			}, playerDamagerDelayTicks, playerDamagerDelayTicks);

	damagerMap.put(affectedWorld, playerDamagerSchedulerId);

    }
}
