package com.github.Icyene.Storm.Snow.Piling.Listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.StormUtil;
import com.github.Icyene.Storm.MultiWorld.MultiWorldManager;

public class WeatherListener {
    public Storm storm;
    
    public static int pilerDelayTicks = 10;

    public static int pilerScheduleId = -1;
    HashMap<World, Integer> pilerMap = new HashMap<World, Integer>();
    
    public WeatherListener(Storm sStorm) {
	storm = sStorm;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void pilingWeatherListener(WeatherChangeEvent event)
    {

	if (event.isCancelled()) {
	    return;
	}

	final World affectedWorld = event.getWorld();

	if (event.toWeatherState()) {// gets if its set to raining

	
	    if (!MultiWorldManager.checkWorld(affectedWorld,
			GlobalVariables.snow_piling_allowedWorlds)) {
		    System.out.println("World not enabled in config for snow pileup.");
		    return;
		}
	    
	    //ITS RAINING, CONTINUE
	}
	else if (!event.toWeatherState()) {

	    // Cancel piling tasks for specific world
	    try {
		Bukkit.getScheduler().cancelTask(
			pilerMap.get(affectedWorld));
		Bukkit.getScheduler().cancelTask(pilerMap.get(affectedWorld));
		StormUtil.log("Cleared schedulers.");
	    } catch (Exception e) {

	    }
	    return;
	}

	// Piler
	int pilerScheduleId = Bukkit.getScheduler()
		.scheduleSyncRepeatingTask(storm,
			new Runnable()
			{
			    @Override
			    public void run()
			    {
				//TODO PILE TEH SNOWZ
			    }
			    
			}, 0, pilerDelayTicks);
	
	pilerMap.put(affectedWorld, pilerScheduleId);	    
}
}