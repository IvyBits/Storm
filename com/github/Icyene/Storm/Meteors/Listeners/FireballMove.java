package com.github.Icyene.Storm.Meteors.Listeners;

import org.bukkit.entity.Fireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Meteors.Events.FireballMoveEvent;

public class FireballMove implements Listener {
    
    public Storm storm;
    public float explosionPower = 5.0F;
    
    public FireballMove(Storm sStorm) {
	this.storm = sStorm;
    }
    
    @EventHandler
    public void onFireballMove(FireballMoveEvent move) {
	final Fireball ball = move.getFireball();
	
	ball.getWorld().createExplosion(ball.getLocation(), explosionPower);
    }

}
