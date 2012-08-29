package com.github.Icyene.Storm.Wildfire.Tasks;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Wildfire.Wildfire;
import com.github.Icyene.Storm.Wildfire.Listeners.WildfireListeners;

public class Igniter {

    private int id;
    private Random rand = new Random();
    @SuppressWarnings("unused")
    private World affectedWorld;
    private Storm storm;

    public Igniter(Storm storm) {
	this.storm = storm;
    }

    public void run() {

	id = Bukkit.getScheduler().scheduleSyncRepeatingTask(
		storm,
		new Runnable() {
		    @Override
		    public void run() {
			if (rand.nextInt(100) <= Storm.config.Natural__Disasters_Meteor_Chance__To__Spawn) {
			    Block toBurn;
			    while (true) {
				Chunk chunk = Storm.util.pickChunk(Storm.util
					.pickWorld(
						storm,
						Storm.config.Natural__Disasters_Wildfires_Allowed__Worlds));

				if (chunk == null) {
				    Storm.util.log("Selected chunk is null. Aborting wildfire.");
				    return;
				}

				final int x = rand.nextInt(16);
				final int z = rand.nextInt(16);

				toBurn = chunk.getWorld()
					.getHighestBlockAt(
						chunk
							.getBlock(
								x,
								4,
								z)
							.getLocation())
					.getLocation()
					.subtract(0, 1, 0)
					.getBlock();

				if (Wildfire.leafyBiomes.contains(toBurn
					.getBiome())
					&& Arrays
						.asList(Wildfire.flammableBlocks)
						.contains(toBurn.getTypeId())) {
				    break;
				}

			    }

			    toBurn = toBurn.getLocation().add(0, 1, 0)
				    .getBlock();
			    toBurn.setType(Material.FIRE);
			    WildfireListeners.infernink.add(toBurn);
			    Storm.util
				    .broadcast(Storm.config.Natural__Disasters_Wildfires_Message__On__Start
					    .replace("%x", toBurn.getX() + "")
					    .replace("%y", toBurn.getY() + "")
					    .replace("%z", toBurn.getZ() + ""));

			}
		    }

		}
		,
		Storm.config.Natural__Disasters_Wildfires_Scheduler_Spawner__Recalculation__Intervals__In__Ticks,
		Storm.config.Natural__Disasters_Wildfires_Scheduler_Spawner__Recalculation__Intervals__In__Ticks);

    }

    public void stop() {
	Bukkit.getScheduler().cancelTask(id);
    }

}
