package com.github.stormproject.storm.weather.wildfire;

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.WorldVariables;
import com.github.stormproject.storm.utility.StormUtil;
import com.github.stormproject.storm.weather.StormWeather;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * A wildfire weather object.
 */
public class WildfireWeather extends StormWeather { //TODO: Make use of getTickedBlock(Chunk) to get better ranges of wildfires 

    private final WorldVariables glob;
    private final World affectedWorld;

    /**
     * Creates a wildfire weather object for given world.
     *
     * @param world The world this object will be handling
     */

    public WildfireWeather(String world) {
        super(world);
        affectedWorld = Bukkit.getWorld(world);
        glob = Storm.wConfigs.get(world);
        autoKillTicks = 7500 + Storm.random.nextInt(1024);
    }

    @Override
    public boolean canStart() {
        return glob.Weathers__Enabled_Natural__Disasters_Wildfires;
    }

    /**
     * Called when wildfire starts for the handled world.
     */
    @Override
    public void start() {
        Block toBurn;
        int recurse = 0;
        Chunk chunk;
        while (true) {
            recurse++;
            if (recurse <= 40 && (chunk = StormUtil.pickChunk(affectedWorld)) != null) {
                int x = Storm.random.nextInt(15), z = Storm.random.nextInt(15);
                toBurn = chunk.getWorld().getHighestBlockAt(chunk.getBlock(x, 4, z).getLocation()).getLocation().subtract(0, 1, 0).getBlock();

                if (!StormUtil.isBlockProtected(toBurn)
                        && StormUtil.isForest(toBurn.getBiome())
                        && Storm.wConfigs.get(toBurn.getWorld().getName()).Natural__Disasters_Wildfires_Flammable__Blocks.contains(toBurn.getTypeId())) {

                    toBurn = toBurn.getLocation().add(0, 1, 0).getBlock();

                    if (Wildfire.wildfireBlocks.containsKey((world = toBurn.getWorld().getName()))) {
                        Wildfire.wildfireBlocks.get(world).add(toBurn);
                        toBurn.setType(Material.FIRE);
                        StormUtil.broadcast(glob.Natural__Disasters_Wildfires_Messages_On__Start.replace("%x", toBurn.getX() + "")
                                .replace("%y", toBurn.getY() + "").replace("%z", toBurn.getZ() + ""), affectedWorld);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Called when wildfire ends for the handled world.
     */
    @Override
    public void end() {

    }
}
