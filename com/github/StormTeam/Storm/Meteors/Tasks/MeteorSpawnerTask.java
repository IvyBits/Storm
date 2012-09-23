package com.github.StormTeam.Storm.Meteors.Tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Fireball;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.github.StormTeam.Storm.GlobalVariables;
import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Meteors.Entities.EntityMeteor;

public class MeteorSpawnerTask {

	private int id;
	private Random rand = new Random();
	private Storm storm;
	private World spawnWorld;

	private GlobalVariables glob;

	public MeteorSpawnerTask(Storm storm, World spawnWorld) {
		this.storm = storm;
		this.spawnWorld = spawnWorld;
		glob = Storm.wConfigs.get(spawnWorld.getName());
	}

	public void run() {

		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(
		        storm,
		        new Runnable() {
			        @Override
			        public void run() {
				        if (rand.nextInt(100) <= glob.Natural__Disasters_Meteor_Chance__To__Spawn) {

					        Chunk chunk = Storm.util.pickChunk(spawnWorld);

					        if (chunk == null) {
						        return;
					        }

					        final int x = rand.nextInt(16);
					        final int z = rand.nextInt(16);
					        final Block b = chunk.getBlock(x, 4, z);
					        spawnMeteorNaturallyAndRandomly(chunk.getWorld(),
					                b.getX(),
					                b.getZ());
				        }
			        }

		        }
		        ,
		        glob.Natural__Disasters_Meteor_Scheduler__Recalculation__Intervals__In__Ticks,
		        glob.Natural__Disasters_Meteor_Scheduler__Recalculation__Intervals__In__Ticks);
	}

	public void stop() {
		Bukkit.getScheduler().cancelTask(id);
	}

	private void spawnMeteorNaturallyAndRandomly(World world, double x,
	        double z)
	{
		net.minecraft.server.World meteoriteWorld = ((CraftWorld) world)
		        .getHandle();

		EntityMeteor meteor = new EntityMeteor(
		        meteoriteWorld,
		        rand.nextInt(7) + 1,
		        10,
		        rand.nextInt(5) + 5,
		        rand.nextInt(50) + 25,
		        100,
		        glob.Natural__Disasters_Meteor_Message__On__Meteor__Crash,
		        9,
		        80,
		        glob.Natural__Disasters_Meteor_Shockwave_Damage__Message,
		        rand.nextInt(100) + 200,
		        glob.Natural__Disasters_Meteor_Meteor_Spawn,
		        glob.Natural__Disasters_Meteor_Meteor_Radius);

		meteor.spawn();

		meteor.setPosition(
		        x,
		        rand.nextInt(100) + 156,
		        z);
		meteor.yaw = (float) rand.nextInt(360);
		meteor.pitch = -9;
		meteoriteWorld.addEntity(meteor, SpawnReason.DEFAULT);

		Fireball fireMeteor = (Fireball) meteor.getBukkitEntity();

		fireMeteor.setDirection(fireMeteor.getDirection().setY(-1));

	}

}
