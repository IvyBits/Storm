package com.github.Icyene.Storm.Rain.Puddles.Listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.github.Icyene.Storm.Storm;

public class WeatherListener implements Listener {

    public static int puddleSchedulerId;
    Storm storm;

    public static int puddleDelayTicks = 50;

    HashMap<World, Integer> puddleMap = new HashMap<World, Integer>();

    public final Random rand = new Random();

    public WeatherListener(Storm storm) {
    	this.storm = storm;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void acidicWeatherListener(WeatherChangeEvent event)
    {

	final World affectedWorld = event.getWorld();
	
	if (!event.toWeatherState()) {	   
	    try {
		Bukkit.getScheduler().cancelTask(puddleMap.get(affectedWorld));
		System.out.println("Cleared schedulers.");
		
	    } catch (Exception e) {

	    }
	    return;
	}

	puddleSchedulerId = Bukkit.getScheduler()
		.scheduleSyncRepeatingTask(storm,
			new Runnable()
			{

			    @Override
			    public void run() {
			    }
			   				// TODO MAKE TEH PUDDLEZ
				
				
			}
			, 0, puddleDelayTicks);
	puddleMap.put(affectedWorld, puddleSchedulerId);

    }

}
