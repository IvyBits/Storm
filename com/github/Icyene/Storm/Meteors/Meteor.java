package com.github.Icyene.Storm.Meteors;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.logging.Level;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.StormUtil;
import com.github.Icyene.Storm.Meteors.Entities.EntityMeteor;

public class Meteor {

    static Class<?> EntityTypesClass;
    public static int recalculatorScheduleId = -1;
    public static final Random rand = new Random();
    public static Storm storm;

    public static void load(Storm sStorm) {
	storm = sStorm;
	
	if(!GlobalVariables.snow_insubstantial_enabled) {  
	    return;
	}
	
	 Storm.stats.add("Meteors");
	
	try {

	    Method a = net.minecraft.server.EntityTypes.class
		    .getDeclaredMethod("a", Class.class, String.class,
			    int.class);
	    a.setAccessible(true);

	    a.invoke(a, EntityMeteor.class, "Fireball", 12);

	} catch (Exception e) {
	    StormUtil.log(Level.SEVERE, "Failed to create meteor entity!");
	}
	
	 MeteorSpawner.load(sStorm);
	
    }

}
