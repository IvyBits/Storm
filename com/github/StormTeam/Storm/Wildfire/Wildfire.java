package com.github.StormTeam.Storm.Wildfire;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Wildfire.Listeners.WildfireListeners;
import com.github.StormTeam.Storm.Wildfire.Tasks.Igniter;

public class Wildfire {

    public static HashMap<World, List<org.bukkit.block.Block>> wildfireBlocks = new HashMap<World, List<org.bukkit.block.Block>>();
    public static List<Integer> flammableList = Arrays
            .asList(new Integer[]{
                net.minecraft.server.Block.FENCE.id, net.minecraft.server.Block.WOOD.id, net.minecraft.server.Block.WOOD_STAIRS.id,
                net.minecraft.server.Block.WOODEN_DOOR.id, net.minecraft.server.Block.LEAVES.id, net.minecraft.server.Block.BOOKSHELF.id,
                net.minecraft.server.Block.GRASS.id, net.minecraft.server.Block.WOOL.id, net.minecraft.server.Block.VINE.id});
    public static Storm storm;
    private static CommandExecutor exec;

    public static void load(Storm ztorm) {
        storm = ztorm;
        Storm.pm.registerEvents(new WildfireListeners(), storm);

        for (World w : storm.getServer().getWorlds()) {
            if (Storm.wConfigs.get(w).Features_Wildfires) {
                new Igniter(storm, w).run();
            }
        }

        exec = new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
                if ((sender instanceof Player)) {
                    wildfire(((Player) sender).getTargetBlock(null, 0).getLocation());
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "Sorry, you must be in-game to do that!");
                }
                return false;
            }
        };

        ztorm.getCommand("wildfire").setExecutor(exec);

    }

    public static void wildfire(Location targetLoc) {
        Block fire = targetLoc.getBlock().getRelative(BlockFace.UP);
        fire.setType(Material.FIRE);
        World world = targetLoc.getWorld();
        if (wildfireBlocks.containsKey(world)) {
            wildfireBlocks.get(world).add(fire);
            return;
        }
        wildfireBlocks.put(world, Arrays.asList(fire));
    }
}
