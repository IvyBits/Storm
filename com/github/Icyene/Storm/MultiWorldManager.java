package com.github.Icyene.Storm;

import java.util.List;

import org.bukkit.World;

public class MultiWorldManager {

    public static boolean checkWorld(World world, List<String> acid__Rain_Allowed__Worlds) {
      	if (acid__Rain_Allowed__Worlds.contains(world.getName())) {
   	    return true;
   	} else {
   	    return false;
   	}
       }    
}
