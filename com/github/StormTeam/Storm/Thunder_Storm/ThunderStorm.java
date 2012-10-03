package com.github.StormTeam.Storm.Thunder_Storm;

/**
 *
 * @author Tudor
 */
import org.bukkit.World;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Thunder_Storm.Listeners.ThunderListener;
import java.util.ArrayList;

public class ThunderStorm
{
    public static ArrayList<World> thunderingWorlds = new ArrayList<World>();

    public static void load(Storm storm)
    {	        
	Storm.pm.registerEvents(new ThunderListener(storm), storm);
    }    

}