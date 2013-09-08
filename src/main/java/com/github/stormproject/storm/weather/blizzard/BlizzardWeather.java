package com.github.stormproject.storm.weather.blizzard;

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.WorldVariables;
import com.github.stormproject.storm.utility.StormUtil;
import com.github.stormproject.storm.weather.StormWeather;

import java.util.Set;

/**
 * A blizzard weather object.
 */

public class BlizzardWeather extends StormWeather {

    private final WorldVariables glob;
    private EntityDamagerTask enDamager;

    /**
     * Creates a blizzard weather object for given world.
     *
     * @param world The world this object will be handling
     */
    public BlizzardWeather(String world) {
        super(world);
        glob = Storm.wConfigs.get(world);
        needRainFlag = true;
        autoKillTicks = 7500 + Storm.random.nextInt(1024);
    }

    @Override
    public boolean canStart() {
        return glob.Weathers__Enabled_Blizzards;
    }

    /**
     * Called when a blizzard starts for the handled world.
     */
    @Override
    public void start() {
        StormUtil.broadcast(glob.Blizzard_Messages_On__Blizzard__Start, bukkitWorld);
        if (glob.Blizzard_Features_Entity__Damaging || glob.Blizzard_Features_Player__Damaging) {
            enDamager = new EntityDamagerTask(world);
            enDamager.start();
        }
    }

    /**
     * Called when acid rain ends for the handled world.
     */
    @Override
    public void end() {
        StormUtil.broadcast(glob.Blizzard_Messages_On__Blizzard__Stop, bukkitWorld);
        StormUtil.setRainNoEvent(bukkitWorld, false);
        if (enDamager != null)
            enDamager.stop();
        enDamager = null;
    }


    /**
     * Returns the texture to be used during this event.
     *
     * @return The path to the texture
     */
    @Override
    public String getTexture() {
        return glob.Textures_Blizzard__Texture__Path;
    }

    /**
     * Blizzards conflicts with acid rain because of textures: don't allow to run at same time.
     *
     * @return A set containing the String "storm_acidrain"
     */
    @Override
    public Set<String> getConflicts() {
        return StormUtil.asSet("storm_acidrain"); //Yay.
    }
}
