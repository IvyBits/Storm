package com.github.StormTeam.Storm.Blizzard;

import java.util.HashMap;

import org.bukkit.World;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Blizzard.Listeners.BlizzardListeners;

public class Blizzard {

    public static HashMap<World, Boolean> blizzardingWorlds = new HashMap<World, Boolean>();

    public static void load(Storm storm) {
        
        Storm.pm.registerEvents(new BlizzardListeners(storm), storm);
        ModSnow.mod(true);

    }

    public static void unload() {
        ModSnow.mod(false);
    }
}