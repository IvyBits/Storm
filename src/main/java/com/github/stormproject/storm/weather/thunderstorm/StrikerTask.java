package com.github.stormproject.storm.weather.thunderstorm;

/**
 *
 * @author Icyene
 */

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.WorldVariables;
import com.github.stormproject.storm.utility.StormUtil;
import com.github.stormproject.storm.utility.block.BlockTickSelector;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;

public class StrikerTask implements Runnable {

    private int id;
    private final WorldVariables glob;
    private BlockTickSelector ticker;
    private final World affectedWorld;

    public StrikerTask(World affectedWorld) {
        this.glob = Storm.wConfigs.get(affectedWorld.getName());
        this.affectedWorld = affectedWorld;
        ticker = new BlockTickSelector(affectedWorld, glob.Thunder__Storm_Strike__Chance);
    }

    @Override
    public void run() {
        ArrayList<Block> blocks = ticker.getRandomTickedBlocks();
        for (Block b : blocks) {
            Block tran = b.getRelative(BlockFace.DOWN);
            if (StormUtil.isRainy(tran.getBiome())) {
                affectedWorld.strikeLightning(tran.getLocation());
            }
        }
    }

    public void start() {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Storm.instance, this, 0, glob.Thunder__Storm_Scheduler_Striker__Calculation__Intervals__In__Ticks);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }
}
