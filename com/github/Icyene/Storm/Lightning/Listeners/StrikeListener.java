package com.github.Icyene.Storm.Lightning.Listeners;

import java.util.Random;

import org.bukkit.Location;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;

import com.github.Icyene.Storm.Lightning.LightningUtils;
import com.github.Icyene.Storm.MultiWorld.MultiWorldManager;
import com.github.Icyene.Storm.Transformations.BlockChanger;

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
	if (MultiWorldManager.checkWorld(strike.getWorld(), GlobalVariables.storm_lightning_allowedWorlds)) {
	    return;
	}
	if(GlobalVariables.storm_lightning_attraction_blocks_attractionChance >= 100 
    		|| ran.nextInt((int) (1000-GlobalVariables.storm_lightning_attraction_blocks_attractionChance*10)) == 0){
		Location strikeLocation = util.hitMetal(strike.getLightning()
			.getLocation());
		strike.getLightning().teleport(strikeLocation);
		util.damageNearbyPlayers(strikeLocation,
			GlobalVariables.storm_lightning_damage_strikeRadius);
	
		 BlockChanger.transform(strikeLocation.getBlock(), GlobalVariables.storm_lightning_melter_blockTransformations);
	}else{
		return;
	}
    }

   
}
