package com.github.Icyene.Storm.Meteors.Entities;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import com.github.Icyene.Storm.Meteors.Meteor;

import net.minecraft.server.*;

public class EntityMeteor extends EntityFireball {

    public float trailPower = 5.0F;
    public float impactPower = 30.0F;
    Location lastPoint;

    public EntityMeteor(World world) {
	super(world);
    }

    @Override
    public void F_() {

	final Fireball fireball = (Fireball) this.getBukkitEntity();

	Location trackingPoint = fireball.getLocation();

	if (lastPoint != null && lastPoint.distance(trackingPoint) >= 3) {
	    fireball.getWorld().createExplosion(trackingPoint,
		    trailPower);
	}

	if (!world.isStatic
		&& (shooter != null && shooter.dead || !world.isLoaded(
			(int) locX, (int) locY, (int) locZ)))
	{
	    die();
	} else
	{
	    super.F_();
	    setOnFire(1);
	    if (this.i)
	    {
		int i = world.getTypeId(e, this.f, g);
		if (i == h)
		{
		    this.j++;
		    if (this.j == 600)
			die();
		    return;
		}
		this.i = false;
		motX *= random.nextFloat() * 0.2F;
		motY *= random.nextFloat() * 0.2F;
		motZ *= random.nextFloat() * 0.2F;
		this.j = 0;
		this.k = 0;
	    } else
	    {
		this.k++;
	    }
	    Vec3D vec3d = Vec3D.create(locX, locY, locZ);
	    Vec3D vec3d1 = Vec3D.create(locX + motX, locY + motY, locZ + motZ);
	    MovingObjectPosition movingobjectposition = world.a(vec3d, vec3d1);
	    vec3d = Vec3D.create(locX, locY, locZ);
	    vec3d1 = Vec3D.create(locX + motX, locY + motY, locZ + motZ);
	    if (movingobjectposition != null)
		vec3d1 = Vec3D.create(movingobjectposition.pos.a,
			movingobjectposition.pos.b, movingobjectposition.pos.c);
	    Entity entity = null;
	    List list = world.getEntities(this, boundingBox.a(motX, motY, motZ)
		    .grow(1.0D, 1.0D, 1.0D));
	    double d0 = 0.0D;
	    for (int j = 0; j < list.size(); j++)
	    {
		Entity entity1 = (Entity) list.get(j);
		if (!entity1.o_() || entity1.a_(shooter) && this.k < 25)
		    continue;
		float f = 0.3F;
		AxisAlignedBB axisalignedbb = entity1.boundingBox.grow(f, f, f);
		MovingObjectPosition movingobjectposition1 = axisalignedbb.a(
			vec3d, vec3d1);
		if (movingobjectposition1 == null)
		    continue;
		double d1 = vec3d.distanceSquared(movingobjectposition1.pos);
		if (d1 < d0 || d0 == 0.0D)
		{
		    entity = entity1;
		    d0 = d1;
		}
	    }

	    if (entity != null)
		movingobjectposition = new MovingObjectPosition(entity);
	    if (movingobjectposition != null)
	    {
		a(movingobjectposition);
		if (dead)
		{
		    ProjectileHitEvent phe = new ProjectileHitEvent(
			    (Projectile) getBukkitEntity());
		    world.getServer().getPluginManager().callEvent(phe);

		    fireball.getWorld().createExplosion(trackingPoint,
			    impactPower);

		    Bukkit.getServer().broadcastMessage(
			    ChatColor.GRAY
				    + Meteor.impactMessage
					    .replaceAll("<x>",
						    trackingPoint.getX() + "")
					    .replace("<y>",
						    trackingPoint.getY() + "")
					    .replace("<Z>",
						    trackingPoint.getX() + ""));
		}
	    }
	    locX += motX;
	    locY += motY;
	    locZ += motZ;
	    float f1 = MathHelper.sqrt(motX * motX + motZ * motZ);
	    yaw = (float) ((Math.atan2(motX, motZ) * 180D) / 3.1415927410125732D);
	    for (pitch = (float) ((Math.atan2(motY, f1) * 180D) / 3.1415927410125732D); pitch
		    - lastPitch < -180F; lastPitch -= 360F)
		;
	    for (; pitch - lastPitch >= 180F; lastPitch += 360F)
		;
	    for (; yaw - lastYaw < -180F; lastYaw -= 360F)
		;
	    for (; yaw - lastYaw >= 180F; lastYaw += 360F)
		;
	    pitch = lastPitch + (pitch - lastPitch) * 0.2F;
	    yaw = lastYaw + (yaw - lastYaw) * 0.2F;
	    float f2 = 0.95F;
	    if (aU())
	    {
		for (int k = 0; k < 4; k++)
		{
		    float f3 = 0.25F;
		    world.a("bubble", locX - motX * (double) f3, locY - motY
			    * (double) f3, locZ - motZ * (double) f3, motX,
			    motY, motZ);
		}

		f2 = 0.8F;
	    }
	    motX += dirX;
	    motY += dirY;
	    motZ += dirZ;
	    motX *= f2;
	    motY *= f2;
	    motZ *= f2;
	    world.a("smoke", locX, locY + 0.5D, locZ, 0.0D, 0.0D, 0.0D);
	    setPosition(locX, locY, locZ);
	}
	lastPoint = trackingPoint;
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

    private int e;
    private int f;
    private int g;
    private int h;
    private boolean i;
    public EntityLiving shooter;
    private int j;
    private int k;
    public double dirX;
    public double dirY;
    public double dirZ;
    public float yield;
    public boolean isIncendiary;

}