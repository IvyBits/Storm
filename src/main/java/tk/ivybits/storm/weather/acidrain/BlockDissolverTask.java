package tk.ivybits.storm.weather.acidrain;

import tk.ivybits.storm.Storm;
import tk.ivybits.storm.WorldVariables;
import tk.ivybits.storm.utility.StormUtil;
import tk.ivybits.storm.utility.block.BlockTickSelector;
import tk.ivybits.storm.utility.block.BlockTransformer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

/**
 * An object for dissolving blocks during acid rain.
 *
 * @author Icyene
 */

public class BlockDissolverTask implements Runnable {

    private int id;
    private final WorldVariables glob;
    private BlockTickSelector ticker;

    private final List<BlockTransformer> transformations = new ArrayList<BlockTransformer>();

    public BlockDissolverTask(String affectedWorld) {
        glob = Storm.wConfigs.get(affectedWorld);
        ticker = new BlockTickSelector(Bukkit.getWorld(affectedWorld),
                glob.Acid__Rain_Dissolver_Block__Deterioration__Chance, glob.Acid__Rain_Dissolver_Block__Deterioration__Area);
        for (List<String> trans : glob.Acid__Rain_Dissolver_Block__Transformations) {
            transformations.add(new BlockTransformer(new BlockTransformer.IDBlock(trans.get(0)), new BlockTransformer.IDBlock(trans.get(1))));
        }
    }

    @Override
    public void run() {
        List<Block> tb = ticker.getRandomTickedBlocks();
        for (Block b : tb) {
            Block tran = b.getRelative(BlockFace.DOWN);
            if (tran.getTypeId() != 0 && StormUtil.isRainy(tran.getBiome()) && !StormUtil.isBlockProtected(tran)) {
                for (BlockTransformer t : transformations) {
                    if (t.transform(tran)) {
                        StormUtil.playSoundNearby(tran.getLocation(), 5, Sound.FIZZ, 1F, 1F);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Starts the task.
     */
    public void start() {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Storm.instance, this, 0, glob.Acid__Rain_Scheduler_Dissolver__Calculation__Intervals__In__Ticks);
    }

    /**
     * Ends the task.
     */
    public void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }
}
