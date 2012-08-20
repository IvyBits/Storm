package com.github.Icyene.Storm.Transformations;

import org.bukkit.block.Block;

public class BlockChanger {

    public static void transform(Block toTransform, Integer[][] transformations) {

	final int blockId = toTransform.getTypeId();
	for (Integer[] toCheck : transformations) {
	    if (toCheck[0] == blockId) {
		toTransform.setTypeId(toCheck[1]);
		return;
	    }
	}
    }
    
}
