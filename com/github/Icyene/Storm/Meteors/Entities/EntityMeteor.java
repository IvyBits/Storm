package com.github.Icyene.Storm.Meteors.Entities;

import org.bukkit.Location;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.StormUtil;

import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.World;

public class EntityMeteor extends EntityFireball
{
    private float explosionRadius = 50F;
    private float trailPower = 30F;
    private float brightness = 10F;
    private String meteorCrashMessage;
    private int burrowCount = 5;
    private int burrowPower = 10;

    public EntityMeteor(World world)
    {
	super(world);
    }

    @Override
    public void h_()
    {
	if (this.locY > 255) {
	    this.dead = true; // Die silently
	    return;
	}
	if (this.locY < 0) {
	    this.dead = true; // Die silently
	    return;
	}
	createExplosion(this, locX, locY, locZ, trailPower, true);
	StormUtil
	    .damageNearbyPlayers(
		    new Location(this.world.getWorld(), locX, locY,
			    locZ),
		    GlobalVariables.Natural__Disasters_Meteor_Shockwave_Damage__Radius,
		    GlobalVariables.Natural__Disasters_Meteor_Shockwave_Damage,
		    GlobalVariables.Natural__Disasters_Meteor_Shockwave_Damage__Message);
	super.h_();
    }

    @Override
    public void a(MovingObjectPosition movingobjectposition)
    {
	if (burrowCount > 0) {
	    // Not yet dead, so burrow.
	    createExplosion(this, locX, locY, locZ, burrowPower, true);
	    StormUtil
		    .damageNearbyPlayers(
			    new Location(this.world.getWorld(), locX, locY,
				    locZ),
			    GlobalVariables.Natural__Disasters_Meteor_Shockwave_Damage__Radius,
			    GlobalVariables.Natural__Disasters_Meteor_Shockwave_Damage,
			    GlobalVariables.Natural__Disasters_Meteor_Shockwave_Damage__Message);

	} else {

	    createExplosion(this, locX, locY, locZ, explosionRadius, true);
	    getCrashMessage().replace("%x", locX + "")
		    .replace("%z", locZ + "").replace("%y", locY + "");
	    die();
	}
    }

    @Override
    public float c(float f)
    {
	return this.brightness;
    }

    @Override
    public void die()
    {
	this.dead = true;
	StormUtil.broadcast(getCrashMessage());
    }

    public void setCrashMessage(String message) {
	this.meteorCrashMessage = message;
    }

    public String getCrashMessage() {
	return this.meteorCrashMessage;
    }

    public void setBrightness(float brightnessT) {
	this.brightness = brightnessT;
    }

    public float getBrightness() {
	return this.brightness;
    }

    public void setTrail(float f) {
	this.trailPower = f;
    }

    public void setExplosionPower(float f) {
	this.explosionRadius = f;
    }

    public void setBurrowCount(int count) {
	this.burrowCount = count;
    }

    public void setBurrowPower(int power) {
	this.burrowPower = power;
    }

    // Explosion start - less yield explosions

    public EntityMeteorExplosion explode(net.minecraft.server.Entity entity,
	    double d0, double d1, double d2,
	    float f)
    {
	return createExplosion(entity, d0, d1, d2, f, false);
    }

    public EntityMeteorExplosion createExplosion(
	    net.minecraft.server.Entity entity, double d0, double d1,
	    double d2,
	    float f, boolean flag)
    {
	EntityMeteorExplosion explosion = new EntityMeteorExplosion(
		entity.world, entity, d0, d1, d2, f, this.yield);
	explosion.a = flag;
	explosion.a();
	explosion.a(true);
	return explosion;
    }

    // Explosion end

    public EntityLiving shooter;
    public double dirX;
    public double dirY;
    public double dirZ;
    private int yield = 0;
    public boolean isIncendiary;
}