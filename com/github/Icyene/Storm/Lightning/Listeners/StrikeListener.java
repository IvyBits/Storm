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
    boolean recursion;

    Random ran = new Random();

    public StrikeListener(Storm storm) {
	this.util = new LightningUtils(storm);
	this.storm = storm;
    }

    @EventHandler
    public void strikeLightningListener(LightningStrikeEvent strike) {

	if (strike.isCancelled())
	    return;
	if (!MultiWorldManager.checkWorld(strike.getWorld(),
		GlobalVariables.lightning_allowedWorlds)) {
	    return;
	}
	

	
	Location strikeLocation = strike.getLightning().getLocation();
	if (GlobalVariables.lightning_attraction_blocks_attractionChance >= 100
		|| ran.nextInt((int) (1000 - GlobalVariables.lightning_attraction_blocks_attractionChance * 10)) == 0) {
	    strikeLocation = util.hitMetal(strike.getLightning()
		    .getLocation());
	    strike.getLightning().teleport(strikeLocation);
	}
	StormUtil.damageNearbyPlayers(strikeLocation,
		GlobalVariables.lightning_damage_strikeRadius,
		GlobalVariables.lightning_damage_strikeDamage,
		GlobalVariables.lightning_damage_strikeMessage);

	StormUtil.transform(strikeLocation.getBlock(),
		GlobalVariables.lightning_melter_blockTransformations);

    }

}
