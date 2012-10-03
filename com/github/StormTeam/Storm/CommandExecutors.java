package com.github.StormTeam.Storm;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Fireball;

import static com.github.StormTeam.Storm.Acid_Rain.AcidRain.acidicWorlds;
import com.github.StormTeam.Storm.Acid_Rain.Events.AcidRainEvent;
import com.github.StormTeam.Storm.Acid_Rain.Listeners.AcidListener;
import com.github.StormTeam.Storm.Acid_Rain.Tasks.DamagerTask;
import com.github.StormTeam.Storm.Acid_Rain.Tasks.DissolverTask;
import static com.github.StormTeam.Storm.Blizzard.Blizzard.blizzardingWorlds;
import com.github.StormTeam.Storm.Blizzard.Events.BlizzardEvent;
import com.github.StormTeam.Storm.Blizzard.Listeners.BlizzardListeners;
import com.github.StormTeam.Storm.Blizzard.Tasks.PlayerTask;
import com.github.StormTeam.Storm.Meteors.Entities.EntityMeteor;
import com.github.StormTeam.Storm.Thunder_Storm.Events.ThunderStormEvent;
import com.github.StormTeam.Storm.Thunder_Storm.Listeners.ThunderListener;
import com.github.StormTeam.Storm.Thunder_Storm.Tasks.StrikerTask;
import com.github.StormTeam.Storm.Wildfire.Wildfire;
import static com.github.StormTeam.Storm.Thunder_Storm.ThunderStorm.thunderingWorlds;

import java.util.Arrays;

public class CommandExecutors {

    private Storm storm;

    public CommandExecutors(Storm storm) {
        this.storm = storm;
    }

    public void meteor(Location targetLoc, Location spawnLoc) {
        net.minecraft.server.WorldServer mcWorld = ((CraftWorld) (spawnLoc
                .getWorld())).getHandle();

        EntityMeteor mm = new EntityMeteor(
                mcWorld,
                15,
                15,
                15,
                60,
                100,
                Storm.wConfigs.get(mcWorld.getWorld()).Natural__Disasters_Meteor_Message__On__Meteor__Crash,
                9,
                80,
                Storm.wConfigs.get(mcWorld.getWorld()).Natural__Disasters_Meteor_Shockwave_Damage__Message,
                0,
                false,
                0);


        mm.spawn();

        Fireball meteor = (Fireball) mm.getBukkitEntity();
        meteor.teleport(spawnLoc);

        meteor.setDirection(targetLoc.toVector().subtract(spawnLoc.toVector()));
        meteor.setBounce(false);
        meteor.setIsIncendiary(true);
    }

    public void wildfire(Location targetLoc) {

        Block fire = targetLoc.getBlock().getRelative(BlockFace.UP);
        fire.setType(Material.FIRE);
        World world = targetLoc.getWorld();

        if (Wildfire.wildfireBlocks.containsKey(world)) {
            Wildfire.wildfireBlocks.get(world).add(fire);
        } else {
            Wildfire.wildfireBlocks.put(world, Arrays.asList(fire));
        }

    }

    public void acidRain(World world) {

        if (acidicWorlds.contains(world)) {

            blizzardingWorlds.remove(world);
            acidicWorlds.remove(world);
            thunderingWorlds.remove(world);;
            AcidListener.damagerMap.get(world).stop();
            AcidListener.dissolverMap.get(world).stop();

            world.setStorm(false);

            Storm.pm.callEvent(new AcidRainEvent(world, false));

        } else {

            DamagerTask dam = new DamagerTask(storm, world);
            AcidListener.damagerMap.put(world, dam);
            dam.run();

            DissolverTask dis = new DissolverTask(storm, world);
            AcidListener.dissolverMap.put(world, dis);
            dis.run();

            acidicWorlds.add(world);

            Storm.util
                    .broadcast(Storm.wConfigs.get(world).Acid__Rain_Message__On__Acid__Rain__Start);

            world.setStorm(true);

            Storm.pm.callEvent(new AcidRainEvent(world, true));

        }

    }

    public void blizzard(World world) {

        if (blizzardingWorlds.contains(world)) {

            BlizzardListeners.damagerMap.get(world).stop();

            blizzardingWorlds.remove(world);
            acidicWorlds.remove(world);
            thunderingWorlds.remove(world);
            world.setStorm(false);

            Storm.pm.callEvent(new BlizzardEvent(world, false));

        } else {

            PlayerTask dam = new PlayerTask(storm, world);
            dam.run();
            BlizzardListeners.damagerMap.put(world, dam);

            blizzardingWorlds.add(world);
            Storm.util
                    .broadcast(Storm.wConfigs.get(world).Blizzard_Message__On__Blizzard__Start);
            world.setStorm(true);

            Storm.pm.callEvent(new BlizzardEvent(world, true));
        }

    }

    public void thunderstorm(World world) {

        if (thunderingWorlds.contains(world)) {

            ThunderListener.strikerMap.get(world).stop();

            blizzardingWorlds.remove(world);
            acidicWorlds.remove(world);
            thunderingWorlds.remove(world);

            world.setStorm(false);

            Storm.pm.callEvent(new ThunderStormEvent(world, false));

        } else {

            StrikerTask stik = new StrikerTask(storm, world);
            stik.run();
            ThunderListener.strikerMap.put(world, stik);

            thunderingWorlds.add(world);
            Storm.util
                    .broadcast(Storm.wConfigs.get(world).Thunder__Storm_Message__On__Thunder__Storm__Start);
            world.setStorm(true);

            Storm.pm.callEvent(new ThunderStormEvent(world, true));
        }

    }
}
