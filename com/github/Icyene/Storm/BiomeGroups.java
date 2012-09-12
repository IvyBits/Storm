package com.github.Icyene.Storm;

import java.util.Arrays;
import java.util.List;

import org.bukkit.block.Biome;

public class BiomeGroups // Utility class for storing all biomes.
{

	private final List<Biome>	rainBiomes	 = Arrays.asList(new Biome[]
	                                 { Biome.EXTREME_HILLS,
	                                 Biome.FOREST, Biome.FOREST_HILLS,
	                                 Biome.JUNGLE,
	                                 Biome.JUNGLE_HILLS, Biome.MUSHROOM_ISLAND,
	                                 Biome.MUSHROOM_SHORE,
	                                 Biome.PLAINS, Biome.OCEAN, Biome.RIVER,
	                                 Biome.SWAMPLAND, Biome.SKY,
	                                 Biome.SMALL_MOUNTAINS });

	private final List<Biome>	snowBiomes	 = Arrays.asList(new Biome[]
	                                 { Biome.FROZEN_OCEAN, Biome.FROZEN_RIVER,
	                                 Biome.ICE_MOUNTAINS,
	                                 Biome.ICE_PLAINS, Biome.TAIGA,
	                                 Biome.TAIGA_HILLS });

	private final List<Biome>	desertBiomes	= Arrays.asList(new Biome[]
	                                 { Biome.DESERT, Biome.DESERT_HILLS });

	public boolean isRainy(Biome b)
	{
		return rainBiomes.contains(b);
	}

	public boolean isForest(Biome b)
	{
		return isRainy(b);
	}

	public boolean isDesert(Biome b)
	{
		return desertBiomes.contains(b);
	}

	public boolean isTundra(Biome b)
	{
		return snowBiomes.contains(b);

	}

	public boolean isSnowy(Biome b)
	{
		return isTundra(b);
	}

}
