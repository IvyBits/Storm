package com.github.stormproject.storm.utility.data;

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.WorldVariables;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

/**
 * Handles memory leaks caused by World unloading and loads/unloads config files.
 *
 * @author Icyene
 */
public class WorldConfigLoader implements Listener {

    private final Storm storm;

    /**
     * Creates a WorldConfigLoader object to handle config for worlds (un)loaded by plugins a la MultiVerse.
     *
     * @param storm The storm plugin: used for WorldVariables loading
     */

    public WorldConfigLoader(Storm storm) {
        this.storm = storm;
    }

    /**
     * Loads a config file for world when it is loaded
     *
     * @param e The WorldLoadEvent
     */

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        String world = e.getWorld().getName();
        WorldVariables config = new WorldVariables(Storm.instance, world, ".worlds");
        config.load();
        Storm.wConfigs.put(world, config);
    }

    /**
     * Removes the config object on unload; prevents world leaks
     *
     * @param e The WorldUnloadEvent
     */

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent e) {
        Storm.wConfigs.remove(e.getWorld().getName());
    }
}
