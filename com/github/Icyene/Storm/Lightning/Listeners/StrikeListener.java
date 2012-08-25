package com.github.Icyene.Storm.Lightning.Listeners;

import java.util.Random;

import org.bukkit.Location;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Lightning.LightningUtils;
import com.github.Icyene.Storm.MultiWorld.MultiWorldManager;

public class StrikeListener implements Listener {

    LightningUtils util;
    Storm storm;

    Random rand = new Random();

    public StrikeListener(Storm storm) {
	this.util = new LightningUtils(storm);
	this.storm = storm;
    }

    @EventHandler
    public void strikeLightningListener(final LightningStrikeEvent strike) {

	if (strike.isCancelled()) {
	    return;
	}
	if (!MultiWorldManager.checkWorld(strike.getWorld(),
		Storm.config.Lightning_Allowed__Worlds)) {
	    return;
	}

	Location strikeLocation = strike.getLightning().getLocation();

	if (Storm.config.Features_Lightning_Greater__Range__And__Damage) {
	    Storm.util.damageNearbyPlayers(strikeLocation,
		    Storm.config.Lightning_Damage_Damage__Radius,
		    Storm.config.Lightning_Damage_Damage,
		    Storm.config.Lightning_Damage_Hit__Message);
	}

	if (Storm.config.Features_Lightning_Block__Attraction) {
	    if (rand.nextInt(100) <= Storm.config.Lightning_Attraction_Blocks_AttractionChance) {
		strikeLocation = util.hitMetal(strike.getLightning()
			.getLocation());
		strike.getLightning().teleport(strikeLocation);

	    }
	} else {
	    if (Storm.config.Features_Lightning_Player__Attraction) {
		if (rand.nextInt(100) <= Storm.config.Lightning_Attraction_Players_AttractionChance) {
		    strikeLocation = util.hitPlayers(strike.getLightning()
			    .getLocation());
		    strike.getLightning().teleport(strikeLocation);

		}
	    }
	}

	if (Storm.config.Features_Lightning_Block__Transformations) {

	    Storm.util.transform(strikeLocation.getBlock(),
		    Storm.config.Lightning_Melter_Block__Transformations);
	}

    }

}
