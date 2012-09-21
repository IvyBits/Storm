package com.github.Icyene.Storm.Earthquake.Listeners;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Earthquake.Earthquake;

public class PlayerListener implements Listener {
	
	private Storm storm;
	
	public PlayerListener(Storm storm) {
		this.storm = storm;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Location l = e.getPlayer().getLocation().clone();
		Location l2 = e.getPlayer().getLocation().clone();
		l.add(40, 0, 40);
		if(!Earthquake.isQuaked(l)) {	
			l2.subtract(40, 0, 40);
			if(!Earthquake.isQuaked(l2)) {
				Earthquake.loadQuake(l, l2);
			}
		}
	}
}
