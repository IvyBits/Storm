package com.github.StormTeam.Storm.Weather;

import com.github.StormTeam.Storm.Storm;
import java.util.Set;

/**
 * Base class of all Weathers
 */
public abstract class StormWeather {
    /**
     * Constructor, should not be overriden. To execute code on construction,
     * override initialize().
     * 
     * @param storm
     * @param world 
     */
    public StormWeather(Storm storm, String world) {
        this.storm = storm;
        this.world = world;
    }
    
    /**
     * Called on initialization of class.
     */
    public void initialize() {}
    
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
    public abstract String getTexture();
    
    /**
     * Function to check if this weather declares itself to be conflict with
     * the other.
     * 
     * @param other name of other weather
     * @return boolean of conflict
     */
    public static final Boolean conflicts(String other) {
        return CONFLICTS.contains(other);
    }
    
    /**
     * Initialize this to a Set<String> containing the known conflicting
     * weathers.
     */
    public static Set<String> CONFLICTS;
    
    /**
     * Stores the world name this class manages.
     */
    public String world;
    
    /**
     * The storm plugin object.
     */
    public Storm storm;
}
