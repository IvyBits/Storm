package com.github.stormproject.storm.weather.wildfire;

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.WorldVariables;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

import static com.github.stormproject.storm.weather.wildfire.Wildfire.getWFBlocks;

/**
 * Handles fire events for wildfires. The epitome of unreadability.
 */

public class WildfireControl implements Listener {

    /**
     * Checks for other blocks to burn if event involves a block in wildfire.getWFBlocks().
     *
     * @param event The BlockIgniteEvent being handled
     */

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (!event.getCause().equals(IgniteCause.SPREAD))
            return;

        Location loc = event.getBlock().getLocation();
        int ox = (int) loc.getX(), oy = (int) loc.getY(), oz = (int) loc.getZ();
        World world = loc.getWorld();
        String name = world.getName();

        WorldVariables glob = Storm.wConfigs.get(name);
        if (getWFBlocks(name).size() <= glob.Natural__Disasters_Wildfires_Maximum__Fires) {
            int radiuski = glob.Natural__Disasters_Wildfires_Scan__Radius;
            for (int x = -radiuski; x <= radiuski; ++x) {
                for (int y = -radiuski; y <= radiuski; ++y) {
                    for (int z = -radiuski; z <= radiuski; ++z) {
                        if (getWFBlocks(name).contains(new Location(world, x + ox, y + oy, z + oz).getBlock())) {
                            scanForIgnitables(loc, world, radiuski, glob.Natural__Disasters_Wildfires_Spread__Limit);
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     * Removes blocks involved in a wildfire from wildfire.getWFBlocks().
     *
     * @param event The BlockFadeEvent being listened to
     */

    @EventHandler
    public void onBlockEx(BlockFadeEvent event) {
        Block faded = event.getBlock();
        getWFBlocks(faded.getWorld().getName()).remove(faded);
    }

    private void scanForIgnitables(Location loc, World w, int radiuski, int spreadLimit) {
        Block block;
        int spread = 0;

        for (int x = -radiuski; x <= radiuski; ++x) {
            for (int y = -radiuski; y <= radiuski; ++y) {
                for (int z = -radiuski; z <= radiuski; ++z) {
                    if ((block = w.getBlockAt(loc.getBlockX() + x, loc.getBlockX() + y, loc.getBlockX() + z)).getTypeId() != 0)
                        continue;

                    // Tries to burn all blocks with one face touching `block` and `block` itself
                    for (int i = -1; i < 6; ++i) {
                        if (spread < spreadLimit) {
                            burn(block.getRelative(
                                    i >> 1 == 0 ? ((i & 1) == 0 ? 1 : -1) : 0,
                                    i >> 1 == 1 ? ((i & 1) == 0 ? 1 : -1) : 0,
                                    i >> 1 == 2 ? ((i & 1) == 0 ? 1 : -1) : 0)
                            );
                            ++spread;
                        }
                    }
                }
            }
        }
    }

    void burn(final Block toBurn) {
        if (canBurn(toBurn)) {
            toBurn.setType(Material.FIRE);
            getWFBlocks(toBurn.getWorld().getName()).add(toBurn);
        }
    }

    boolean canBurn(Block toCheck) {
        return Storm.wConfigs.get(toCheck.getWorld().getName()).Natural__Disasters_Wildfires_Flammable__Blocks.contains(toCheck.getTypeId());
    }
}
