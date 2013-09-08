package com.github.stormproject.storm.weather.quake;

import com.github.stormproject.storm.Storm;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

/**
 * Task to shake every non-creative player
 *
 * @author Giant, Icyene
 */
public class QuakeTask implements Runnable {

    private Quake quake;
    private boolean toggle;
    private int id;

    public QuakeTask(Quake eq) {
        quake = eq;
    }

    @Override
    public void run() {
        if (Storm.wConfigs.get(quake.world.getName()).Natural__Disasters_Earthquake_Screen__Shaking)
            for (Player p : quake.world.getPlayers()) {
                List<MetadataValue> meta = p.getMetadata("storm_speed");
                if (p.getGameMode() == GameMode.CREATIVE || !quake.isQuaking(p.getLocation())) {
                    if (meta.size() > 0) {
                        p.setWalkSpeed(meta.get(0).asFloat());
                        p.removeMetadata("storm_speed", Storm.instance);
                    }
                    continue;
                }

                if (p.getMetadata("storm_speed").size() == 0)
                    p.setMetadata("storm_speed", new FixedMetadataValue(Storm.instance, p.getWalkSpeed()));

                // TODO
                if (toggle) {
                    p.setWalkSpeed(0.1F);
                } else {
                    p.setWalkSpeed(0.3F);
                }
                toggle = !toggle;
            }
    }

    public void start() {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Storm.instance, this, 0, 2);
    }

    public void stop() {
        for (Player p : quake.world.getPlayers()) {
            List<MetadataValue> meta = p.getMetadata("storm_speed");
            if (meta.size() > 0) {
                p.setWalkSpeed(meta.get(0).asFloat());
                p.removeMetadata("storm_speed", Storm.instance);
            }
        }
        Bukkit.getScheduler().cancelTask(id);
    }
}
