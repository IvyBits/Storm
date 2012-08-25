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
    private String meteorCrashMessage;
    private int burrowCount = 5;
    private int burrowPower = 10;

    public EntityMeteor(World world)
    {
        super(world);
        System.out.println("Meteor with ID of " + this.uniqueId + " has been generated in world " + world + ".");
    }

    @Override
    public void h_()
    {
	if (this.locY > 255) {
	    System.out.println("Meteor with ID of " + this.uniqueId + " has a location of 256. Removing.");
	    this.dead = true; // Die silently
	    return;
	}
	if (this.locY < 0) {
	    System.out.println("Meteor with ID of " + this.uniqueId + " has a location of 0. Removing.");
	    this.dead = true; // Die silently
	    return;
	}
	
	this.motX  *= 1.10;
	this.motY  *= 1.10;
	this.motZ  *= 1.10;
	
	 world.createExplosion(this, locX, locY, locZ, trailPower, true);
	super.h_();
    }

    @Override
    public void a(MovingObjectPosition movingobjectposition)
    {
	if (burrowCount > 0) {
	    // Not yet dead, so burrow.
	    world.createExplosion(this, locX, locY, locZ, burrowPower, true);	      
	    System.out.println("Meteor with ID of " + this.uniqueId + " burrowed.");

	} else {

	    world.createExplosion(this, locX, locY, locZ, explosionRadius, true);
	    
	    Storm.util
	    .damageNearbyPlayers(
		    new Location(this.world.getWorld(), locX, locY,
			    locZ),
			    Storm.config.Natural__Disasters_Meteor_Shockwave_Damage__Radius,
			    Storm.config.Natural__Disasters_Meteor_Shockwave_Damage,
			    Storm.config.Natural__Disasters_Meteor_Shockwave_Damage__Message);
	    
	    getCrashMessage().replace("%x", locX + "")
		    .replace("%z", locZ + "").replace("%y", locY + "");
	    System.out.println("Meteor with ID of " + this.uniqueId + " exploded.");
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
	Storm.util.broadcast(getCrashMessage());
	this.dead = true;

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