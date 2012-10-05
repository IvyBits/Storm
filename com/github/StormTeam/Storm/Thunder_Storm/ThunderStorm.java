package com.github.StormTeam.Storm.Thunder_Storm;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.World;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Thunder_Storm.Events.ThunderStormEvent;
import com.github.StormTeam.Storm.Thunder_Storm.Listeners.ThunderListener;
import com.github.StormTeam.Storm.Thunder_Storm.Tasks.StrikerTask;

/**
 * @author Tudor
 */
public class ThunderStorm {

    public static ArrayList<World> thunderingWorlds = new ArrayList<World>();
    private static Storm storm;
    private static CommandExecutor exec;

    public static void load(Storm ztorm) {
        storm = ztorm;
        Storm.pm.registerEvents(new ThunderListener(storm), storm);

        exec = new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
                if ((sender instanceof Player)) {
                    thunderstorm(((Player) sender).getWorld());
                    return true;
                } else {                   
                        World world = Bukkit.getServer().getWorld(args[0]);
                        if (world != null) {
                            world.setStorm(false); //Cancels other events
                            thunderstorm(world);
                            return true;
                        }                    
                }
                return false;
            }
        };
        storm.getCommand("thunderstorm").setExecutor(exec);
    }

    public static void thunderstorm(World world) {
        if (thunderingWorlds.contains(world)) {
            thunderingWorlds.remove(world);
            ThunderListener.strikerMap.get(world).stop();
            Storm.util.setStormNoEvent(world, false);
            Storm.pm.callEvent(new ThunderStormEvent(world, false));
        } else {
            thunderingWorlds.add(world);
            StrikerTask stik = new StrikerTask(storm, world);
            stik.run();
            ThunderListener.strikerMap.put(world, stik);
            Storm.util.broadcast(Storm.wConfigs.get(world).Thunder__Storm_Message__On__Thunder__Storm__Start);
            Storm.util.setStormNoEvent(world, true);
            Storm.pm.callEvent(new ThunderStormEvent(world, true));
        }
    }
}