package com.github.Icyene.Storm.Meteors;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Meteors.Entities.EntityMeteor;
import com.github.Icyene.Storm.MultiWorld.MultiWorldManager;

public class MeteorSpawner {

    // TODO MAKE LESS MASSIVELY HACKY

    static final Random rand = new Random();
    static final int y = 4;

    public static void makeLargeMeteor(World world, double x, double z)
    {
	net.minecraft.server.World meteoriteWorld = ((CraftWorld) world)
		.getHandle(); // get world

	EntityMeteor meteor = new EntityMeteor(meteoriteWorld);

	meteor.setPosition(
		x,
		com.github.Icyene.Storm.GlobalVariables.naturalDisasters_meteorites_meteor_spawnHeight,
		z);
	meteor.yaw = (float) rand.nextInt(360); // GET RANDOM DIRECTION
	meteor.pitch = GlobalVariables.naturalDisasters_meteorites_meteor_pitch; // FACE
								      // EARTH
	meteor.yield = GlobalVariables.naturalDisasters_meteorites_meteor_yield; // Items dropped

	meteoriteWorld.addEntity(meteor, SpawnReason.DEFAULT);

	meteor.setDirection(meteor.getDirection().setY(GlobalVariables.naturalDisasters_meteorites_meteor_accelarationY));

	System.out.println("Meteor generated.");
    }

    public static void load(final Storm storm) {

	Bukkit.getScheduler().scheduleSyncRepeatingTask(
		storm,
		new Runnable() {
		    @Override
		    public void run() {
			if (com.github.Icyene.Storm.GlobalVariables.naturalDisasters_meteorites_meteor_spawnChance >= 100
				|| rand.nextInt((int) (1000 - com.github.Icyene.Storm.GlobalVariables.naturalDisasters_meteorites_meteor_spawnChance * 10)) == 0) {

			    Chunk chunk = pickChunk(pickWorld(storm));

			    final int x = rand.nextInt(16);
			    final int z = rand.nextInt(16);
			    final Block b = chunk.getBlock(x, y, z);
			    makeLargeMeteor(chunk.getWorld(), b.getX(),
				    b.getZ());
			} else {
			    System.out.println("Meteor averted.");
			}
		    }

		}
		,
		com.github.Icyene.Storm.GlobalVariables.naturalDisasters_meteorites_meteor_recalculationDelayTicks,
		com.github.Icyene.Storm.GlobalVariables.naturalDisasters_meteorites_meteor_recalculationDelayTicks);
	System.out.println("Thread meteor started.");
    }

    public static Chunk pickChunk(final World w) {
	final Chunk[] loadedChunks = w.getLoadedChunks();
	return loadedChunks[rand.nextInt(loadedChunks.length)];
    }

    public static World pickWorld(final Storm storm) {
	ArrayList<World> worlds = new ArrayList<World>();
	for (World w : storm.getServer().getWorlds()) {
	    if (MultiWorldManager.checkWorld(w,
		    GlobalVariables.naturalDisasters_meteorites_meteor_allowedWorlds)) {
		worlds.add(w);
	    }
	}
	return worlds.get(rand.nextInt(worlds.size()));
    }

}
