package com.github.stormproject.storm.weather.volcano;

import com.github.stormproject.storm.Storm;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

/**
 * @author Icyene, xiaomao
 */

public class EruptTask implements Runnable {

    private int id;
    private final VolcanoWorker volcano;

    public EruptTask(VolcanoWorker volcano) {
        this.volcano = volcano;
    }

    @Override
    public void run() {
        if (volcano.layer < 10 || !(Storm.random.nextInt(100) > 70) || !volcano.active)
            return;

        // volcano.recalculateLayer();
        Location er = volcano.center.clone();
        er.setY(volcano.center.getBlockY() + volcano.layer);

        if (Storm.random.nextInt(100) > 95)
            volcano.explode(er, volcano.layer / 10 + 2);
        int rand = Storm.random.nextInt(1) + 1;
        for (int i = 0; i != rand; i++) {
            FallingBlock b = volcano.world.spawnFallingBlock(er, 11, (byte) 0);
            b.setDropItem(false);
            b.setVelocity(new Vector(Math.random() - 0.5, 0.3, Math.random() - 0.5));
        }
    }

    public void start() {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Storm.instance, this, 20, 20);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }
}
