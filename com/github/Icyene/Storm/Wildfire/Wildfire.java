package com.github.Icyene.Storm.Wildfire;

import java.util.Arrays;
import java.util.List;

import org.bukkit.block.Biome;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Wildfire.Listeners.FireEvent;

public class Wildfire {


    public static List<Biome> leafyBiomes = Arrays.asList(new Biome[] {
	    
	    Biome.FOREST, Biome.FOREST_HILLS, Biome.JUNGLE, Biome.JUNGLE_HILLS, Biome.HELL
	    
    });
    
    public static void load(Storm storm) {
	try {

	    if(GlobalVariables.Features_Wildfires) {
	    storm.getServer().getPluginManager()
		    .registerEvents(new FireEvent(), storm);
	    }

	} catch (Exception e) {
	};

    }
}
