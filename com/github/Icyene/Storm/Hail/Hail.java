package com.github.Icyene.Storm.Hail;

import org.bukkit.plugin.PluginManager;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Hail.Listeners.HailListener;

public class Hail {
	
    public static void load(Storm storm) {
    	PluginManager pm = storm.getServer().getPluginManager();
    	pm.registerEvents(new HailListener(storm), storm);
    }

}
