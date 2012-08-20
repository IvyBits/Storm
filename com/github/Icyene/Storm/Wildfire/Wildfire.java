package com.github.Icyene.Storm.Wildfire;

import java.lang.reflect.Method;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Wildfire.Listeners.FireEvent;

import net.minecraft.server.BlockFire;
import net.minecraft.server.World;

public class Wildfire {

    public static Method canBurn; 
    
    public static void load(Storm storm) {
	try {
	    
	canBurn = BlockFire.class.getDeclaredMethod("canPlace", World.class, int.class, int.class, int.class);
	
	storm.getServer().getPluginManager().registerEvents(new FireEvent(), storm);
	
	
	} catch (Exception e) {};
    }
    
}
