package com.github.Icyene.Storm.Hail.Entities;

import org.bukkit.Effect;
import org.bukkit.Location;

import net.minecraft.server.DamageSource;
import net.minecraft.server.EntityBlaze;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntitySnowball;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.World;

public class EntityHailStone extends EntitySnowball
{

    private byte damage = 0; // GlobalVariables.....

    public EntityHailStone(World world)
    {
	super(world);
    }

    public EntityHailStone(World world, EntityLiving entityliving)
    {
	super(world, entityliving);
    }

    public EntityHailStone(World world, double d, double d1, double d2)
    {
	super(world, d, d1, d2);
    }

    @Override
    public void a(MovingObjectPosition movingobjectposition)
    {
	if (movingobjectposition.entity != null)
	{
	    if (movingobjectposition.entity instanceof EntityBlaze)
		damage *= damage;
	    movingobjectposition.entity.damageEntity(
		    DamageSource.projectile(this, shooter), damage);
	}
	final org.bukkit.World bukkitWorld = world.getWorld();
	for (int i = 0; i < 8; i++) {
	    bukkitWorld.playEffect(new Location(bukkitWorld, locX, locY, locZ),
		    Effect.POTION_BREAK, 1);
	}

	if (!world.isStatic)
	    die();
    }

    public byte getHitDamage() {
	return this.damage;
    }

    public void setHitDamage(byte damage) {
	this.damage = damage;
    }
}
