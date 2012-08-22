package com.github.Icyene.Storm.Blizzard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.plugin.PluginManager;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Blizzard.Listeners.BlizzardSpawner;

public class Blizzard {

    public static HashMap<World, Boolean> blizzardingWorlds = new HashMap<World, Boolean>();

    public static List<Biome> snowyBiomes = Arrays.asList(new Biome[]
    { Biome.FROZEN_OCEAN, Biome.FROZEN_RIVER, Biome.ICE_MOUNTAINS,
	    Biome.ICE_PLAINS, Biome.TAIGA, Biome.TAIGA_HILLS });

    public static void load(Storm storm)
    {
	if (GlobalVariables.Features_Blizzards_Player__Damaging) {
	    PluginManager pm = storm.getServer().getPluginManager();
	    pm.registerEvents(new BlizzardSpawner(storm), storm);
	}

	if (GlobalVariables.Features_Blizzards_Slowing__Snow) {
	    ModSnow.mod(true);
	}
    }

    public static void unload() {
	ModSnow.mod(false);
    }

}
