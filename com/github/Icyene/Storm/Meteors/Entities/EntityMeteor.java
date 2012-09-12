package com.github.Icyene.Storm.Meteors.Entities;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.github.Icyene.Storm.Storm;

import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.World;

public class EntityMeteor extends EntityFireball
{
	private float	explosionRadius	= 50F;
	private float	trailPower	    = 20F;
	private float	brightness	    = 10F;
	private String	meteorCrashMessage;
	private int	   burrowCount	    = 5;
	private int	   burrowPower	    = 10;

	private String	damageMessage;
	private int	   shockwaveDamage;
	private int	   shockwaveDamageRadius;
	private int	   snowRadius;

	private boolean	h_lock, h_lock_2, h_lock_3;

	public EntityMeteor(World world)
	{
		super(world);
	}

	public EntityMeteor(World world, int burrowCount, int burrowPower,
	        float trailPower, float explosionRadius, float brightness,
	        String crashMessage, int shockwaveDamage,
	        int shockwaveDamageRadius, String damageMessage, int snowRadius)
	{
		super(world);

		this.burrowPower = burrowPower;
		this.burrowCount = burrowCount;
		this.trailPower = trailPower;
		this.explosionRadius = explosionRadius;
		this.brightness = brightness;
		this.meteorCrashMessage = crashMessage;
		this.shockwaveDamage = shockwaveDamage;
		this.shockwaveDamageRadius = shockwaveDamageRadius;
		this.damageMessage = damageMessage;
		this.snowRadius = snowRadius;
		this.damageMessage = damageMessage;

	}

	public void spawn()
	{
		world.addEntity(this, SpawnReason.CUSTOM);

	}

	@Override
	public void h_()
	{
		do
		{
			h_lock = !h_lock;
			if (h_lock)
				break;
			h_lock_2 = !h_lock_2;
			if (h_lock_2)
				break;
			h_lock_3 = !h_lock_3;
			if (h_lock_3) 
				break;

			int locY = (int) (this.locY);
			if ((locY & 0xFFFFFF00) != 0)
			{ // !(0x00 < locY < 0xFF)
				this.dead = true; // Die silently
				return;
			}

			if ((locY & 0xFFFFFFE0) == 0)
			{ // locy < 32
				explode();
				return;
			}

			world.createExplosion(this, locX, locY, locZ, trailPower, true);
		} while (false);
		motX *= 0.909F;
		motY *= 0.909F;
		motZ *= 0.909F;

		super.h_();
	}

	@Override
	public void a(MovingObjectPosition movingobjectposition)
	{
		if (burrowCount > 0)
		{
			// Not yet dead, so burrow.
			world.createExplosion(this, locX, locY, locZ, burrowPower, true);
			--burrowCount;
			return;
		}
		makeWinter();
		explode();

	}

	public void explode()
	{
		world.createExplosion(this, locX, locY, locZ, explosionRadius, true);

		Storm.util
		        .damageNearbyPlayers(
		                new Location(this.world.getWorld(), locX, locY,
		                        locZ),
		                shockwaveDamageRadius,
		                shockwaveDamage,
		                damageMessage);

		for (Player p : world.getWorld().getPlayers())
		{
			Storm.util
			        .message(
			                p,
			                this.meteorCrashMessage
			                        .replace("%x", (int) locX + "")
			                        .replace("%z", (int) locZ + "")
			                        .replace("%y", (int) locY + ""));
		}
		die();
	}

	public void makeWinter()
	{
		CraftWorld craftworld = world.getWorld();
		int radiusSquared = snowRadius * snowRadius;

		for (int x = -snowRadius; x <= snowRadius; x++)
		{
			for (int z = -snowRadius; z <= snowRadius; z++)
			{
				if ((x * x) + (z * z) <= radiusSquared)
				{
					craftworld.getHighestBlockAt((int) (x + locX),
					        (int) (z + locZ)).setBiome(Biome.TAIGA);
				}
			}
		}
	}

	@Override
	public float c(float f)
	{
		return this.brightness;
	}

	public EntityLiving	shooter;
	public double	    dirX;
	public double	    dirY;
	public double	    dirZ;
	public int	        yield	= 0;
	public boolean	    isIncendiary;
}