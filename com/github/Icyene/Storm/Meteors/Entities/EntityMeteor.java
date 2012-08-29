package com.github.Icyene.Storm.Meteors.Entities;

import org.bukkit.Location;

import com.github.Icyene.Storm.Storm;

import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.World;

public class EntityMeteor extends EntityFireball
{
    private float explosionRadius = 50F;
    private float trailPower = 20F;
    private float brightness = 10F;
    private String meteorCrashMessage = Storm.config.Natural__Disasters_Meteor_Message__On__Meteor__Crash;
    private int burrowCount = 5;
    private int burrowPower = 10;

    private int prevMotX = 0;
    private int prevMotY = 0;
    private int prevMotZ = 0;

    private Location thisLoc;
    private Location prevLoc;

    public EntityMeteor(World world)
    {
	super(world);
	prevLoc = new Location(world.getWorld(), locX, locY, locZ);
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

	if (this.motX == 0 || this.motY == 0 || this.motZ == 0) {
	    this.dead = true; // Stopped moving, kill
	    return;
	}

	// Make sure it never stalls in air
	if (prevMotX <= motX) {
	    prevMotX = (int) motX;
	}
	else {
	    motX = prevMotX;
	}
	if (prevMotY <= motY) {
	    prevMotY = (int) motY;
	}
	else {
	    motY = prevMotY;
	}
	if (prevMotZ <= motZ) {
	    prevMotZ = (int) motZ;
	}
	else {
	    motZ = prevMotZ;
	}

	if (locY < 30) {
	    // Just explode
	    explode();

	}

	thisLoc = new Location(world.getWorld(), locX, locY, locZ);
	if (thisLoc.distance(prevLoc) > 1) {
	    world.createExplosion(this, locX, locY, locZ, trailPower, true);
	    prevLoc = thisLoc;
	}
	super.h_();
    }

    @Override
    public void a(MovingObjectPosition movingobjectposition)
    {
	if (burrowCount > 0) {
	    // Not yet dead, so burrow.
	    world.createExplosion(this, locX, locY, locZ, burrowPower, true);
	    --burrowCount;
	    return;
	}
	explode();

    }

    public void explode() {
	world.createExplosion(this, locX, locY, locZ, explosionRadius, true);

	Storm.util
		.damageNearbyPlayers(
			new Location(this.world.getWorld(), locX, locY,
				locZ),
			Storm.config.Natural__Disasters_Meteor_Shockwave_Damage__Radius,
			Storm.config.Natural__Disasters_Meteor_Shockwave_Damage,
			Storm.config.Natural__Disasters_Meteor_Shockwave_Damage__Message);

	Storm.util.broadcast(getCrashMessage().replace("%x", (int) locX + "")
		.replace("%z", (int) locZ + "").replace("%y", (int) locY + "")); // Lose
										 // percision

	die();
    }

    @Override
    public float c(float f)
    {
	return this.brightness;
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

    public EntityLiving shooter;
    public double dirX;
    public double dirY;
    public double dirZ;
    public int yield = 0;
    public boolean isIncendiary;
}