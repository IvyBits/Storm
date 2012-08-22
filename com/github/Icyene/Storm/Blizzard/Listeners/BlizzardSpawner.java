package com.github.Icyene.Storm.Blizzard.Listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.StormUtil;
import com.github.Icyene.Storm.Blizzard.Blizzard;
import com.github.Icyene.Storm.MultiWorld.MultiWorldManager;

public class BlizzardSpawner implements Listener {

    private Random rand = new Random();
    HashMap<World, Integer> damagerMap = new HashMap<World, Integer>();
    private Storm storm;
    private int damagerScheduleId = -1;

    public BlizzardSpawner(Storm storm)
    {
	this.storm = storm;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blizzardListener(WeatherChangeEvent event)
    {

	if (event.isCancelled()) {

	    return;
	}

	final World affectedWorld = event.getWorld();

	if (event.toWeatherState()) {// gets if its set to raining

	    if (rand.nextInt(100) <= GlobalVariables.Blizzard_Blizzard__Chance) {

		// Here it checks to see if the world is enabled
		if (!MultiWorldManager.checkWorld(affectedWorld,
			GlobalVariables.Blizzard_Allowed__Worlds)) {
		    return;
		}
		Blizzard.blizzardingWorlds.put(affectedWorld, Boolean.TRUE);

		StormUtil.broadcast(GlobalVariables.Blizzard_Message__On__Blizzard__Start);

	    } else {
		return;
	    }
	}
	else if (!event.toWeatherState()) {
	    Blizzard.blizzardingWorlds.put(affectedWorld, Boolean.FALSE);
	    // Cancel damaging tasks for specific world
	    try {
		Bukkit.getScheduler().cancelTask(
			damagerMap.get(affectedWorld));
		Bukkit.getScheduler().cancelTask(damagerMap.get(affectedWorld));
	    } catch (Exception e) {

	    }
	    return;
	}

	damagerScheduleId = Bukkit.getScheduler()
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

					if(!Blizzard.snowyBiomes.contains(damagee.getLocation().getBlock().getBiome())) {
						return;
					    }

					
					damagee.setHealth(damagee
						.getHealth()
						- GlobalVariables.Blizzard_Player_Damage__From__Exposure);

					damagee.addPotionEffect(new PotionEffect(
						PotionEffectType.BLINDNESS,
						GlobalVariables.Blizzard_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks,
						GlobalVariables.Blizzard_Damager_Blindness__Amplitude));

					StormUtil
						.message(
							damagee,
							GlobalVariables.Blizzard_Damager_Message__On__Player__Damaged__Cold);

				    }
				}
			    }

			}
			,
			GlobalVariables.Blizzard_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks
			,
			GlobalVariables.Blizzard_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks
		);

	damagerMap.put(affectedWorld, damagerScheduleId);

    }
}
