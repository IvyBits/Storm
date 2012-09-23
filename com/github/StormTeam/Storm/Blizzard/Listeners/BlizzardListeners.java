package com.github.StormTeam.Storm.Blizzard.Listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.github.StormTeam.Storm.GlobalVariables;
import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Blizzard.Blizzard;
import static com.github.StormTeam.Storm.Blizzard.Blizzard.blizzardingWorlds;
import com.github.StormTeam.Storm.Blizzard.Events.BlizzardEvent;
import com.github.StormTeam.Storm.Blizzard.Tasks.PlayerTask;

public class BlizzardListeners implements Listener {

    private Random rand = new Random();
    public static HashMap<String, PlayerTask> damagerMap = new HashMap<String, PlayerTask>();
    private Storm storm;

    public BlizzardListeners(Storm storm) {
        this.storm = storm;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void blizzardListener(WeatherChangeEvent event) {

        if (event.isCancelled()) {
            return;
        }

        World affectedWorld = event.getWorld();
        String name = affectedWorld.getName();

        GlobalVariables glob = Storm.wConfigs.get(name);

        if (event.toWeatherState()) {// gets if its set to raining

            if (rand.nextInt(100) <= glob.Blizzard_Blizzard__Chance) {

                // Here it checks to see if the world is enabled
                if (!glob.Features_Blizzards_Player__Damaging
                        && !glob.Features_Blizzards_Slowing__Snow) {
                    return;
                }
                blizzardingWorlds.put(name, true);

                BlizzardEvent startEvent = new BlizzardEvent(affectedWorld, true);
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
        } else if (!event.toWeatherState()) {

            blizzardingWorlds.put(name, false);
            // Cancel damaging tasks for specific world

            try {
                damagerMap.get(name).stop();
            } catch (Exception e) {
            };

            BlizzardEvent startEvent = new BlizzardEvent(affectedWorld, false);
            Bukkit.getServer().getPluginManager().callEvent(startEvent);

            return;
        }

        if (glob.Features_Blizzards_Player__Damaging) {
            PlayerTask bliz = new PlayerTask(storm, affectedWorld);
            damagerMap.put(affectedWorld.getName(), bliz);
            bliz.run();
        }

    }
}
