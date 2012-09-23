package com.github.StormTeam.Storm.Acid_Rain.Listeners;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.github.StormTeam.Storm.GlobalVariables;
import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Acid_Rain.AcidRain;
import com.github.StormTeam.Storm.Acid_Rain.Events.AcidRainEvent;
import com.github.StormTeam.Storm.Acid_Rain.Tasks.DamagerTask;
import com.github.StormTeam.Storm.Acid_Rain.Tasks.DissolverTask;

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

	GlobalVariables glob = Storm.wConfigs.get(affectedWorld.getName());
	
	if (event.toWeatherState()) {// gets if its set to raining

	    if (rand.nextInt(100) <= glob.Acid__Rain_Acid__Rain__Chance) {

		if(!glob.Features_Acid__Rain_Dissolving__Blocks && !glob.Features_Acid__Rain_Player__Damaging) {
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

		for (Player p : affectedWorld.getPlayers()) {

		    Storm.util
			    .message(
				    p,
				    glob.Acid__Rain_Message__On__Acid__Rain__Start);
		}

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
	    try {
		dissolverMap.get(affectedWorld).stop();
		damagerMap.get(affectedWorld).stop();
	    } catch (Exception e) {
	    }
	    ;

	    return;
	}

	if (glob.Features_Acid__Rain_Dissolving__Blocks) {
	    final DissolverTask dis = new DissolverTask(storm, affectedWorld);
	    dissolverMap.put(affectedWorld, new DissolverTask(storm,
		    affectedWorld));
	    dis.run();
	}

	if (glob.Features_Acid__Rain_Player__Damaging) {
	    final DamagerTask dam = new DamagerTask(storm, affectedWorld);
	    damagerMap.put(affectedWorld, dam);
	    dam.run();
	}

    }
}
