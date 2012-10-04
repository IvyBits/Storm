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
import static com.github.StormTeam.Storm.Blizzard.Blizzard.blizzardingWorlds;
import com.github.StormTeam.Storm.Blizzard.Events.BlizzardEvent;
import com.github.StormTeam.Storm.Blizzard.ModSnow;
import com.github.StormTeam.Storm.Blizzard.Tasks.PlayerTask;

public class BlizzardListeners implements Listener {

    private Random rand = new Random();
    public static HashMap<World, PlayerTask> damagerMap = new HashMap<World, PlayerTask>();
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

        GlobalVariables glob = Storm.wConfigs.get(affectedWorld);

        if (event.toWeatherState()) {// gets if its set to raining

            if (rand.nextInt(100) <= glob.Blizzard_Blizzard__Chance) {

                // Here it checks to see if the world is enabled
                if (!glob.Features_Blizzards_Player__Damaging
                        && !glob.Features_Blizzards_Slowing__Snow) {
                    return;
                }
                blizzardingWorlds.add(affectedWorld);

                BlizzardEvent startEvent = new BlizzardEvent(affectedWorld, true);
                Bukkit.getServer().getPluginManager().callEvent(startEvent);

                if (glob.Features_Blizzards_Slowing__Snow) {
                    ModSnow.mod(true);
                }

                if (startEvent.isCancelled()) {
                    return;
                }

                for (Player p : event.getWorld().getPlayers()) {
                    Storm.util
                            .message(
                            p,
                            glob.Blizzard_Message__On__Blizzard__Start);
                }

                if (glob.Features_Blizzards_Player__Damaging) {
                    PlayerTask bliz = new PlayerTask(storm, affectedWorld);
                    damagerMap.put(affectedWorld, bliz);
                    bliz.run();
                }

            } else {
                return;
            }
        } else if (!event.toWeatherState()) {

            blizzardingWorlds.remove(affectedWorld);
            // Cancel damaging tasks for specific world

            try {
                damagerMap.get(affectedWorld).stop();
            } catch (Exception e) {
            };

            BlizzardEvent endEvent = new BlizzardEvent(affectedWorld, false);
            Bukkit.getServer().getPluginManager().callEvent(endEvent);

            if (glob.Features_Blizzards_Slowing__Snow) {
                ModSnow.mod(false);
            }

            return;
        }



    }
}
