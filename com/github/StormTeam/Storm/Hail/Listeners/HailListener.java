package com.github.StormTeam.Storm.Hail.Listeners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Acid_Rain.AcidRain;

public class HailListener implements Listener {

    Storm storm;
    Random ran = new Random();
    public ArrayList<World> affectedWorlds = new ArrayList<World>();
    public ArrayList<Item> hailStones = new ArrayList<Item>();
    long hailDelayTicks = 60;

    public HailListener(Storm storm) {
	this.storm = storm;
	run();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onWeatherChange(WeatherChangeEvent event) {
	World world = event.getWorld();
	if (!AcidRain.acidicWorlds.get(world)) {
	    if ((event.toWeatherState())) {
		startHail(world);
	    } else if (affectedWorlds.contains(world)
		    && !event.toWeatherState()) {
		stopHail(world);
	    }
	}
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemPickup(PlayerPickupItemEvent e) {

    }

    private void startHail(World world) {
	affectedWorlds.add(world);
	
	Storm.util.log("Starting hail storm on " + world.getName());
	
    }

    private void stopHail(World world) {
	affectedWorlds.remove(world);
	
	Storm.util.log("Stopping hail storm on " + world.getName());
	
    }

    private void run() {

	Bukkit.getScheduler().scheduleSyncRepeatingTask(storm, new Runnable() {
	    @Override
	    public void run() {
		for (World w : affectedWorlds) {
		    for (Location loc : getRandomBlocks(w)) {
			//Item tmp = w.dropItem(loc, new ItemStack(
			//	Material.SNOW_BALL));
			//tmp.setVelocity(1);
			hailStones.add(w.dropItem(loc, new ItemStack(
				Material.SNOW_BALL)));
		    }
		}
	    }

	}
		, 0, hailDelayTicks);
    }

    private HashSet<Location> getRandomBlocks(World w) {
	HashSet<Location> locs = new HashSet<Location>();
	for (Chunk chunk : w.getLoadedChunks()) {
	    for (int i = 1; i <= 4; i++) {
		locs.add(chunk.getBlock(ran.nextInt(16), ran.nextInt(100)+156, ran.nextInt(16))
			.getLocation());
	    }
	}
	return locs;
    }
}


