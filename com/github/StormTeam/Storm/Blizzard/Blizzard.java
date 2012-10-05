package com.github.StormTeam.Storm.Blizzard;

import com.github.StormTeam.Storm.Blizzard.Events.BlizzardEvent;
import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.command.CommandExecutor;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Blizzard.Listeners.BlizzardListeners;
import com.github.StormTeam.Storm.Blizzard.Tasks.PlayerTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Blizzard {

    public static ArrayList<World> blizzardingWorlds = new ArrayList<World>();
    private static Storm storm;
    private static CommandExecutor exec;

    public static void load(Storm ztorm) {
        storm = ztorm;
        Storm.pm.registerEvents(new BlizzardListeners(storm), storm);
        ModSnow.mod(true);
        exec = new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
                if ((sender instanceof Player)) {
                    blizzard(((Player) sender).getWorld());
                    return true;
                } else {                   
                        World world = Bukkit.getServer().getWorld(args[0]);
                        if (world != null) {
                            world.setStorm(false); //Cancels other events
                            blizzard(world);
                            return true;
                        }                    
                }
                return false;
            }
        };
        storm.getCommand("blizzard").setExecutor(exec);

    }

    public static void unload() {
        ModSnow.mod(false);
    }

    public static void blizzard(World world) {
        if (blizzardingWorlds.contains(world)) {
            blizzardingWorlds.remove(world);
            BlizzardListeners.damagerMap.get(world).stop();
            Storm.util.setStormNoEvent(world, false);
            Storm.pm.callEvent(new BlizzardEvent(world, false));
        } else {
            blizzardingWorlds.add(world);
            PlayerTask dam = new PlayerTask(storm, world);
            dam.run();
            BlizzardListeners.damagerMap.put(world, dam);
            Storm.util.broadcast(Storm.wConfigs.get(world).Blizzard_Message__On__Blizzard__Start);
            Storm.util.setStormNoEvent(world, true);
            Storm.pm.callEvent(new BlizzardEvent(world, true));
        }

    }
}