package com.github.StormTeam.Storm;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import static com.github.StormTeam.Storm.Storm.wConfigs;
import org.bukkit.World;

/**
 *
 * @author Tudor
 */
public class WorldConfigLoader implements Listener {

    private Storm storm;

    public WorldConfigLoader(Storm storm) {
        this.storm = storm;
    }

    @EventHandler
    public void onLoad(WorldLoadEvent e) {
        World w = e.getWorld();       
        GlobalVariables config = new GlobalVariables(storm, w.getName());
        config.load();
        wConfigs.put(w, config);
    }
}
