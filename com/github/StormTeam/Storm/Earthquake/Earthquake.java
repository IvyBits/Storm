package com.github.StormTeam.Storm.Earthquake;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Earthquake.Exceptions.InvalidWorldException;
import com.github.StormTeam.Storm.Earthquake.Listeners.PlayerListener;

import java.util.HashMap;
import java.util.Iterator;

public class Earthquake {
	
	private static Storm storm;
	private static HashMap<Integer, Quake> quakes = new HashMap<Integer, Quake>();

	public static void load(Storm storm) {
		Earthquake.storm = storm;
		Storm.pm.registerEvents(new PlayerListener(storm), storm);
	}
	
	public static Integer loadQuake(Location one, Location two) {
		try {
			Integer id = quakes.size();
			Quake q = new Quake(storm, id, one, two);
			
			quakes.put(id, q);
			
			return id;
		}catch(InvalidWorldException e) {
			return null;
		}
	}
	
	public static Boolean isQuaked(Player p) {
		return isQuaked(p.getLocation());
	}
	
	public static Boolean isQuaked(Location l) {
		Iterator<Quake> qI = quakes.values().iterator();
		while(qI.hasNext()) {
			Quake quake = qI.next();
			if(!quake.isLoading() && !quake.isRunning()) {
				qI.remove();
				continue;
			}
			
			if(quake.isQuaking(l))
				return true;
		}
		
		return false;
	}
	
	public static void stopQuake(int QuakeID) {
		if(quakes.containsKey(QuakeID)) {
			Quake q = quakes.remove(QuakeID);
			q.stop();
		}
	}
}
