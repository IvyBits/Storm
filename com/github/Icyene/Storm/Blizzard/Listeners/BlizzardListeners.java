package com.github.Icyene.Storm.Blizzard.Listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.github.Icyene.Storm.MultiWorldManager;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Blizzard.Blizzard;
import com.github.Icyene.Storm.Blizzard.Tasks.BlizzardTask;
import com.github.Icyene.Storm.Events.BlizzardEvent;

public class BlizzardListeners implements Listener {

    private Random rand = new Random();
    public static HashMap<World, BlizzardTask> damagerMap = new HashMap<World, BlizzardTask>();
    private Storm storm;

    public BlizzardListeners(Storm storm)
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

	    if (rand.nextInt(100) <= Storm.config.Blizzard_Blizzard__Chance) {

		// Here it checks to see if the world is enabled
		if (!MultiWorldManager.checkWorld(affectedWorld,
			Storm.config.Blizzard_Allowed__Worlds)) {
		    return;
		}
		Blizzard.blizzardingWorlds.remove(affectedWorld);
		Blizzard.blizzardingWorlds.put(affectedWorld, Boolean.TRUE);

		BlizzardEvent startEvent = new BlizzardEvent(
			affectedWorld, true);
		Bukkit.getServer().getPluginManager().callEvent(startEvent);

		if (startEvent.isCancelled()) {
		    return;
		}

		Storm.util
			.broadcast(Storm.config.Blizzard_Message__On__Blizzard__Start);

	    } else {
		return;
	    }
	}
	else if (!event.toWeatherState()) {
	    Blizzard.blizzardingWorlds.remove(affectedWorld);
	    Blizzard.blizzardingWorlds.put(affectedWorld, Boolean.FALSE);
	    // Cancel damaging tasks for specific world

	    damagerMap.get(affectedWorld).stop();

	    BlizzardEvent startEvent = new BlizzardEvent(
		    affectedWorld, false);
	    Bukkit.getServer().getPluginManager().callEvent(startEvent);

	    return;
	}

	final BlizzardTask bliz = new BlizzardTask(storm, affectedWorld);
	damagerMap.put(affectedWorld, bliz);
	bliz.run();

    }
}
