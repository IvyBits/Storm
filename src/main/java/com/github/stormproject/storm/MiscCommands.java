package com.github.stormproject.storm;

import com.github.stormproject.storm.utility.ReflectCommand;
import com.github.stormproject.storm.weather.exc.WeatherNotFoundException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class MiscCommands {
    @ReflectCommand.Command(
            name = "stopall",
            usage = "/<command> [world]",
            permission = "storm.stopall.command",
            sender = ReflectCommand.Sender.EVERYONE
    )
    public static boolean stopallConsole(CommandSender sender, String world) {
        stopall(world);
        sender.sendMessage(ChatColor.GREEN + "Weather events stopped.");
        return true;
    }

    @ReflectCommand.Command(
            name = "stopall",
            usage = "/<command> [world]",
            permission = "storm.stopall.command",
            sender = ReflectCommand.Sender.PLAYER
    )
    public static boolean stopallPlayer(Player sender) {
        stopall((sender).getWorld().getName());
        sender.sendMessage(ChatColor.GREEN + "Weather events stopped.");
        return true;
    }

    private static void stopall(String world) {
        Set<String> active = Storm.manager.getActiveWeathers(world);
        for (String weather : active) {
            try {
                Storm.manager.disableWeatherForWorld(weather, world);
            } catch (WeatherNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void load() {
        Storm.commandRegistrator.register(MiscCommands.class);
    }
}
