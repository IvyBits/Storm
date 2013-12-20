package tk.ivybits.storm.utility.block;

import tk.ivybits.storm.Storm;
import tk.ivybits.storm.utility.data.Pair;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An object for returning large lists of pseudorandom blocks at high speeds.
 *
 * @author Icyene
 */

public class BlockTickSelector {
    private final World world;
    private int chan;
    private int radius;

    public BlockTickSelector(World world, int selChance, int rad) {
        this.world = world;
        this.radius = rad;
        this.chan = selChance;
    }

    public BlockTickSelector(World world, int selChance) {
        this(world, selChance, 16);
    }

    ArrayList<Pair<Integer, Integer>> getRandomTickedChunks() {
        ArrayList<Pair<Integer, Integer>> doTick = new ArrayList<Pair<Integer, Integer>>();

        if (world.getPlayers().isEmpty()) {
            return doTick;
        }

        List<Chunk> loadedChunks = Arrays.asList(world.getLoadedChunks());
        List<Player> players = world.getPlayers();
        int len = players.size();
        for (int i = 0; i != len; i++) {
            Player entityhuman = players.get(i);
            int eX = (int) Math.floor(entityhuman.getLocation().getBlockX() / 16),
                    eZ = (int) Math.floor(entityhuman.getLocation().getBlockZ() / 16);
            byte range = 7;
            for (int x = -range; x <= range; x++) {
                for (int z = -range; z <= range; z++) {
                    if (loadedChunks.contains(world.getChunkAt(x + eX, z + eZ))) {
                        doTick.add(new Pair<Integer, Integer>(x + eX, z + eZ));
                    }
                }
            }
        }
        return doTick;
    }

    /**
     * Fetches a random list of ticked blocks.
     */

    public ArrayList<Block> getRandomTickedBlocks() {
        ArrayList<Block> doTick = new ArrayList<Block>();
        ArrayList<Pair<Integer, Integer>> ticked = getRandomTickedChunks();

        if (ticked.isEmpty()) {
            return doTick;
        }

        int len = ticked.size() / 2;
        for (int i = 0; i != len; i++) {
            Pair<Integer, Integer> pair = ticked.get(i);
            int xOffset = pair.LEFT << 4, zOffset = pair.RIGHT << 4;
            Chunk chunk = world.getChunkAt(pair.LEFT, pair.RIGHT);
            chunk.load();
            if (Storm.random.nextInt(100) <= chan) {
                int x = Storm.random.nextInt(radius), z = Storm.random.nextInt(radius);
                doTick.add(world.getBlockAt(x + xOffset, world.getHighestBlockYAt(x + xOffset, z + zOffset), z + zOffset));
            }
        }
        return doTick;
    }
}
