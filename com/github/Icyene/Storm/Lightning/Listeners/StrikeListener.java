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
	  //  System.out.println("Strike cancelled.");
	    return;
	}
	if (!MultiWorldManager.checkWorld(strike.getWorld(),
		GlobalVariables.lightning_allowedWorlds)) {
	  //  System.out.println("Invalid world: " + strike.getWorld().getName()
		  //  + ": " + GlobalVariables.lightning_allowedWorlds);
	    return;
	}

	Location strikeLocation = strike.getLightning().getLocation();

	StormUtil.damageNearbyPlayers(strikeLocation,
		GlobalVariables.lightning_damage_strikeRadius,
		GlobalVariables.lightning_damage_strikeDamage,
		GlobalVariables.lightning_damage_strikeMessage);

	StormUtil.transform(strikeLocation.getBlock(),
		GlobalVariables.lightning_melter_blockTransformations);
	
	if (rand.nextInt(100) <= GlobalVariables.lightning_attraction_blocks_attractionChance) {
	    strikeLocation = util.hitMetal(strike.getLightning()
		    .getLocation());
	    strike.getLightning().teleport(strikeLocation);
	    return;
	}
	
	if (rand.nextInt(100) <= GlobalVariables.lightning_attraction_players_attractionChance) {
	    strikeLocation = util.hitPlayers(strike.getLightning()
		    .getLocation());
	    strike.getLightning().teleport(strikeLocation);
	    return;
	}
	
	
	
	

    }

}
