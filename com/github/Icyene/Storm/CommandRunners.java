package com.github.Icyene.Storm;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Fireball;

import com.github.Icyene.Storm.Acid_Rain.AcidRain;
import com.github.Icyene.Storm.Acid_Rain.Events.AcidRainEvent;
import com.github.Icyene.Storm.Acid_Rain.Listeners.AcidListener;
import com.github.Icyene.Storm.Acid_Rain.Tasks.DamagerTask;
import com.github.Icyene.Storm.Acid_Rain.Tasks.DissolverTask;
import com.github.Icyene.Storm.Blizzard.Blizzard;
import com.github.Icyene.Storm.Blizzard.Events.BlizzardEvent;
import com.github.Icyene.Storm.Blizzard.Listeners.BlizzardListeners;
import com.github.Icyene.Storm.Blizzard.Tasks.BlizzardTask;
import com.github.Icyene.Storm.Meteors.Entities.EntityMeteor;
import com.github.Icyene.Storm.Wildfire.Listeners.WildfireListeners;

public class CommandRunners {

    private Storm storm;

    public CommandRunners(Storm storm) {
	this.storm = storm;
    }

    public void meteor(Location targetLoc, Location spawnLoc)
    {
	net.minecraft.server.WorldServer mcWorld = ((CraftWorld) (spawnLoc
		.getWorld())).getHandle();

	EntityMeteor mm = new EntityMeteor(
		mcWorld,
		15,
		15,
		15,
		60,
		100,
		Storm.wConfigs.get(mcWorld.getWorld().getName()).Natural__Disasters_Meteor_Message__On__Meteor__Crash,
		9,
		80,
		Storm.wConfigs.get(mcWorld.getWorld().getName()).Natural__Disasters_Meteor_Shockwave_Damage__Message,
		0,Storm.wConfigs.get(mcWorld.getWorld().getName()).Natural__Disasters_Meteor_Meteor_Spawn,Storm.wConfigs.get(mcWorld.getWorld().getName()).Natural__Disasters_Meteor_Meteor_Radius);
	mm.spawn();

	Fireball meteor = (Fireball) mm.getBukkitEntity();
	meteor.teleport(spawnLoc);

	meteor.setDirection(targetLoc.toVector().subtract(spawnLoc.toVector()));
	meteor.setBounce(false);
	meteor.setIsIncendiary(true);
	meteor.setYield(0);

    }

    public void wildfire(Location targetLoc)
    {

	Block fire = targetLoc.getBlock().getRelative(BlockFace.UP);
	fire.setType(Material.FIRE);
	WildfireListeners.infernink.get(targetLoc.getWorld()).add(fire);

    }

    public void acidRain(World world) {

	if (AcidRain.acidicWorlds.containsKey(world)
		&& AcidRain.acidicWorlds.get(world)) {

	    AcidRain.acidicWorlds.remove(world);
	    AcidRain.acidicWorlds.put(world, Boolean.FALSE);
	    AcidListener.damagerMap.get(world).stop();
	    AcidListener.dissolverMap.get(world).stop();

	    world.setStorm(false);

	    AcidRainEvent endEvent = new AcidRainEvent(world,
		    false);
	    Bukkit.getServer().getPluginManager().callEvent(endEvent);

	} else {

	    final DamagerTask dam = new DamagerTask(storm, world);
	    AcidListener.damagerMap.put(world, dam);
	    dam.run();
	    final DissolverTask dis = new DissolverTask(storm, world);
	    AcidListener.dissolverMap.put(world,
		    new DissolverTask(storm, world));
	    dis.run();
	    AcidRain.acidicWorlds.remove(world);
	    AcidRain.acidicWorlds.put(world, Boolean.TRUE);
	    Storm.util
		    .broadcast(Storm.wConfigs.get(world.getName()).Acid__Rain_Message__On__Acid__Rain__Start);

	    world.setStorm(true);

	    AcidRainEvent startEvent = new AcidRainEvent(world,
		    true);
	    Bukkit.getServer().getPluginManager().callEvent(startEvent);

	}

    }

    public void blizzard(World world) {

	if (Blizzard.blizzardingWorlds.containsKey(world)
		&& Blizzard.blizzardingWorlds.get(world)) {

	    BlizzardListeners.damagerMap.get(world).stop();
	    Blizzard.blizzardingWorlds.remove(world);
	    Blizzard.blizzardingWorlds.put(world, Boolean.FALSE);

	    world.setStorm(false);

	    BlizzardEvent endEvent = new BlizzardEvent(world,
		    false);
	    Bukkit.getServer().getPluginManager().callEvent(endEvent);

	} else {

	    final BlizzardTask dam = new BlizzardTask(storm, world);
	    BlizzardListeners.damagerMap.put(world, dam);
	    dam.run();
	    Blizzard.blizzardingWorlds.remove(world);
	    Blizzard.blizzardingWorlds.put(world, Boolean.TRUE);
	    Storm.util
		    .broadcast(Storm.wConfigs.get(world.getName()).Blizzard_Message__On__Blizzard__Start);
	    world.setStorm(true);

	    BlizzardEvent startEvent = new BlizzardEvent(world,
		    false);
	    Bukkit.getServer().getPluginManager().callEvent(startEvent);

	}

    }

}
