package com.github.Icyene.Storm.Lightning.Listeners;

import org.bukkit.Location;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

import com.github.Icyene.Storm.Storm;

import com.github.Icyene.Storm.Lightning.Lightning;
import com.github.Icyene.Storm.Lightning.LightningUtils;

public class StrikeListener implements Listener {

    LightningUtils util;
    Storm storm;

    public StrikeListener(Storm storm) {
	this.util = new LightningUtils(storm);
	this.storm = storm;
    }

    @EventHandler
    public void strikeLightningListener(LightningStrikeEvent strike) {

	if (strike.isCancelled())
	    return;

	strike.setCancelled(true);

	Location strikeLocation = util.hitMetal(strike.getLightning()
		.getLocation());
	strike.getWorld().strikeLightning(strikeLocation);
	util.damageNearbyPlayers(strikeLocation,
		Lightning.strikeRadius);

	util.melt(strikeLocation.getBlock());

	strike.getWorld().strikeLightningEffect(strikeLocation);

    }

}

