package com.github.stormproject.storm.weather.wildfire;

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.WorldVariables;
import com.github.stormproject.storm.utility.ErrorLogger;
import com.github.stormproject.storm.utility.ReflectCommand;
import com.github.stormproject.storm.weather.exc.WeatherNotFoundException;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A class for loading wildfires.
 */

public class Wildfire {

    /**
     * A HashMap containing world names and blocks that are part of a wildfire.
     */
    public static final HashMap<String, Set<Block>> wildfireBlocks = new HashMap<String, Set<Block>>();

    /**
     * Enables wildfires.
     */

    public static void load() {
        try {
            Storm.pm.registerEvents(new WildfireControl(), Storm.instance);
            Storm.manager.registerWeather(WildfireWeather.class, "storm_wildfire");

            for (World w : Bukkit.getWorlds())
                loadWorld(w);

            Storm.manager.registerWorldLoadHandler(Wildfire.class.getDeclaredMethod("loadWorld", World.class));
            Storm.commandRegistrator.register(Wildfire.class);
        } catch (Exception e) {
            ErrorLogger.alert(e);
        }
    }

    private static void loadWorld(World world) throws WeatherNotFoundException {
        String name = world.getName();
        WorldVariables temp = Storm.wConfigs.get(name);
        if (temp.Weathers__Enabled_Natural__Disasters_Wildfires) {
            Storm.manager.enableWeatherForWorld("storm_wildfire", name,
                    temp.Natural__Disasters_Wildfires_Chance__To__Start,
                    temp.Natural__Disasters_Wildfires_Wildfire__Base__Interval);
        }
    }

    @ReflectCommand.Command(
            name = "wildfire",
            usage = "/<command> [world]",
            permission = "storm.wildfire.command",
            sender = ReflectCommand.Sender.EVERYONE
    )
    public static boolean wildfireConsole(CommandSender sender, String world) {
        if (wildfireConsole(world)) {
            sender.sendMessage(ChatColor.RED + "Wildfires are not enabled in specified world or are conflicting with another weather!");
            return true;
        }
        return false;
    }

    @SuppressWarnings("SameReturnValue")
    @ReflectCommand.Command(
            name = "wildfire",
            permission = "storm.wildfire.command",
            sender = ReflectCommand.Sender.PLAYER
    )
    public static boolean wildfirePlayer(Player sender) {
        wildfire(sender.getTargetBlock(null, 0).getLocation());
        return true;
    }

    private static boolean wildfireConsole(String world) {
        try {
            if (Storm.manager.getActiveWeathers(world).contains("storm_wildfire")) {
                Storm.manager.stopWeather("storm_wildfire", world);
                return false;
            } else {
                return Storm.manager.startWeather("storm_wildfire", world);
            }
        } catch (Exception ex) {
            return true;
        }
    }

    private static void wildfire(Location targetLoc) {
        org.bukkit.block.Block fire = targetLoc.getBlock().getRelative(BlockFace.UP);
        fire.setType(Material.FIRE);
        String world = targetLoc.getWorld().getName();
        if (wildfireBlocks.containsKey(world)) {
            wildfireBlocks.get(world).add(fire);
            return;
        }
        getWFBlocks(world).add(fire);
    }

    /**
     * Returns a Set of all fire blocks involved in a wildfire in given world.
     *
     * @param world The World name
     * @return A set of wildfire blocks
     */
    public static Set<Block> getWFBlocks(String world) {
        Set<Block> set = wildfireBlocks.get(world);
        if (set == null) {
            set = new HashSet<Block>();
            wildfireBlocks.put(world, set);
        }
        return set;
    }
}
