package com.github.Icyene.Storm.Meteors.Tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Fireball;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Meteors.Entities.EntityMeteor;

public class MeteorSpawnerTask {

    private int id;
    private Random rand = new Random();
    private Storm storm;

    public MeteorSpawnerTask(Storm storm) {
	this.storm = storm;
    }

    public void run() {

	id = Bukkit.getScheduler().scheduleSyncRepeatingTask(
		storm,
		new Runnable() {
		    @Override
		    public void run() {
			if (rand.nextInt(100) <= Storm.config.Natural__Disasters_Meteor_Chance__To__Spawn) {

			       Chunk chunk = Storm.util.pickChunk(Storm.util.pickWorld(storm, Storm.config.Natural__Disasters_Meteor_Allowed__Worlds));

				if(chunk == null) {
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
		Storm.config.Natural__Disasters_Meteor_Scheduler_Spawner__Recalculation__Intervals__In__Ticks,
		Storm.config.Natural__Disasters_Meteor_Scheduler_Spawner__Recalculation__Intervals__In__Ticks);
    }

     public void stop() {
	Bukkit.getScheduler().cancelTask(id);
    }
     
     private void spawnMeteorNaturallyAndRandomly(World world, double x,
		    double z)
	    {
		net.minecraft.server.World meteoriteWorld = ((CraftWorld) world)
			.getHandle();

		EntityMeteor meteor = new EntityMeteor(meteoriteWorld);

		meteor.setPosition(
			x,
			rand.nextInt(100) + 156,
			z);
		meteor.yaw = (float) rand.nextInt(360);
		meteor.pitch = -9;
		meteor.setBurrowCount(rand.nextInt(7)+1);
		meteoriteWorld.addEntity(meteor, SpawnReason.DEFAULT);

		meteor.setCrashMessage(Storm.config.Natural__Disasters_Meteor_Message__On__Meteor__Crash);
		meteor.setBrightness(rand.nextInt(30) + 5);
		meteor.setExplosionPower(rand.nextInt(50) + 25);
		meteor.setTrail(rand.nextInt(5) + 5);

		Fireball fireMeteor = (Fireball) meteor.getBukkitEntity();

		fireMeteor.setDirection(fireMeteor.getDirection().setY(-1));

	    }

}
