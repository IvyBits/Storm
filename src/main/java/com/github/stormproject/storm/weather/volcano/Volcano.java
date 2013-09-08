package com.github.stormproject.storm.weather.volcano;

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.WorldVariables;
import com.github.stormproject.storm.utility.ErrorLogger;
import com.github.stormproject.storm.utility.ReflectCommand;
import com.github.stormproject.storm.weather.exc.WeatherNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;

public class Volcano {
    public static File vulkanos;

    public static void load() {
        try {
            vulkanos = new File(Storm.instance.getDataFolder() + File.separator + "volcanoes.bin");
            if (vulkanos.exists() || vulkanos.createNewFile())
                VolcanoControl.load(vulkanos);
            Storm.manager.registerWeather(VolcanoWeather.class, "storm_volcano");
            for (World w : Bukkit.getWorlds()) {
                loadWorld(w);
            }
            Storm.manager.registerWorldLoadHandler(Volcano.class.getDeclaredMethod("loadWorld", World.class));
            Storm.commandRegistrator.register(Volcano.class);
            new LifeTask().start();
        } catch (Exception e) {
            ErrorLogger.alert(e);
        }
    }

    private static void loadWorld(World world) throws WeatherNotFoundException {
        String name = world.getName();
        WorldVariables temp = Storm.wConfigs.get(name);
        if (temp.Weathers__Enabled_Natural__Disasters_Volcanoes) {
            Storm.manager.enableWeatherForWorld("storm_volcano", name,
                    temp.Natural__Disasters_Volcano_Chance__To__Start, temp.Natural__Disasters_Volcano_Volcano__Base__Interval);
        }
    }

    @ReflectCommand.Command(
            name = "volcano",
            alias = {"firemountain", "vulkano"},
            usage = "/<command>",
            permission = "storm.volcano.command",
            sender = ReflectCommand.Sender.PLAYER
    )
    public static boolean volcano(Player p) {
        try {
            volcano(p.getTargetBlock(null, 0).getLocation(), 90);
        } catch (Exception e) {
            p.sendMessage(ChatColor.RED + "You can't create a volcano in a protected area!");
        }
        return true;
    }

    @ReflectCommand.Command(
            name = "volcano",
            alias = {"firemountain", "vulkano"},
            usage = "/<command>",
            permission = "storm.volcano.command",
            sender = ReflectCommand.Sender.PLAYER
    )
    public static boolean volcano(Player p, String radius) {
        try {
            volcano(p.getTargetBlock(null, 200).getLocation(), Integer.parseInt(radius));
        } catch (Exception e) {
            p.sendMessage(ChatColor.RED + "You can't create a volcano in a protected area!");
        }
        return true;
    }

    public static void volcano(Location loc, int radius) {
        VolcanoWorker volcano = new VolcanoWorker(loc, radius, 0);
        volcano.start();
    }
}
