package com.github.Icyene.Storm;

import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.Icyene.Storm.Events.AcidRainEvent;
import com.github.Icyene.Storm.Events.BlizzardEvent;

public class MultistateManager implements Listener {

    // This class makes sure there can't be a blizzard at the same time as acid
    // rain, etc.

    //FIXME: Currently broken. Shattered.
    
    public MultistateManager() {
    };

    HashMap<World, Boolean> multiMap = new HashMap<World, Boolean>();

    @EventHandler
    public void hasMultipleState(AcidRainEvent e) {

	World affectedWorld = e.getAffectedWorld();

	if (e.getWeatherState()) { // Acid rain just started

	    if (multiMap.containsKey(affectedWorld)
		    && multiMap.get(affectedWorld)) {

		e.setCancelled(true);
		return;
	    }

	}

	multiMap.remove(affectedWorld);
	multiMap.put(affectedWorld, Boolean.TRUE);

    }

    @EventHandler
    public void hasMultipleState(BlizzardEvent e) {

	World affectedWorld = e.getAffectedWorld();

	if (e.getWeatherState()) { // Blizzard just started

	    if (multiMap.containsKey(affectedWorld)
		    && multiMap.get(affectedWorld)) {

		e.setCancelled(true);
		return;

	    }

	}

	multiMap.remove(affectedWorld);
	multiMap.put(affectedWorld, Boolean.TRUE);

    }

}
