package com.github.Icyene.Storm.Lightning.Listeners;

import java.util.Random;

import org.bukkit.Location;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Lightning.LightningUtils;

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

	GlobalVariables glob = Storm.wConfigs.get(strike.getWorld().getName());

	Location strikeLocation = strike.getLightning().getLocation();

	if (glob.Features_Lightning_Greater__Range__And__Damage) {
	    Storm.util.damageNearbyPlayers(strikeLocation,
		    glob.Lightning_Damage_Damage__Radius,
		    glob.Lightning_Damage_Damage,
		    glob.Lightning_Damage_Hit__Message);
	}

	if (glob.Features_Lightning_Block__Attraction) {
	    if (rand.nextInt(100) <= glob.Lightning_Attraction_Blocks_AttractionChance) {
		strikeLocation = util.hitMetal(strike.getLightning()
			.getLocation());
		strike.getLightning().teleport(strikeLocation);

	    }
	} else {
	    if (glob.Features_Lightning_Player__Attraction) {
		if (rand.nextInt(100) <= glob.Lightning_Attraction_Players_AttractionChance) {
		    strikeLocation = util.hitPlayers(strike.getLightning()
			    .getLocation());
		    strike.getLightning().teleport(strikeLocation);

		}
	    }
	}

	if (glob.Features_Lightning_Block__Transformations) {

	    Storm.util.transform(strikeLocation.getBlock(),
		    glob.Lightning_Melter_Block__Transformations);
	}

    }

}
