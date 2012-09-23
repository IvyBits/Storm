package com.github.StormTeam.Storm.Hail;

import org.bukkit.plugin.PluginManager;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Hail.Listeners.HailListener;

public class Hail {

    public static void load(Storm storm) {
	PluginManager pm = storm.getServer().getPluginManager();
	pm.registerEvents(new HailListener(storm), storm);
    }

}
