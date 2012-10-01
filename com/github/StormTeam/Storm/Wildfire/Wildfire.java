package com.github.StormTeam.Storm.Wildfire;

import java.util.Arrays;
import java.util.List;

import net.minecraft.server.Block;

import org.bukkit.World;
import org.bukkit.block.Biome;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Wildfire.Listeners.WildfireListeners;
import com.github.StormTeam.Storm.Wildfire.Tasks.Igniter;
import java.util.HashMap;

public class Wildfire {

    public static HashMap<World, List<org.bukkit.block.Block>> wildfireBlocks = new HashMap<World, List<org.bukkit.block.Block>>();
    public static List<Integer> flammableList = Arrays
            .asList(new Integer[]{
                net.minecraft.server.Block.FENCE.id, net.minecraft.server.Block.WOOD.id, net.minecraft.server.Block.WOOD_STAIRS.id,
                net.minecraft.server.Block.WOODEN_DOOR.id, net.minecraft.server.Block.LEAVES.id, net.minecraft.server.Block.BOOKSHELF.id,
                net.minecraft.server.Block.GRASS.id, net.minecraft.server.Block.WOOL.id, net.minecraft.server.Block.VINE.id});

    public static void load(Storm storm) {

        Storm.pm.registerEvents(new WildfireListeners(), storm);
        for (World w : storm.getServer().getWorlds()) {
            if (Storm.wConfigs.get(w).Features_Wildfires) {
                new Igniter(storm, w).run();
            }
        }

    }
}
