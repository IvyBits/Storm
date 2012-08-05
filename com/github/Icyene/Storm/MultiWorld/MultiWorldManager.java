package com.github.Icyene.Storm.MultiWorld;

import java.util.Arrays;

import org.bukkit.World;

import com.github.Icyene.Storm.GlobalVariables;

public class MultiWorldManager {

    public static boolean checkWorld(World world, String[] worlds) {
   	System.out.println(world.getName());
   	System.out.println(Arrays
   		.asList(GlobalVariables.lightning_allowedWorlds));

   	if (Arrays.asList(GlobalVariables.lightning_allowedWorlds)
   		.contains(world.getName())) {
   	    return true;
   	} else {
   	    return false;
   	}

       }
    
}
