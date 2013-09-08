package com.github.stormproject.storm.weather.thunderstorm;

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.WorldVariables;
import com.github.stormproject.storm.utility.StormUtil;
import com.github.stormproject.storm.weather.StormWeather;

/**
 * A thunder storm weather object.
 */

public class ThunderStormWeather extends StormWeather {

    private final WorldVariables glob;
    private StrikerTask striker;

    /**
     * Creates a thunder storm weather object for given world.
     *
     * @param world The world this object will be handling
     */

    public ThunderStormWeather(String world) {
        super(world);
        glob = Storm.wConfigs.get(world);
        needRainFlag = true;
        autoKillTicks = 7500 + Storm.random.nextInt(1024);

    }

    /**
     * Called when thunder storm starts for the handled world.
     */

    @Override
    public boolean canStart() {
        return glob.Weathers__Enabled_Thunder__Storms;
    }

    @Override
    public void start() {
        StormUtil.broadcast(glob.Thunder__Storm_Messages_On__Thunder__Storm__Start, bukkitWorld);

        if (glob.Thunder__Storm_Features_Thunder__Striking) {
            striker = new StrikerTask(bukkitWorld);
            striker.start();
        }
    }

    /**
     * Called when thunder storm ends for the handled world.
     */

    @Override
    public void end() {
        StormUtil.broadcast(glob.Thunder__Storm_Messages_On__Thunder__Storm__Stop, bukkitWorld);
        if (striker != null)
            striker.stop();
        striker = null; //Remove references
    }
}
