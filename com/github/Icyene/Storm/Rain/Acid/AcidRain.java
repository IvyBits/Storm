package com.github.Icyene.Storm.Rain.Acid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.plugin.PluginManager;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Rain.Acid.Listeners.WeatherListener;

public class AcidRain
{
    public static HashMap<World, Boolean> acidicWorlds = new HashMap<World, Boolean>();

    public static List<Biome> rainyBiomes = new ArrayList<Biome>();

    public static void load(Storm storm)
    {
	PluginManager pm = storm.getServer().getPluginManager();
	pm.registerEvents(new WeatherListener(storm), storm);

	rainyBiomes = Arrays.asList(new Biome[]
	{ Biome.EXTREME_HILLS, Biome.FOREST, Biome.FOREST_HILLS, Biome.JUNGLE,
		Biome.JUNGLE_HILLS, Biome.MUSHROOM_ISLAND,
		Biome.MUSHROOM_SHORE,
		Biome.PLAINS, Biome.OCEAN, Biome.RIVER,
		Biome.SWAMPLAND, Biome.SKY, Biome.SMALL_MOUNTAINS });
    }    

}