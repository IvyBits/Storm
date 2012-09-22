package com.github.StormTeam.Storm.Lightning;

import org.bukkit.plugin.PluginManager;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Lightning.Listeners.StrikeListener;

public class Lightning {

    public static void load(Storm storm)
    {
	PluginManager pm = storm.getServer().getPluginManager();
	pm.registerEvents(new StrikeListener(storm), storm);
    }

}
