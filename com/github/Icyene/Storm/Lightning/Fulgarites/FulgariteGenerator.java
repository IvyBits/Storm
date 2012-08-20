package com.github.Icyene.Storm.Lightning.Fulgarites;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class FulgariteGenerator {

    private static final Random rand = new Random();

    public static void generateFulgarite(Location toGenFrom, int MAX_DEPTH,
	    int MIN_DEPTH) {

	final int depth = rand.nextInt(MAX_DEPTH + MIN_DEPTH) + MIN_DEPTH;

	for (int i = 0; i == depth; ++i) {

	    final Block toFulgaratinate /* No, its not a word */= toGenFrom
		    .subtract(0, i, 0).getBlock();
	    if (toFulgaratinate.getType().equals(Material.SAND)) {

		toFulgaratinate.setType(Material.GLASS);

	    } else {
		return; //Stop
	    }

	}

    }

}
