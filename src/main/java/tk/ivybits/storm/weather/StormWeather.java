package tk.ivybits.storm.weather;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Collections;
import java.util.Set;

/**
 * Base class of all Weathers
 */
public abstract class StormWeather {
    /**
     * Constructor. DO NOT CHANGE ARGUMENTS.
     *
     * @param world World name to act opon
     */
    public StormWeather(String world) {
        this.world = world;
        this.bukkitWorld = Bukkit.getWorld(world);
    }

    public boolean canStart() {
        return true;
    }

    /**
     * Called when the weather is started.
     */
    public abstract void start();

    /**
     * Called when the weather is stopped.
     */
    public abstract void end();

    /**
     * Gets the texture of the weather.
     *
     * @return URL to texture if there is a texture, else null.
     */
    public String getTexture() {
        return null;
    }

    /**
     * Gets a Set<String> of conflicting weathers.
     *
     * @return A Set<String> of conflicting weathers.
     */
    @SuppressWarnings("UnusedDeclaration")
    public Set getConflicts() {
        return Collections.EMPTY_SET;
    }

    public void setState(Object[] args) {
    }

    /**
     * Number of ticks before this weather is auto-killed.
     */
    public int autoKillTicks = -1;

    /**
     * Set to true to enable minecraft rain mode.
     */
    public boolean needRainFlag = false;

    /**
     * Set to true to enable minecraft thunder mode.
     */
    public boolean needThunderFlag = false;

    /**
     * Stores the world name this class manages.
     */
    protected String world;

    protected final World bukkitWorld;
}
