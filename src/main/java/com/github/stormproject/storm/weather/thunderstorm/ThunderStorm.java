package com.github.stormproject.storm.weather.thunderstorm;

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.WorldVariables;
import com.github.stormproject.storm.utility.ErrorLogger;
import com.github.stormproject.storm.utility.ReflectCommand;
import com.github.stormproject.storm.weather.exc.WeatherNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A class for loading thunder storms.
 */

public class ThunderStorm {

    /**
     * Enables thunder storms.
     */

    public static void load() {

        try {
            Storm.manager.registerWeather(ThunderStormWeather.class, "storm_thunderstorm");

            for (World w : Bukkit.getWorlds()) {
                loadWorld(w);
            }
            Storm.manager.registerWorldLoadHandler(ThunderStorm.class.getDeclaredMethod("loadWorld", World.class));
            Storm.commandRegistrator.register(ThunderStorm.class);
        } catch (Exception e) {
            ErrorLogger.alert(e);
        }
    }

    private static void loadWorld(World world) throws WeatherNotFoundException {
        String name = world.getName();
        WorldVariables temp = Storm.wConfigs.get(name);
        if (temp.Thunder__Storm_Features_Thunder__Striking) {
            Storm.manager.enableWeatherForWorld("storm_thunderstorm", name,
                    temp.Thunder__Storm_Thunder__Storm__Chance, temp.Thunder__Storm_Thunder__Storm__Base__Interval);
        }
    }

    @ReflectCommand.Command(
            name = "thunderstorm",
            usage = "/<command> [world]",
            permission = "storm.thunderstorm.command",
            sender = ReflectCommand.Sender.EVERYONE
    )
    public static boolean thunderstormConsole(CommandSender sender, String world) {
        if (thunderstorm(world)) {
            sender.sendMessage(ChatColor.RED + "Thunderstorms are not enabled in specified world or are conflicting with another weather!");
            return true;
        }
        return false;
    }

    @ReflectCommand.Command(
            name = "thunderstorm",
            permission = "storm.thunderstorm.command",
            sender = ReflectCommand.Sender.PLAYER
    )
    public static boolean thunderstormPlayer(Player sender) {
        if (thunderstorm((sender).getWorld().getName())) {
            sender.sendMessage(ChatColor.RED + "Thunderstorms are not enabled in specified world or are conflicting with another weather!");
            return true;
        }
        return false;
    }

    private static boolean thunderstorm(String world) {
        try {
            if (Storm.manager.getActiveWeathers(world).contains("storm_thunderstorm")) {
                Storm.manager.stopWeather("storm_thunderstorm", world);
                return false;
            } else {
                return Storm.manager.startWeather("storm_thunderstorm", world);
            }
        } catch (Exception ex) {
            return true;
        }
    }
}
