package com.github.StormTeam.Storm.Acid_Rain;

import org.bukkit.World;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Acid_Rain.Listeners.AcidListener;
import java.util.ArrayList;

public class AcidRain
{
    public static ArrayList<World> acidicWorlds = new ArrayList<World>();

    public static void load(Storm storm)
    {	
	Storm.pm.registerEvents(new AcidListener(storm), storm);
    }    

}