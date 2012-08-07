/*
 * Storm
 * Copyright (C) 2012 Icyene
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.Icyene.Storm.Rain.Acid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.plugin.PluginManager;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Rain.Acid.Listeners.FormListener;
import com.github.Icyene.Storm.Rain.Acid.Listeners.GrowthListener;
import com.github.Icyene.Storm.Rain.Acid.Listeners.WeatherListener;

public class AcidRain
{
    public static HashMap<World, Boolean> acidicWorlds = new HashMap<World, Boolean>();

    public static List<Biome> rainyBiomes,
	    snowyBiomes = new ArrayList<Biome>();

    public static boolean acidSnow = GlobalVariables.rain_acid_acidSnow;
    public static boolean acidRain = GlobalVariables.rain_acid_acidRain;

    public static void load(Storm storm)
    {
	
	PluginManager pm = storm.getServer().getPluginManager();
	pm.registerEvents(new WeatherListener(storm), storm);
	pm.registerEvents(new GrowthListener(), storm);
	pm.registerEvents(new FormListener(), storm);

	rainyBiomes = Arrays.asList(new Biome[]
	{ Biome.EXTREME_HILLS, Biome.FOREST, Biome.FOREST_HILLS, Biome.JUNGLE,
		Biome.JUNGLE_HILLS, Biome.MUSHROOM_ISLAND,
		Biome.MUSHROOM_SHORE,
		Biome.PLAINS, Biome.OCEAN, Biome.SEASONAL_FOREST, Biome.RIVER,
		Biome.SWAMPLAND, Biome.SKY, Biome.SMALL_MOUNTAINS });

	snowyBiomes = Arrays.asList(new Biome[]
	{ Biome.TAIGA_HILLS, Biome.TAIGA, Biome.TUNDRA, Biome.FROZEN_OCEAN,
		Biome.FROZEN_RIVER, Biome.ICE_DESERT, Biome.ICE_MOUNTAINS,
		Biome.ICE_PLAINS });

    }    

}