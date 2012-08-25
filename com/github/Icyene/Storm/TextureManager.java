package com.github.Icyene.Storm;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.Icyene.Storm.Blizzard.Blizzard;
import com.github.Icyene.Storm.Events.AcidRainEvent;
import com.github.Icyene.Storm.Events.BlizzardEvent;
import com.github.Icyene.Storm.Rain.Acid.AcidRain;

public class TextureManager implements Listener {

    public TextureManager() {

    }

    @EventHandler
    private void worldEvent(PlayerChangedWorldEvent e) {

	final Player hopper = e.getPlayer();
	final World toWorld = hopper.getWorld();
	final World fromWorld = e.getFrom();

	if (!toWorld.equals(fromWorld)) {

	    if (Blizzard.blizzardingWorlds.containsKey(toWorld)) {

		if (Blizzard.blizzardingWorlds.get(toWorld)) {

		    Storm.util.setTexture(hopper,
			    Storm.config.Textures_Blizzard_Texture__Path);

		    return;

		}
	    }

	    if (AcidRain.acidicWorlds.containsKey(toWorld)) {
		if (AcidRain.acidicWorlds.get(toWorld)) {

		    Storm.util.setTexture(hopper,
			    Storm.config.Textures_Acid__Rain_Texture__Path);

		    return; // The hashmap is as World, Boolean. Bool is whether
			    // the world does have event, , da

		}
	    }
	    Storm.util.clearTexture(hopper);

	}

    }

    @EventHandler
    private void loginEvent(PlayerJoinEvent e) {

	final Player hopper = e.getPlayer();
	final World world = hopper.getWorld();

	if (Blizzard.blizzardingWorlds.containsKey(world)) {
	    if (Blizzard.blizzardingWorlds.get(world)) { // This is line 60 from
							 // the traceback. NPE.
							 // But how???

		Storm.util.setTexture(hopper,
			Storm.config.Textures_Blizzard_Texture__Path);

		return;

	    }
	}

	if (AcidRain.acidicWorlds.containsKey(world)) {
	    if (AcidRain.acidicWorlds.get(world)) {

		Storm.util.setTexture(hopper,
			Storm.config.Textures_Acid__Rain_Texture__Path);

		return;

	    }
	}

	Storm.util.clearTexture(hopper);

    }

    @EventHandler
    private void setAcidTexture(AcidRainEvent event) {

	final World world = event.getAffectedWorld();

	if (event.getWeatherState()) {

	    for (Player p : world.getPlayers()) {

		Storm.util.setTexture(p,
			Storm.config.Textures_Acid__Rain_Texture__Path);

	    }
	} else {

	    for (Player p : world.getPlayers()) {

		Storm.util.clearTexture(p);

	    }

	}

    }

    @EventHandler
    private void setBlizzardTexture(BlizzardEvent event) {

	final World world = event.getAffectedWorld();

	if (event.getWeatherState()) {

	    for (Player p : world.getPlayers()) {

		Storm.util.setTexture(p,
			Storm.config.Textures_Blizzard_Texture__Path);

	    }
	} else {

	    for (Player p : world.getPlayers()) {

		Storm.util.clearTexture(p);

	    }

	}

    }

}
