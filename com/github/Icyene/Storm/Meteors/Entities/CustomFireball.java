package com.github.Icyene.Storm.Meteors.Entities;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;

import com.github.Icyene.Storm.Meteors.Events.FireballMoveEvent;

import net.minecraft.server.EntityFireball;
import net.minecraft.server.World;

public class CustomFireball extends EntityFireball {

    public CustomFireball(World world) {
	super(world);
    }

    @Override
    public void F_() {
	Fireball fireball = (Fireball) this.getBukkitEntity();

	Location from = new Location(fireball.getWorld(), this.lastX,
		this.lastY, this.lastZ, this.lastYaw, this.lastPitch);
	Location to = new Location(fireball.getWorld(), this.locX, this.locY,
		this.locZ, this.yaw, this.pitch);

	FireballMoveEvent event = new FireballMoveEvent(fireball, from, to);

	this.world.getServer().getPluginManager().callEvent(event);

	if (event.isCancelled() && !fireball.isDead()) {
	    return;
	}

	super.F_();
    }
    
    public double getDirection() {
	return dirX;
    }

}