/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.StormTeam.Storm.API;

import java.util.Set;
import org.bukkit.World;

/**
 * Base class of all Weathers
 */
public abstract class StormWeather {
    public StormWeather(String world) {
        this.world = world;
    }
    public abstract void start();
    public abstract void end();
    public abstract String getTexture();
    
    public static final Boolean conflicts(String other) {
        return CONFLICTS.contains(other);
    }
    
    public static Set<String> CONFLICTS;
    public static String world;
}
