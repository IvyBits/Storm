package com.github.Icyene.Storm.Wildfire.Tasks;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Wildfire.Wildfire;
import com.github.Icyene.Storm.Wildfire.Listeners.WildfireListeners;

public class Igniter {

	private int id;
	private Random rand = new Random();
	private World affectedWorld;
	private Storm storm;

	private GlobalVariables glob;

	public Igniter(Storm storm, World spawnWorld) {
		this.storm = storm;
		this.affectedWorld = spawnWorld;
		glob = Storm.wConfigs.get(spawnWorld.getName());
	}

	public void run() {

		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(
		        storm,
		        new Runnable() {
			        @Override
			        public void run() {
				        if (rand.nextInt(100) <= glob.Natural__Disasters_Meteor_Chance__To__Spawn) {
					        Block toBurn;
					        while (true) {

						        Chunk chunk = Storm.util
						                .pickChunk(affectedWorld);

						        if (chunk == null) {
							        Storm.util
							                .log("Selected chunk is null. Aborting wildfire.");
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

						        if (Storm.util
						                .isBlockProtected(toBurn)) {

						        } else {

							        if (Wildfire.leafyBiomes.contains(toBurn
							                .getBiome())
							                && Arrays
							                        .asList(Wildfire.flammableBlocks)
							                        .contains(
							                                toBurn.getTypeId())) {
								        break;
							        }

						        }

						        toBurn = toBurn.getLocation().add(0, 1, 0)
						                .getBlock();
						        toBurn.setType(Material.FIRE);
						        World world;
						        if (WildfireListeners.infernink
						                .containsKey((world = toBurn.getWorld()))) {
							        WildfireListeners.infernink.get(world).add(
							                toBurn);
						        }

						        for (Player p : affectedWorld.getPlayers()) {
							        Storm.util
							                .message(
							                        p,
							                        glob.Natural__Disasters_Wildfires_Message__On__Start
							                                .replace(
							                                        "%x",
							                                        toBurn.getX()
							                                                + "")
							                                .replace(
							                                        "%y",
							                                        toBurn.getY()
							                                                + "")
							                                .replace(
							                                        "%z",
							                                        toBurn.getZ()
							                                                + ""));
						        }

					        }
				        }
			        }

		        }
		        ,
		        glob.Natural__Disasters_Wildfires_Scheduler__Recalculation__Intervals__In__Ticks,
		        glob.Natural__Disasters_Wildfires_Scheduler__Recalculation__Intervals__In__Ticks);

	}

	public void stop() {
		Bukkit.getScheduler().cancelTask(id);
	}

}
