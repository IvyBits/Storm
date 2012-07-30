package com.github.Icyene.Storm.Earthquake.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import com.github.Icyene.Storm.Earthquake.Quaker;

public class InventoryOpened implements Listener {

    public InventoryOpened() {

    }

    public void onPlayerInventoryOpen(InventoryOpenEvent open) {

	final Player opener = (Player) open.getPlayer();

	if (Quaker.earthquakeWorlds.get(opener.getWorld()) == true) {

	    open.setCancelled(true);
	    opener.sendMessage(ChatColor.GRAY
		    + "You have greater things to worry about during an earthquake"
		    +
		    " then you inventory! Don't worry, your diamonds are still there.");

	}

    }

}
