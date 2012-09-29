package com.github.StormTeam.Storm.Blizzard;

import java.util.HashMap;

import org.bukkit.World;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Blizzard.Listeners.BlizzardListeners;
import java.util.ArrayList;

public class Blizzard {

    public static ArrayList<World> blizzardingWorlds = new ArrayList<World>();

    public static void load(Storm storm) {
        
        Storm.pm.registerEvents(new BlizzardListeners(storm), storm);
        ModSnow.mod(true);

    }

    public static void unload() {
        ModSnow.mod(false);
    }
}