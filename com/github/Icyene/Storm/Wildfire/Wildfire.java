package com.github.Icyene.Storm.Wildfire;

import org.bukkit.event.Listener;

import com.github.Icyene.Storm.Storm;

public class Wildfire implements Listener {

    public void load(Storm storm) {
	storm.getServer().getPluginManager().registerEvents(this, storm);
    }

}
