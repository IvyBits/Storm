package com.github.Icyene.Storm.Blizzard.Listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Blizzard.Blizzard;
import com.github.Icyene.Storm.Blizzard.Events.BlizzardEvent;
import com.github.Icyene.Storm.Blizzard.Tasks.BlizzardTask;

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

	GlobalVariables glob = Storm.wConfigs.get(affectedWorld.getName());

	if (event.toWeatherState()) {// gets if its set to raining

	    if (rand.nextInt(100) <= glob.Blizzard_Blizzard__Chance) {

		// Here it checks to see if the world is enabled
		if (!glob.Features_Blizzards_Player__Damaging
			&& !glob.Features_Blizzards_Slowing__Snow) {
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

		for (Player p : event.getWorld().getPlayers()) {
		    Storm.util
			    .message(
				    p,
				    glob.Blizzard_Message__On__Blizzard__Start);
		}

	    } else {
		return;
	    }
	}
	else if (!event.toWeatherState()) {
	    Blizzard.blizzardingWorlds.remove(affectedWorld);
	    Blizzard.blizzardingWorlds.put(affectedWorld, Boolean.FALSE);
	    // Cancel damaging tasks for specific world

	    try {
		damagerMap.get(affectedWorld).stop();
	    } catch (Exception e) {
	    }
	    ;

	    BlizzardEvent startEvent = new BlizzardEvent(
		    affectedWorld, false);
	    Bukkit.getServer().getPluginManager().callEvent(startEvent);

	    return;
	}

	if (glob.Features_Blizzards_Player__Damaging) {
	    final BlizzardTask bliz = new BlizzardTask(storm, affectedWorld);
	    damagerMap.put(affectedWorld, bliz);
	    bliz.run();
	}

    }

}
