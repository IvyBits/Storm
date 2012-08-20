package com.github.Icyene.Storm.Wildfire;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Wildfire.Listeners.FireEvent;

public class Wildfire {

    public static void load(Storm storm) {
	try {

	    if(GlobalVariables.Features_Wildfires) {
	    storm.getServer().getPluginManager()
		    .registerEvents(new FireEvent(), storm);
	    }

	} catch (Exception e) {
	};

    }
}
