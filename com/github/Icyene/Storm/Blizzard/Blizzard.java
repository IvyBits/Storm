package com.github.Icyene.Storm.Blizzard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.plugin.PluginManager;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Blizzard.Listeners.BlizzardListeners;

public class Blizzard {

    public static HashMap<World, Boolean> blizzardingWorlds = new HashMap<World, Boolean>();

    public static List<Biome> snowyBiomes = Arrays.asList(new Biome[]
    { Biome.FROZEN_OCEAN, Biome.FROZEN_RIVER, Biome.ICE_MOUNTAINS,
	    Biome.ICE_PLAINS, Biome.TAIGA, Biome.TAIGA_HILLS });

    public static void load(Storm storm)
    {
	if (Storm.config.Features_Blizzards_Player__Damaging) {
	    PluginManager pm = storm.getServer().getPluginManager();
	    pm.registerEvents(new BlizzardListeners(storm), storm);
	}

	if (Storm.config.Features_Blizzards_Slowing__Snow) {
	    ModSnow.mod(true);
	}
    }

    public static void unload() {
	ModSnow.mod(false);
    }

}
// Trying to see if a player, when they log in, log into a world that has a blizzard, and if so, send them a TPack change.
// basically that lineyou want to see if their world is in the hashmap??????  Yes, let me look at the docs  WAIT!!! IT WORKS FINE. Oh damn
//I'm an idiot. If CWorld isnt in the hashmap already... that means it will return null... dammit'