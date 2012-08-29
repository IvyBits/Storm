package com.github.Icyene.Storm;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Fireball;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.github.Icyene.Storm.Acid_Rain.AcidRain;
import com.github.Icyene.Storm.Acid_Rain.Listeners.AcidListener;
import com.github.Icyene.Storm.Acid_Rain.Tasks.DamagerTask;
import com.github.Icyene.Storm.Acid_Rain.Tasks.DissolverTask;
import com.github.Icyene.Storm.Blizzard.Blizzard;
import com.github.Icyene.Storm.Blizzard.Listeners.BlizzardListeners;
import com.github.Icyene.Storm.Meteors.Entities.EntityMeteor;
import com.github.Icyene.Storm.Wildfire.Listeners.WildfireListeners;

public class CommandRunners {

    private Storm storm;

    public CommandRunners(Storm storm) {
	this.storm = storm;
    }

    public void meteor(Location targetLoc, Location spawnLoc)
    {
	net.minecraft.server.WorldServer mcWorld = ((CraftWorld) (targetLoc
		.getWorld())).getHandle();

	EntityMeteor mm = new EntityMeteor(mcWorld);
	mcWorld.addEntity(mm, SpawnReason.CUSTOM);

	mm.setCrashMessage(" ");
	mm.setBrightness(30F);
	mm.setExplosionPower(40F);
	mm.setTrail(10F);

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
	WildfireListeners.infernink.add(fire);

    }

    public void acidRain(World world) {

	if (AcidRain.acidicWorlds.get(world)) {

	    AcidRain.acidicWorlds.remove(world);
	    AcidRain.acidicWorlds.put(world, Boolean.FALSE);
	    AcidListener.damagerMap.get(world).stop();
	    AcidListener.dissolverMap.get(world).stop();

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
		    .broadcast(Storm.config.Acid__Rain_Message__On__Acid__Rain__Start);

	}

    }

    public void blizzard(World world) {

	if (Blizzard.blizzardingWorlds.get(world)) {

	    BlizzardListeners.damagerMap.get(world).stop();
	    Blizzard.blizzardingWorlds.remove(world);
	    Blizzard.blizzardingWorlds.put(world, Boolean.FALSE);

	} else {

	    final DamagerTask dam = new DamagerTask(storm, world);
	    AcidListener.damagerMap.put(world, dam);
	    dam.run();
	    final DissolverTask dis = new DissolverTask(storm, world);
	    AcidListener.dissolverMap.put(world,
		    new DissolverTask(storm, world));
	    dis.run();
	    Blizzard.blizzardingWorlds.remove(world);
	    Blizzard.blizzardingWorlds.put(world, Boolean.TRUE);
	    Storm.util
		    .broadcast(Storm.config.Blizzard_Message__On__Blizzard__Start);

	}

    }

}
