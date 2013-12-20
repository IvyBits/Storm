package tk.ivybits.storm.weather.volcano;

import tk.ivybits.storm.Storm;
import tk.ivybits.storm.utility.Verbose;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class GrowthTask implements Runnable {

    private int id;
    private final VolcanoWorker volcano;
    private int heightCap;

    public GrowthTask(VolcanoWorker volcano) {
        this.volcano = volcano;
        this.heightCap = volcano.radius * 2 + volcano.y;
    }

    @Override
    public void run() {
        if (!volcano.active)
            return;
        if (volcano.layer >= heightCap) {
            stop();
            return;
        }

        //  volcano.recalculateLayer();
        Block set = volcano.center.clone().add(0, volcano.layer, 0).getBlock();
        if (!volcano.area.contains(set)) {
            stop();
            return;
        } else {
            Verbose.log("volcano no contain block.");
        }
        set.setType(Material.LAVA);
        volcano.area.sendClientChanges();
        volcano.layer++;
        VolcanoControl.dumpVolcanoes();
    }

    public void start() {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Storm.instance, this, 0, 300);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }
}
