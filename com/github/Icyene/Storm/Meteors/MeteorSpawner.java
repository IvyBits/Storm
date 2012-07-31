package com.github.Icyene.Storm.Meteors;

import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.World;

import org.bukkit.craftbukkit.CraftWorld;

import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.Icyene.Storm.Meteors.Entities.EntityMeteor;

public class MeteorSpawner {
 
    
    //TODO MAKE LESS MASSIVELY HACKY
   

    public void makeMeteor(World world, double x, double z)
    {
	Random rand = new Random();

	net.minecraft.server.World meteoriteWorld = ((CraftWorld) world).getHandle(); //get world

	EntityMeteor meteor = new EntityMeteor(meteoriteWorld);

	meteor.setPosition(x, 190, z);
	meteor.yaw = (float) rand.nextInt(360);
	meteor.pitch = (float) -15;
	meteor.yield = 5;
	meteor.explosionPower = (float) 2.0;
	meteor.impactPower = (float) 30.0;

	meteoriteWorld.addEntity(meteor, SpawnReason.CUSTOM);

	meteor.setDirection(meteor.getDirection().setY(-1));
    }

}
