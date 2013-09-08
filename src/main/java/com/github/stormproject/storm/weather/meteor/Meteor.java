package com.github.stormproject.storm.weather.meteor;

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.WorldVariables;
import com.github.stormproject.storm.utility.ErrorLogger;
import com.github.stormproject.storm.utility.ReflectCommand;
import com.github.stormproject.storm.weather.exc.WeatherNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_6_R2.CraftWorld;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * A class for loading meteors.
 */

public class Meteor {

    public static final Set<Integer> meteors = new HashSet();

    /**
     * Enables meteors.
     */

    public static void load() {
        try {
            Storm.pm.registerEvents(new SafeExplosion(), Storm.instance);
            patchMeteor();
            Storm.manager.registerWeather(MeteorWeather.class, "storm_meteor");

            for (World w : Bukkit.getWorlds()) {
                loadWorld(w);
            }
            Storm.manager.registerWorldLoadHandler(Meteor.class.getDeclaredMethod("loadWorld", World.class));
            Storm.commandRegistrator.register(Meteor.class);
        } catch (Exception e) {
            ErrorLogger.alert(e);
        }
    }

    private static void loadWorld(World world) throws WeatherNotFoundException {
        String name = world.getName();
        WorldVariables temp = Storm.wConfigs.get(name);
        if (temp.Weathers__Enabled_Natural__Disasters_Meteors) {
            Storm.manager.enableWeatherForWorld("storm_meteor", name,
                    temp.Natural__Disasters_Meteor_Chance__To__Spawn,
                    temp.Natural__Disasters_Meteor_Meteor__Base__Interval);
        }
    }

    @ReflectCommand.Command(
            name = "meteor",
            alias = {"meteorite"},
            usage = "/<command> [world]",
            permission = "storm.meteor.command",
            sender = ReflectCommand.Sender.EVERYONE
    )
    public static boolean meteorConsole(CommandSender sender, String world) {
        if (meteorConsole(world)) {
            sender.sendMessage(ChatColor.RED + "Meteors are not enabled in specified world or are conflicting with another weather!");
            return true;
        }
        return false;
    }

    @SuppressWarnings("SameReturnValue")
    @ReflectCommand.Command(
            name = "meteor",
            alias = {"meteorite"},
            permission = "storm.meteor.command",
            sender = ReflectCommand.Sender.PLAYER
    )
    public static boolean meteorPlayer(Player sender) {
        Location senderLocation = sender.getLocation();
        trajectoryMeteor(sender.getTargetBlock(null, 0).getLocation(),
                senderLocation.toVector().add(senderLocation.getDirection().normalize()).toLocation(senderLocation.getWorld()));
        return true;
    }

    private static boolean meteorConsole(String world) {
        try {
            if (Storm.manager.getActiveWeathers(world).contains("storm_meteor")) {
                Storm.manager.stopWeather("storm_meteor", world);
                return false;
            } else {
                return Storm.manager.startWeather("storm_meteor", world);
            }
        } catch (Exception ex) {
            return true;
        }
    }

    private static void trajectoryMeteor(Location targetLoc, Location spawnLoc) {
        net.minecraft.server.v1_6_R2.WorldServer mcWorld = ((CraftWorld) (spawnLoc.getWorld())).getHandle();

        WorldVariables glob = Storm.wConfigs.get(mcWorld.getWorld().getName());
        EntityMeteor mm = new EntityMeteor(
                mcWorld, 15, 15, 15, 60, 100,
                glob.Natural__Disasters_Meteor_Messages_On__Meteor__Crash, 9,
                glob.Natural__Disasters_Meteor_Shockwave_Damage__Radius,
                glob.Natural__Disasters_Meteor_Messages_On__Damaged__By__Shockwave, false, 0);

        mm.spawn();

        Fireball meteor = (Fireball) mm.getBukkitEntity();
        meteor.teleport(spawnLoc);
        meteor.setDirection(targetLoc.toVector().subtract(spawnLoc.toVector()));
        meteor.setBounce(false);
        meteor.setIsIncendiary(true);
    }

    private static void patchMeteor() {
        try {
            Method a = net.minecraft.server.v1_6_R2.EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            a.setAccessible(true);
            a.invoke(a, EntityMeteor.class, "StormMeteor", 12);
        } catch (Exception e) {
            ErrorLogger.alert(e);
        }
    }
}
