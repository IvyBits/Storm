package com.github.StormTeam.Storm;

import com.github.StormTeam.Storm.Acid_Rain.AcidRain;
import com.github.StormTeam.Storm.Acid_Rain.Listeners.AcidListener;
import com.github.StormTeam.Storm.Blizzard.Blizzard;
import com.github.StormTeam.Storm.Blizzard.Listeners.BlizzardListeners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import static com.github.StormTeam.Storm.Storm.wConfigs;
import org.bukkit.World;
import org.bukkit.event.world.WorldUnloadEvent;

/**
 *
 * @author Tudor
 */
public class WorldMemoryManager implements Listener {

    private Storm storm;

    public WorldMemoryManager(Storm storm) {
        this.storm = storm;
    }

    @EventHandler
    public void onLoad(WorldLoadEvent e) {
        //Simply loads config file on world load - the one-size-fit-all-config-manager
        World world = e.getWorld();
        GlobalVariables config = new GlobalVariables(storm, world.getName());
        config.load();
        wConfigs.put(world, config);
    }

    @EventHandler
    public void onUnload(WorldUnloadEvent e) {

        World world = e.getWorld();
        if (wConfigs.containsKey(world)) {
            wConfigs.remove(e.getWorld());
        }
        if (AcidRain.acidicWorlds.contains(world)) {
            if (AcidListener.damagerMap.containsKey(world)) {
                AcidListener.damagerMap.get(world).stop();
            }
            if (AcidListener.dissolverMap.containsKey(world)) {
                AcidListener.dissolverMap.get(world).stop();
            }
        }
        if (Blizzard.blizzardingWorlds.contains(world)) {
            if (BlizzardListeners.damagerMap.containsKey(world)) {
                BlizzardListeners.damagerMap.get(world).stop();
            }
        }


    }
}
