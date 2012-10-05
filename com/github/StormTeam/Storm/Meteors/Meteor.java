package com.github.StormTeam.Storm.Meteors;

import java.lang.reflect.Method;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Meteors.Tasks.MeteorSpawnerTask;
import com.github.StormTeam.Storm.Meteors.Entities.EntityMeteor;

public class Meteor {

    public static Storm storm;
    private static CommandExecutor exec;

    public static void load(Storm ztorm) {
        storm = ztorm;
        try {
            Method a = net.minecraft.server.EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
            a.setAccessible(true);
            a.invoke(a, EntityMeteor.class, "Fireball", 12);

        } catch (Exception e) {
            Storm.util.log(Level.SEVERE, "Failed to create meteor entity!");
        }

        for (World w : ztorm.getServer().getWorlds()) {
            if (Storm.wConfigs.get(w).Features_Meteor) {
                new MeteorSpawnerTask(storm, w).run();
            }
        }

        exec = new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
                if ((sender instanceof Player)) {
                    Player p = ((Player) sender);
                    Location ploc = p.getLocation();
                    meteor(p.getTargetBlock(null, 0).getLocation(),
                            ploc.toVector().add(ploc.getDirection().normalize()).toLocation(ploc.getWorld()));
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Sorry, you must be in-game to do that!");
                }
                return false;
            }
        };

        ztorm.getCommand("meteor").setExecutor(exec);
    }

    public static void meteor(Location targetLoc, Location spawnLoc) {
        net.minecraft.server.WorldServer mcWorld = ((CraftWorld) (spawnLoc.getWorld())).getHandle();

        EntityMeteor mm = new EntityMeteor(
                mcWorld, 15, 15, 15, 60, 100,
                Storm.wConfigs.get(mcWorld.getWorld()).Natural__Disasters_Meteor_Message__On__Meteor__Crash, 9, 80,
                Storm.wConfigs.get(mcWorld.getWorld()).Natural__Disasters_Meteor_Shockwave_Damage__Message, 0, false, 0);

        mm.spawn();

        Fireball meteor = (Fireball) mm.getBukkitEntity();
        meteor.teleport(spawnLoc);
        meteor.setDirection(targetLoc.toVector().subtract(spawnLoc.toVector()));
        meteor.setBounce(false);
        meteor.setIsIncendiary(true);
    }
}
