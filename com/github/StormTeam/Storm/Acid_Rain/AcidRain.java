package com.github.StormTeam.Storm.Acid_Rain;

import com.github.StormTeam.Storm.Acid_Rain.Events.AcidRainEvent;
import org.bukkit.World;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Acid_Rain.Listeners.AcidListener;
import com.github.StormTeam.Storm.Acid_Rain.Tasks.DamagerTask;
import com.github.StormTeam.Storm.Acid_Rain.Tasks.DissolverTask;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcidRain {

    public static ArrayList<World> acidicWorlds = new ArrayList<World>();
    private static Storm storm;
    private static CommandExecutor exec;

    public static void load(Storm ztorm) {
        storm = ztorm;
        Storm.pm.registerEvents(new AcidListener(storm), storm);
        exec = new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
                if ((sender instanceof Player)) {
                    acidRain(((Player) sender).getWorld());
                    return true;
                } else {                  
                        World world = Bukkit.getServer().getWorld(args[0]);
                        if (world != null) {
                            world.setStorm(false); //Cancels other events
                            acidRain(world);
                            return true;
                        }                    
                }
                return false;
            }
        };
        storm.getCommand("acidrain").setExecutor(exec);
    }

    public static void acidRain(World world) {
        if (acidicWorlds.contains(world)) {
            acidicWorlds.remove(world);
            AcidListener.damagerMap.get(world).stop();
            AcidListener.dissolverMap.get(world).stop();
            Storm.util.setStormNoEvent(world, false);
            Storm.pm.callEvent(new AcidRainEvent(world, false));
        } else {
            acidicWorlds.add(world);
            DamagerTask dam = new DamagerTask(storm, world);
            AcidListener.damagerMap.put(world, dam);
            dam.run();
            DissolverTask dis = new DissolverTask(storm, world);
            AcidListener.dissolverMap.put(world, dis);
            dis.run();
            Storm.util.broadcast(Storm.wConfigs.get(world).Acid__Rain_Message__On__Acid__Rain__Start);
            Storm.util.setStormNoEvent(world, true);
            Storm.pm.callEvent(new AcidRainEvent(world, true));
        }
    }
}
