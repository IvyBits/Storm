package com.github.Icyene.Storm.Wildfire;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.StormUtil;
import com.github.Icyene.Storm.Wildfire.Listeners.FireEvent;

public class WildfireSpawner {

    static final Random rand = new Random();
    static final int y = 4;

    public static void load(final Storm storm) {

	Bukkit.getScheduler().scheduleSyncRepeatingTask(
		storm,
		new Runnable() {
		    @Override
		    public void run() {
			if (rand.nextInt(100) <= GlobalVariables.Natural__Disasters_Meteor_Chance__To__Spawn) {
			    Block toBurn;
			    while (true) {
				Chunk chunk = StormUtil.pickChunk(StormUtil
					.pickWorld(
						storm,
						GlobalVariables.Natural__Disasters_Wildfires_Allowed__Worlds));

				final int x = rand.nextInt(16);
				final int z = rand.nextInt(16);

				toBurn = chunk.getWorld()
					.getHighestBlockAt(
						chunk
							.getBlock(
								x,
								y,
								z)
							.getLocation())
					.getLocation()
					.subtract(0, 1, 0)
					.getBlock();

				if (Wildfire.leafyBiomes.contains(toBurn
					.getBiome())
					&& Arrays
						.asList(GlobalVariables.Natural__Disasters_Wildfires_Flammable__Blocks)
						.contains(toBurn.getTypeId())) {
				    break;
				}

			    }

			    toBurn = toBurn.getLocation().add(0, 1, 0)
				    .getBlock();
			    toBurn.setType(Material.FIRE);
			    FireEvent.infernink.add(toBurn);
			    StormUtil
				    .broadcast(GlobalVariables.Natural__Disasters_Wildfires_Message__On__Start
					    .replace("%x", toBurn.getX() + "")
					    .replace("%y", toBurn.getY() + "")
					    .replace("%z", toBurn.getZ() + ""));

			}
		    }

		}
		,
		GlobalVariables.Natural__Disasters_Wildfires_Scheduler_Spawner__Recalculation__Intervals__In__Ticks,
		GlobalVariables.Natural__Disasters_Wildfires_Scheduler_Spawner__Recalculation__Intervals__In__Ticks);
    }

}
