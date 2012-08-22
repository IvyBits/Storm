package com.github.Icyene.Storm.MultiWorld;

import java.util.Arrays;
import org.bukkit.World;

public class MultiWorldManager {

    public static boolean checkWorld(World world, String[] worlds) {
      	if (Arrays.asList(worlds)
   		.contains(world.getName())) {
   	    return true;
   	} else {
   	    return false;
   	}
       }    
}
