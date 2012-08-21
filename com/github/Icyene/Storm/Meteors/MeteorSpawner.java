package com.github.Icyene.Storm.Meteors;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Fireball;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Vector;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.StormUtil;
import com.github.Icyene.Storm.Meteors.Entities.EntityMeteor;

public class MeteorSpawner {

    static final Random rand = new Random();
    static final int y = 4;

    public static void load(final Storm storm) {

	Bukkit.getScheduler().scheduleSyncRepeatingTask(
		storm,
		new Runnable() {
		    @Override
		    public void run() {
			if (rand.nextInt(100) <= GlobalVariables.Natural__Disasters_Meteor_Chance__To__Spawn) {

			    Chunk chunk = StormUtil.pickChunk(StormUtil.pickWorld(storm, GlobalVariables.Natural__Disasters_Meteor_Allowed__Worlds));

			    final int x = rand.nextInt(16);
			    final int z = rand.nextInt(16);
			    final Block b = chunk.getBlock(x, y, z);
			    spawnMeteorNaturallyAndRandomly(chunk.getWorld(),
				    b.getX(),
				    b.getZ());
			}
		    }

		}
		,
		com.github.Icyene.Storm.GlobalVariables.Natural__Disasters_Meteor_Scheduler_Spawner__Recalculation__Intervals__In__Ticks,
		com.github.Icyene.Storm.GlobalVariables.Natural__Disasters_Meteor_Scheduler_Spawner__Recalculation__Intervals__In__Ticks);
    }

    public static void meteorCommand(Location targetLoc,
	    Location spawnLoc)
    {
	// Target coords
	final double x1 = targetLoc.getX();
	final double y1 = targetLoc.getY();
	final double z1 = targetLoc.getZ();

	// Spawn coords
	final double x0 = spawnLoc.getX();
	final double y0 = spawnLoc.getY();
	final double z0 = spawnLoc.getZ();

	Vector translation = new Vector(x1 - x0, y1 - y0, z1 - z0);

	net.minecraft.server.WorldServer mcWorld = ((CraftWorld) (targetLoc
		.getWorld())).getHandle();

	EntityMeteor mm = new EntityMeteor(mcWorld);
	mcWorld.addEntity(mm, SpawnReason.CUSTOM);

	mm.setCrashMessage(GlobalVariables.Natural__Disasters_Meteor_Message__On__Meteor__Crash);
	mm.setBrightness(30F);
	mm.setExplosionPower(60F);
	mm.setTrail(10F);

	Fireball meteor = (Fireball) mm.getBukkitEntity();
	meteor.teleport(spawnLoc);

	meteor.setDirection(translation);
	meteor.setBounce(false);
	meteor.setIsIncendiary(true);
	meteor.setYield(2);

    }

    public static void spawnMeteorNaturallyAndRandomly(World world, double x,
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
	meteor.pitch = -rand.nextInt(20);
	meteor.setBurrowCount(rand.nextInt(10) + 5);
	meteoriteWorld.addEntity(meteor, SpawnReason.DEFAULT);

	meteor.setCrashMessage(GlobalVariables.Natural__Disasters_Meteor_Message__On__Meteor__Crash);
	meteor.setBrightness(rand.nextInt(30) + 5);
	meteor.setExplosionPower(rand.nextInt(50) + 25);
	meteor.setTrail(rand.nextInt(5) + 5);

	Fireball fireMeteor = (Fireball) meteor.getBukkitEntity();

	fireMeteor.setDirection(fireMeteor.getDirection().setY(-1));

    }

}
