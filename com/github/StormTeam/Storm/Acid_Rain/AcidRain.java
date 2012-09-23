package com.github.StormTeam.Storm.Acid_Rain;

import java.util.HashMap;
import org.bukkit.World;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Acid_Rain.Listeners.AcidListener;

public class AcidRain
{
    public static HashMap<String, Boolean> acidicWorlds = new HashMap<String, Boolean>();

    public static void load(Storm storm)
    {	
	Storm.pm.registerEvents(new AcidListener(storm), storm);
    }    

}