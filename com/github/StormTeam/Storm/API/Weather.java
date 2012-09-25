package com.github.StormTeam.Storm.API;

import org.bukkit.World;

/**
 *
 * @author Tudor
 */
public interface Weather {
    
    public void setup();
    public boolean start(World world);
    public void end();
    public String getTexture();
    
}
