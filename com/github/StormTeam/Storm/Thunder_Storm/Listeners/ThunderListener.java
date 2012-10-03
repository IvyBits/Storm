/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.StormTeam.Storm.Thunder_Storm.Listeners;

/**
 *
 * @author Tudor
 */

import com.github.StormTeam.Storm.GlobalVariables;
import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Thunder_Storm.Events.ThunderStormEvent;
import com.github.StormTeam.Storm.Thunder_Storm.Tasks.StrikerTask;
import static com.github.StormTeam.Storm.Thunder_Storm.ThunderStorm.thunderingWorlds;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class ThunderListener 
     implements Listener {

    private static final Random rand = new Random();
    public static HashMap<World, StrikerTask> strikerMap = new HashMap<World, StrikerTask>();
   
    private Storm storm;

    public ThunderListener(Storm storm) {
        this.storm = storm;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void acidicWeatherListener(WeatherChangeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        World affectedWorld = event.getWorld();
      
        GlobalVariables glob = Storm.wConfigs.get(affectedWorld);

        if(!glob.Features_Thunder__Storms) {
            return;
        }
        
        if (event.toWeatherState()) {// gets if its set to raining

            if (rand.nextInt(100) <= glob.Acid__Rain_Acid__Rain__Chance) {

                thunderingWorlds.add(affectedWorld);

                ThunderStormEvent startEvent = new ThunderStormEvent(affectedWorld,
                        true);
                Storm.pm.callEvent(new ThunderStormEvent(affectedWorld, true));

                if (startEvent.isCancelled()) {
                    return;
                }

                for (Player p : affectedWorld.getPlayers()) {

                    Storm.util.message(p, glob.Thunder__Storm_Message__On__Thunder__Storm__Start);
                }

            } else {
                return;
            }
        } else if (!event.toWeatherState()) {
          
            thunderingWorlds.remove(affectedWorld);

            // Cancel damaging tasks for specific world
            ThunderStormEvent startEvent = new ThunderStormEvent(affectedWorld, false);
            Bukkit.getServer().getPluginManager().callEvent(startEvent);
            try {
                strikerMap.get(affectedWorld).stop();               
            } catch (Exception e) {
            };

            return;
        }


    }
}
