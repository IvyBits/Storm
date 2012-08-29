package com.github.Icyene.Storm.Acid_Rain.Listeners;

import java.util.*;

import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import com.github.Icyene.Storm.MultiWorldManager;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Acid_Rain.AcidRain;
import com.github.Icyene.Storm.Acid_Rain.Tasks.DamagerTask;
import com.github.Icyene.Storm.Acid_Rain.Tasks.DissolverTask;
import com.github.Icyene.Storm.Events.AcidRainEvent;

public class AcidListener implements Listener
{

    private static final Random rand = new Random();

    public static HashMap<World, DissolverTask> dissolverMap = new HashMap<World, DissolverTask>();
    public static HashMap<World, DamagerTask> damagerMap = new HashMap<World, DamagerTask>();

    private Storm storm;

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

		Storm.util
			.broadcast(Storm.config.Acid__Rain_Message__On__Acid__Rain__Start);

	    } else {
		return;
	    }
	}
	else if (!event.toWeatherState()) {

	    AcidRain.acidicWorlds.remove(affectedWorld);
	    AcidRain.acidicWorlds.put(affectedWorld, Boolean.FALSE);

	    // Cancel damaging tasks for specific world

	    AcidRainEvent startEvent = new AcidRainEvent(affectedWorld,
		    false);
	    Bukkit.getServer().getPluginManager().callEvent(startEvent);

	    dissolverMap.get(affectedWorld).stop();
	    damagerMap.get(affectedWorld).stop();

	    return;
	}

	if (Storm.config.Features_Acid__Rain_Dissolving__Blocks) {
	    final DissolverTask dis = new DissolverTask(storm, affectedWorld);
	    dissolverMap.put(affectedWorld, new DissolverTask(storm,
		    affectedWorld));
	    dis.run();
	}

	if (Storm.config.Features_Acid__Rain_Player__Damaging) {
	    final DamagerTask dam = new DamagerTask(storm, affectedWorld);
	    damagerMap.put(affectedWorld, dam);
	    dam.run();
	}

    }
}
