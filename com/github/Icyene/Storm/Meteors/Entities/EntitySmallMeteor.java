package com.github.Icyene.Storm.Meteors.Entities;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.World;

public class EntitySmallMeteor extends EntityFireball
{
    final private Random rand = new Random();

    private float explosionRadius = 50F;
    private float brightness = 10F;
    private int burrowCount = 5;
    private int burrowPower = 10;

    public EntitySmallMeteor(World world)
    {
	super(world);
    }

    @Override
    public void h_()
    {
	final CraftWorld cWorld = world.getWorld();
	final Location thisLoc = new Location(cWorld, locX, locY, locZ);
	if (rand.nextBoolean()) {
	    cWorld.playEffect(thisLoc, Effect.MOBSPAWNER_FLAMES, 1,
		    rand.nextInt(10) + 5);
	} else {
	    cWorld.playEffect(thisLoc, Effect.SMOKE, 1);
	}
	if (rand.nextBoolean()) {
	    cWorld.playEffect(thisLoc, Effect.GHAST_SHOOT, 1);
	} else {
	    cWorld.playEffect(thisLoc, Effect.GHAST_SHRIEK, 1);
	}

	super.h_();
    }

    @Override
    public void a(MovingObjectPosition movingobjectposition)
    {
	if (burrowCount > 0) {
	    // Not yet dead, so burrow.
	    createExplosion(this, locX, locY, locZ, burrowPower, true);

	} else {

	    createExplosion(this, locX, locY, locZ, explosionRadius, true);
	    die();
	}
    }

    @Override
    public float c(float f)
    {
	return this.brightness;
    }

    public void setBrightness(float brightnessT) {
	this.brightness = brightnessT;
    }

    public float getBrightness() {
	return this.brightness;
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