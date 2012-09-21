package com.github.Icyene.Storm.Earthquake.Listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.github.Icyene.Storm.Earthquake.Quake;

public class MobListener implements Listener {

	private Quake q;
	
	public MobListener(Quake q) {
		this.q = q;
	}
	
	public void forget() {
		EntityTargetEvent.getHandlerList().unregister(this);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityTarget(EntityTargetEvent e) {
		if(e.getEntity().getType() != EntityType.CREEPER)
			return;
		
		// Targeting happened outside of quake area.
		if(!q.isQuaking(e.getEntity().getLocation()))
			return;

		if(e.getReason() == TargetReason.TARGET_ATTACKED_ENTITY)
			return;
		
		if(e.getReason() == TargetReason.FORGOT_TARGET)
			return;
		
		if(e.getReason() == TargetReason.TARGET_DIED)
			return;
		
		e.setCancelled(true);
	}
	
}
