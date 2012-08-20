package com.github.Icyene.Storm.Lightning.Listeners;

import java.util.Random;

import org.bukkit.Location;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.StormUtil;

import com.github.Icyene.Storm.Lightning.LightningUtils;
import com.github.Icyene.Storm.Lightning.Fulgarites.FulgariteGenerator;
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
		GlobalVariables.Lightning_Allowed__Worlds)) {
	    return;
	}

	Location strikeLocation = strike.getLightning().getLocation();
	if (GlobalVariables.Features_Lightning_Greater__Range__And__Damage) {
	    StormUtil.damageNearbyPlayers(strikeLocation,
		    GlobalVariables.Lightning_Damage_Damage__Radius,
		    GlobalVariables.Lightning_Damage_Damage,
		    GlobalVariables.Lightning_Damage_Hit__Message);
	}
	if (GlobalVariables.Features_Lightning_Block__Attraction__Transformations) {

	    StormUtil.transform(strikeLocation.getBlock(),
		    GlobalVariables.Lightning_Melter_Block__Transformations);
	}
	if (GlobalVariables.Features_Lightning_Block__Attraction) {
	    if (rand.nextInt(100) <= GlobalVariables.Lightning_Attraction_Blocks_AttractionChance) {
		strikeLocation = util.hitMetal(strike.getLightning()
			.getLocation());
		strike.getLightning().teleport(strikeLocation);

	    }
	} else {
	    if (GlobalVariables.Features_Lightning_Player__Attraction) {
		if (rand.nextInt(100) <= GlobalVariables.Lightning_Attraction_Players_AttractionChance) {
		    strikeLocation = util.hitPlayers(strike.getLightning()
			    .getLocation());
		    strike.getLightning().teleport(strikeLocation);

		}
	    }
	}

	if (GlobalVariables.Features_Lightning_Fulgarites) {
	    FulgariteGenerator.generateFulgarite(strikeLocation,
		    GlobalVariables.Lightning_Fulgarites_Maximum__Depth,
		    GlobalVariables.Lightning_Fulgarites_Minimum__Depth);
	}

    }

}
