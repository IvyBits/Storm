package tk.ivybits.storm.weather.quake;

import tk.ivybits.storm.Storm;
import tk.ivybits.storm.WorldVariables;
import tk.ivybits.storm.utility.StormUtil;
import tk.ivybits.storm.utility.Verbose;
import tk.ivybits.storm.utility.block.Cuboid;
import tk.ivybits.storm.utility.math.Cracker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.List;

public class RuptureTask implements Runnable {

    private Location location;
    private int length;
    private final int width;
    private int id;
    private int layerIndex = 0;
    private Cracker cracker;
    private Cuboid area;
    private World world;
    private WorldVariables glob;

    public RuptureTask(Cuboid cube, Location loc, int ruptureLength, int ruptureWidth, int depth) {
        location = loc;
        world = location.getWorld();
        length = ruptureLength;
        width = ruptureWidth;
        area = cube;
        glob = Storm.wConfigs.get(world.getName());

        cracker = new Cracker(length, location.getBlockX(), location.getBlockY(), location.getBlockZ(), width, depth);
        cracker.plot();
    }

    public void run() {
        Verbose.log("Cracking layer " + layerIndex);
        List<Vector> layer = cracker.get(layerIndex);
        if (layer.size() == 0) {
            stop();
            return;
        }
        for (Vector block : layer) {
            BlockIterator bi = new BlockIterator(world, block, new Vector(0, 1, 0), 0, (256 - block.getBlockY()));
            while (bi.hasNext()) {
                Block toInspect = bi.next();
                if (StormUtil.isBlockProtected(toInspect))
                    continue;
                int bid = toInspect.getTypeId();
                if (bid != 0 && id != 7)
                    area.setBlockFast(toInspect, 0);
                else if ((bid & 0xFE) == 8) // 8 or 9
                    toInspect.setTypeId(9, true);
                else if ((bid & 0xFE) == 10) // 10 or 11
                    toInspect.setTypeId(10, true);
                else if (bid == 55)
                    toInspect.setTypeId(0, true);
                else if (bid == 52)
                    toInspect.setTypeId(0, true);
            }
        }
        StormUtil.playSoundNearby(location, (length * width) / 2, Sound.AMBIENCE_THUNDER, 1F, Storm.random.nextInt(3) + 1);
        area.sendClientChanges();
        ++layerIndex;
    }

    public void start() {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Storm.instance, this, 0, 20);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }
}
