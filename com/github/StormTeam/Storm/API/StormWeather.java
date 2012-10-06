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
    public abstract void setup();
    public abstract boolean start(World world);
    public abstract void end();
    public abstract String getTexture();
    public Set<String> conflicts;
}
