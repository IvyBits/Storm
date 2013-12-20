package tk.ivybits.storm.weather.volcano;

import tk.ivybits.storm.Storm;
import org.bukkit.Bukkit;

public class LifeTask implements Runnable {

    private int id;

    @Override
    public void run() {
        int size = VolcanoControl.volcanoes.size();
        for (int i = 0; i != size; i++) {
            VolcanoWorker vulc = VolcanoControl.volcanoes.get(i);
            if (Storm.random.nextInt(100) > 90)
                vulc.delete();
            else if (Storm.random.nextInt(100) > 70 && !vulc.active)
                vulc.start();
            else if (Storm.random.nextInt(100) > 40)
                vulc.stop();
        }
    }

    public void start() {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Storm.instance, this, 36000, 36000);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }
}
