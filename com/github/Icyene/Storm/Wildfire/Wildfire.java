package com.github.Icyene.Storm.Wildfire;

import java.util.Arrays;
import java.util.List;

import net.minecraft.server.Block;

import org.bukkit.World;
import org.bukkit.block.Biome;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Wildfire.Tasks.Igniter;

public class Wildfire {

    public static Integer[] flammableBlocks = new Integer[] {
	    Block.FENCE.id, Block.WOOD.id, Block.WOOD_STAIRS.id,
	    Block.WOODEN_DOOR.id, Block.LEAVES.id, Block.BOOKSHELF.id,
	    Block.GRASS.id, Block.WOOL.id, Block.VINE.id,
	    Block.BIRCH_WOOD_STAIRS.id,

    };

    public static List<Biome> leafyBiomes = Arrays.asList(new Biome[] {

	    Biome.FOREST, Biome.FOREST_HILLS, Biome.JUNGLE, Biome.JUNGLE_HILLS,
	    Biome.HELL

    });

    public static void load(Storm storm) {

	for (World w : storm.getServer().getWorlds()) {
	    if (Storm.wConfigs.get(w.getName()).Features_Meteor) {
		new Igniter(storm, w).run();
	    }
	}

    }
}
