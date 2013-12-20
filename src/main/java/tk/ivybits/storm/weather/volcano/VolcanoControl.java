/*
 * This file is part of Storm.
 *
 * Storm is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Storm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Storm.  If not, see
 * <http://www.gnu.org/licenses/>.
 */


package tk.ivybits.storm.weather.volcano;

import com.google.common.io.Files;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import tk.ivybits.storm.Storm;
import tk.ivybits.storm.utility.ErrorLogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class VolcanoControl implements Listener {
    static public final List<VolcanoWorker> volcanoes = new ArrayList<VolcanoWorker>();
    static final HashMap<String, ArrayList<Integer>> volcanoBlockCache = new HashMap<String, ArrayList<Integer>>();
    static final Object mutex = new Object();

    public void forget() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void coolLava(BlockFromToEvent e) {
        Block from = e.getBlock();
        for (VolcanoWorker volcano : volcanoes) {
            if ((from.getTypeId() & 0xfe) == 0xa && volcano.active && volcano.ownsBlock(from)) { //Checks if the block is lava and if a volcano owns it
                solidify(volcano, from, randomVolcanoBlock(from.getWorld()));
                if (!volcano.ownsBlock(e.getToBlock()))
                    volcano.grower.stop(); //Reached boundaries
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void unloadChunk(ChunkUnloadEvent e) {
        Chunk c = e.getChunk();
        for (VolcanoWorker volcano : volcanoes) {
            if (volcano.area.contains(c)) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void unloadWorld(WorldUnloadEvent e) {
        for (VolcanoWorker vulk : volcanoes) {
            if (vulk.world.equals(e.getWorld())) {
                vulk.active = false;
                dumpVolcanoes();
            }
        }
    }

    static void solidify(VolcanoWorker vulc, Block lava, int idTo) {
        int data;
        if ((data = lava.getData()) != 0x9)
            vulc.area.setBlockFastDelayed(lava, idTo, (byte) 0, ((data & 0x8) == 0x8 ? 1 : 4 - data / 2) * 20 * 2);
    }

    static ArrayList<Integer> getVolcanoBlock(String world) {
        if (!volcanoBlockCache.containsKey(world))
            volcanoBlockCache.put(world, new ArrayList() {
                {
                    for (int i = 0; i < 100; ++i)
                        add(Material.STONE.getId());
                }
            });
        return volcanoBlockCache.get(world);
    }

    static int randomVolcanoBlock(String world) {
        ArrayList<Integer> choices = getVolcanoBlock(world);
        return choices.get(Storm.random.nextInt(choices.size()));
    }

    static int randomVolcanoBlock(World world) {
        return randomVolcanoBlock(world.getName());
    }

    public static void dumpVolcanoes() {
        try {
            VolcanoControl.save(Volcano.vulkanos);
        } catch (Exception e) {
            ErrorLogger.alert(e);
        }
    }

    public static void save(final File dump) {
        synchronized (mutex) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(Storm.instance, new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!dump.exists())
                            dump.createNewFile();
                        String contents = "";
                        for (VolcanoWorker vulc : volcanoes) {
                            if (Storm.wConfigs.get(vulc.world.getName()).Natural__Disasters_Volcano_Features_Survive__Restarts)
                                contents = contents + vulc.toString();
                        }
                        BufferedWriter writer = new BufferedWriter(new FileWriter(dump));
                        writer.write(contents);
                        writer.close();
                    } catch (Exception e) {
                        ErrorLogger.alert(e);
                    }
                }
            }
            );
        }
    }

    public static void load(File file) throws IOException {
        if (!file.exists())
            file.createNewFile();
        String contents = Files.toString(file, Charset.defaultCharset());
        if (!StringUtils.isEmpty(contents))
            for (String vulc : Arrays.asList(contents.split("\n"))) {
                VolcanoWorker maker = new VolcanoWorker();
                maker.fromString(vulc);
                maker.start();
                volcanoes.add(maker);
            }
    }
}
