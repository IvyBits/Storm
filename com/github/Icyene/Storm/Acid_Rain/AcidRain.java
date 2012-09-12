package com.github.Icyene.Storm.Acid_Rain;

import java.util.HashMap;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Acid_Rain.Listeners.AcidListener;

public class AcidRain
{
    public static HashMap<World, Boolean> acidicWorlds = new HashMap<World, Boolean>();

    public static void load(Storm storm)
    {
	PluginManager pm = storm.getServer().getPluginManager();
	pm.registerEvents(new AcidListener(storm), storm);
    }    

}