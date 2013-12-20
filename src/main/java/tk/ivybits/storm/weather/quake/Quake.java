package tk.ivybits.storm.weather.quake;

import tk.ivybits.storm.Storm;
import tk.ivybits.storm.utility.Verbose;
import org.bukkit.Location;
import org.bukkit.World;

public class Quake {
    public World world;
    public final Location epicenter;
    private EarthquakeControl controller;
    private QuakeTask quaker;
    public final int radius;
    public final int magnitude;

    public Quake(Location epic, int magn) {
        epicenter = epic;
        radius = magn * 100;
        magnitude = magn;
        world = epicenter.getWorld();
    }

    public void start() {
        // Blocks will bounce everywhere in the quake!
        Verbose.log("Starting quake!");
        controller = new EarthquakeControl();
        Storm.pm.registerEvents(controller, Storm.instance);
        quaker = new QuakeTask(this);
        quaker.start();

        // Get cracking!
        EarthquakeControl.crack(new Location(world, epicenter.getBlockX(), epicenter.getBlockY(), epicenter.getBlockZ()), (int) Math.sqrt(radius) * 3, magnitude, 32 + magnitude);
    }

    public void stop() {
        Verbose.log("Stopping quake!");
        EarthquakeControl.quakes.remove(this);
        if (controller != null && EarthquakeControl.quakes.isEmpty()) {
            controller.forget();
        }
        quaker.stop();
    }

    public boolean isQuaking(Location point) {
        return point.getWorld().equals(this.world) && epicenter.distance(point) <= radius;
    }
}
