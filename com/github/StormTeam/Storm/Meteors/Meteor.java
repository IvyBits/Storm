package com.github.StormTeam.Storm.Meteors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.World;
import org.bukkit.entity.Fireball;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Meteors.Entities.EntityMeteor;
import com.github.StormTeam.Storm.Meteors.Tasks.MeteorSpawnerTask;

public class Meteor {

    public static Storm storm;
    public static List<Fireball> activeMeteors = new ArrayList<Fireball>();

    public static void load(Storm sStorm) {
	storm = sStorm;

	

	    try {
		Method a = net.minecraft.server.EntityTypes.class
			.getDeclaredMethod("a", Class.class, String.class,
				int.class);
		a.setAccessible(true);

		a.invoke(a, EntityMeteor.class, "Fireball", 12);

	    } catch (Exception e) {
		Storm.util.log(Level.SEVERE, "Failed to create meteor entity!");
	    }

	    for (World w : sStorm.getServer().getWorlds()) {
		if (Storm.wConfigs.get(w.getName()).Features_Meteor) {
		    new MeteorSpawnerTask(storm, w).run();
		}
	    }

	
    }

}
