package tk.ivybits.storm.weather.acidrain;

import tk.ivybits.storm.Storm;
import tk.ivybits.storm.WorldVariables;
import tk.ivybits.storm.utility.ErrorLogger;
import tk.ivybits.storm.utility.ReflectCommand;
import tk.ivybits.storm.weather.exc.WeatherNotFoundException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A class for loading acid rain.
 */

public class AcidRain {

    /**
     * Enables acid rain.
     */

    public static void load() {
        try {
            Storm.manager.registerWeather(AcidRainWeather.class, "storm_acidrain");

            for (World w : Bukkit.getWorlds()) {
                loadWorld(w);
            }
            Storm.manager.registerWorldLoadHandler(AcidRain.class.getDeclaredMethod("loadWorld", World.class));
            Storm.commandRegistrator.register(AcidRain.class);
        } catch (Exception e) {
            ErrorLogger.alert(e);
        }
    }

    private static void loadWorld(World world) throws WeatherNotFoundException {
        String name = world.getName();
        WorldVariables temp = Storm.wConfigs.get(name);
        if (temp.Weathers__Enabled_Acid__Rain) {
            Storm.manager.enableWeatherForWorld("storm_acidrain", name,
                    temp.Acid__Rain_Acid__Rain__Chance, temp.Acid__Rain_Acid__Rain__Base__Interval);
        }
    }

    @ReflectCommand.Command(
            name = "acidrain",
            usage = "/<command> [world]",
            permission = "storm.acidrain.command",
            sender = ReflectCommand.Sender.EVERYONE
    )
    public static boolean acidrainConsole(CommandSender sender, String world) {
        if (acidrain(world)) {
            sender.sendMessage(ChatColor.RED + "Acid rain not enabled in specified world or is conflicting with another weather!");
            return true;
        }
        return false;
    }

    @ReflectCommand.Command(
            name = "acidrain",
            usage = "/<command> [world]",
            permission = "storm.acidrain.command",
            sender = ReflectCommand.Sender.PLAYER
    )
    public static boolean acidrainPlayer(Player sender) {
        if (acidrain((sender).getWorld().getName())) {
            sender.sendMessage(ChatColor.RED + "Acid rain not enabled in specified world or is conflicting with another weather!");
            return true;
        }
        return false;
    }

    private static boolean acidrain(String world) {
        try {
            if (Storm.manager.getActiveWeathers(world).contains("storm_acidrain")) {
                Storm.manager.stopWeather("storm_acidrain", world);
                return false;
            } else {
                return Storm.manager.startWeather("storm_acidrain", world);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }
}
