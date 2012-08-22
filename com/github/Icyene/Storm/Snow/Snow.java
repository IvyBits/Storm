package com.github.Icyene.Storm.Snow;

import org.bukkit.plugin.PluginManager;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Snow.ThruBlocks.SnowThroughBlocks;

public class Snow
{

    public static void load(Storm storm)
    {

	if (GlobalVariables.Features_Snow_PassThruBlocks) {
	PluginManager pm = storm.getServer().getPluginManager();
	pm.registerEvents(new SnowThroughBlocks(), storm);
	}
	
	//ModSnow.mod(true);

    }

}
