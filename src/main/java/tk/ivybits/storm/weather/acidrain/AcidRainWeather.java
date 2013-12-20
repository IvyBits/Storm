package tk.ivybits.storm.weather.acidrain;

import tk.ivybits.storm.Storm;
import tk.ivybits.storm.WorldVariables;
import tk.ivybits.storm.utility.StormUtil;
import tk.ivybits.storm.weather.StormWeather;

import java.util.HashSet;
import java.util.Set;

/**
 * An acidic weather object.
 */

public class AcidRainWeather extends StormWeather {

    private final WorldVariables glob;
    private EntityDamagerTask enDamager;
    private BlockDissolverTask dissolver;

    public AcidRainWeather(String world) {
        super(world);
        glob = Storm.wConfigs.get(world);
        needRainFlag = true;
        autoKillTicks = 7500 + Storm.random.nextInt(1024);
    }

    @Override
    public boolean canStart() {
        return glob.Weathers__Enabled_Acid__Rain;
    }

    /**
     * Called when acid rain starts for the handled world.
     */

    @Override
    public void start() {
        StormUtil.broadcast(glob.Acid__Rain_Messages_On__Acid__Rain__Start, bukkitWorld);

        if (glob.Acid__Rain_Features_Entity__Damaging || glob.Blizzard_Features_Player__Damaging) {
            enDamager = new EntityDamagerTask(world);
            enDamager.start();
        }

        if (glob.Acid__Rain_Features_Dissolving__Blocks) {
            dissolver = new BlockDissolverTask(world);
            dissolver.start();
        }
    }

    /**
     * Called when acid rain ends for the handled world.
     */

    @Override
    public void end() {
        StormUtil.broadcast(glob.Acid__Rain_Messages_On__Acid__Rain__Stop, bukkitWorld);
        if (enDamager != null)
            enDamager.stop();
        enDamager = null;
        if (dissolver != null)
            dissolver.stop();
        dissolver = null;
    }

    /**
     * Returns the texture to be used during this event.
     *
     * @return The path to the texture
     */

    @Override
    public String getTexture() {
        return glob.Textures_Acid__Rain__Texture__Path;
    }

    /**
     * Acid rain conflicts with blizzards because of textures: don't allow to run at same time.
     *
     * @return A set containing the String "storm_blizzard"
     */

    @Override
    public Set<String> getConflicts() {
        HashSet<String> temp = new HashSet<String>();
        temp.add("storm_blizzard");
        return temp;
    }
}
