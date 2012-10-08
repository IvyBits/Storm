package com.github.StormTeam.Storm.Weather;

import com.github.StormTeam.Storm.Storm;
import java.util.Set;

/**
 * Base class of all Weathers
 */
public abstract class StormWeather {
    public StormWeather(Storm storm, String world) {
        this.storm = storm;
        this.world = world;
    }
    public abstract void start();
    public abstract void end();
    public abstract String getTexture();
    
    public static final Boolean conflicts(String other) {
        return CONFLICTS.contains(other);
    }
    
    public static Set<String> CONFLICTS;
    public String world;
    public Storm storm;
}
