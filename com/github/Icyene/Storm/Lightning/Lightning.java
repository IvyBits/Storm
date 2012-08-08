package com.github.Icyene.Storm.Lightning;

import org.bukkit.plugin.PluginManager;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Lightning.Listeners.StrikeListener;

public class Lightning {

    public static void load(Storm storm)
    {
	PluginManager pm = storm.getServer().getPluginManager();
	pm.registerEvents(new StrikeListener(storm), storm);
    }

}
