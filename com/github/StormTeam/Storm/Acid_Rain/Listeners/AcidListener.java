package com.github.StormTeam.Storm.Acid_Rain.Listeners;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.weather.WeatherChangeEvent;

import com.github.StormTeam.Storm.GlobalVariables;
import com.github.StormTeam.Storm.Storm;
import static com.github.StormTeam.Storm.Acid_Rain.AcidRain.acidicWorlds;
import com.github.StormTeam.Storm.Acid_Rain.Events.AcidRainEvent;
import com.github.StormTeam.Storm.Acid_Rain.Tasks.DamagerTask;
import com.github.StormTeam.Storm.Acid_Rain.Tasks.DissolverTask;

public class AcidListener implements Listener {

    private static final Random rand = new Random();
    public static HashMap<String, DissolverTask> dissolverMap = new HashMap<String, DissolverTask>();
    public static HashMap<String, DamagerTask> damagerMap = new HashMap<String, DamagerTask>();
    private Storm storm;

    public AcidListener(Storm storm) {
        this.storm = storm;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void acidicWeatherListener(WeatherChangeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        World affectedWorld = event.getWorld();
        String name = affectedWorld.getName();

        GlobalVariables glob = Storm.wConfigs.get(name);

        if (event.toWeatherState()) {// gets if its set to raining

            if (rand.nextInt(100) <= glob.Acid__Rain_Acid__Rain__Chance) {

                if (!glob.Features_Acid__Rain_Dissolving__Blocks && !glob.Features_Acid__Rain_Player__Damaging) {
                    return;
                }

                acidicWorlds.put(name, true);

                AcidRainEvent startEvent = new AcidRainEvent(affectedWorld,
                        true);
                Storm.pm.callEvent(new AcidRainEvent(affectedWorld, true));

                if (startEvent.isCancelled()) {
                    return;
                }

                for (Player p : affectedWorld.getPlayers()) {

                    Storm.util.message(p, glob.Acid__Rain_Message__On__Acid__Rain__Start);
                }

            } else {
                return;
            }
        } else if (!event.toWeatherState()) {
          
            acidicWorlds.put(name, false);

            // Cancel damaging tasks for specific world
            AcidRainEvent startEvent = new AcidRainEvent(affectedWorld,
                    false);
            Bukkit.getServer().getPluginManager().callEvent(startEvent);
            try {
                dissolverMap.get(name).stop();
                damagerMap.get(name).stop();
            } catch (Exception e) {
            };

            return;
        }

        if (glob.Features_Acid__Rain_Dissolving__Blocks) {
            DissolverTask dis = new DissolverTask(storm, affectedWorld);
            dissolverMap.put(name, new DissolverTask(storm, affectedWorld));
            dis.run();
        }

        if (glob.Features_Acid__Rain_Player__Damaging) {
            DamagerTask dam = new DamagerTask(storm, affectedWorld);
            damagerMap.put(name, dam);
            dam.run();
        }

    }
}
