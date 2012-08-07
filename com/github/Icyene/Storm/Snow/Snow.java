package com.github.Icyene.Storm.Snow;

import org.bukkit.plugin.PluginManager;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Snow.Insubstantial.SnowThroughBlocks;

public class Snow
{

    public static void load(Storm storm)
    {
	
	if(GlobalVariables.snow_insubstantial_enabled) {   
	    PluginManager pm = storm.getServer().getPluginManager();
	    pm.registerEvents(new SnowThroughBlocks(), storm);
	    Storm.stats.add("Insubstantial Snow");	    
	}
	
	

	

    }

}
