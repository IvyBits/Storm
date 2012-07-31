package com.github.Icyene.Storm.Meteors;

import java.lang.reflect.Method;
import java.util.logging.Level;

import com.github.Icyene.Storm.StormUtil;
import com.github.Icyene.Storm.Meteors.Entities.EntityMeteor;

public class Meteor {

    static Class<?> EntityTypesClass;
    public static String impactMessage = "A meteor has impacted the ground at <x>, <y>, <z>.";

    public void load() {
	try {

	    Method a = net.minecraft.server.EntityTypes.class
		    .getDeclaredMethod("a", Class.class, String.class,
			    int.class);
	    a.setAccessible(true);

	    a.invoke(a, EntityMeteor.class, "Fireball", 12);

	} catch (Exception e) {
	    StormUtil.log(Level.SEVERE, "Failed to create meteor entity!");
	}
    }

}
