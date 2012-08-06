package com.github.Icyene.Storm.Meteors.Entities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Fireball;

import org.bukkit.event.entity.ExplosionPrimeEvent;

import org.bukkit.util.Vector;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.StormUtil;

import net.minecraft.server.*;

public class EntityMeteor extends EntityFireball {

    public EntityMeteor(World world) {
	super(world);
	a(1.0F, 1.0F);
    }

    @Override
    // movement hook
    public void h_() {

	final Fireball fireball = (Fireball) this.getBukkitEntity();

	fireball.getWorld().createExplosion(fireball.getLocation(),
		GlobalVariables.meteorites_meteor_trailPower);
	System.out.println("Exploding trail.");

	super.h_();

    }

    // teh explosions
    @Override
    public void a(MovingObjectPosition movingobjectposition)
    {
	if (!world.isStatic)
	{
	    if (movingobjectposition.entity != null)
		movingobjectposition.entity.damageEntity(
			DamageSource.fireball(this, shooter), 6);
	    ExplosionPrimeEvent event = new ExplosionPrimeEvent(
		    (Explosive) CraftEntity.getEntity(world.getServer(), this));
	    world.getServer().getPluginManager().callEvent(event);
	    if (!event.isCancelled()) {

		world.createExplosion(
			this,
			locX,
			locY,
			locZ,
			GlobalVariables.meteorites_meteor_impactExplosionRadius,
			event.getFire());

		StormUtil.broadcast(GlobalVariables.meteorites_meteor_impactMessage
						.replace("<x>", locX + "")
						.replace("<y>", locY + "")
						.replace("<z>", locZ + ""));

	    }
	    die();
	}
    }

    // Brightness
    public float c(float f)
    {
	return GlobalVariables.meteorites_meteor_brightness;
    }

    public Vector getDirection() {
	return new Vector(dirX, dirY, dirZ);
    }

    public void setDirection(Vector direction) {
	setDirection(direction.getX(), direction.getY(), direction.getZ());
    }

    public void setDirection(double x, double y, double z)
    {
	super.setDirection(x, y, z);
    }

    public EntityLiving shooter;
    public double dirX;
    public double dirY;
    public double dirZ;
    public float yield;
    public boolean isIncendiary;
}