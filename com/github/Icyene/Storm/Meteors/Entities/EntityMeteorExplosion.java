// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Explosion.java

package com.github.Icyene.Storm.Meteors.Entities;

import net.minecraft.server.*;

import java.util.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.*;

// Referenced classes of package net.minecraft.server:
//            ChunkPosition, Entity, EntityHuman, World, 
//            MathHelper, Block, AxisAlignedBB, AABBPool, 
//            Vec3D, Vec3DPool, DamageSource, BlockFire

public class EntityMeteorExplosion
{
    private static final Random rand = new Random();

    public EntityMeteorExplosion(net.minecraft.server.World world,
	    net.minecraft.server.Entity entity, double d0, double d1,
	    double d2, float f, int yield)
    {
	this.dropYield = yield;
	a = false;
	h = 16;
	i = new Random();
	blocks = new ArrayList<ChunkPosition>();
	k = new HashMap<EntityHuman, Vec3D>();
	wasCanceled = false;
	this.world = world;
	source = entity;
	size = (float) Math.max(f, 0.0D);
	posX = d0;
	posY = d1;
	posZ = d2;
    }

    public void a(boolean flag)
    {
	world.makeSound(
		posX,
		posY,
		posZ,
		"random.explode",
		4F,
		(1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F);
	world.a("hugeexplosion", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
	org.bukkit.World bworld = world.getWorld();
	Entity explode = source != null ? source.getBukkitEntity() : null;
	Location location = new Location(bworld, posX, posY, posZ);
	List<Block> blockList = new ArrayList<Block>();
	for (int j = blocks.size() - 1; j >= 0; j--)
	{
	    ChunkPosition cpos = (ChunkPosition) blocks.get(j);
	    org.bukkit.block.Block block = bworld.getBlockAt(cpos.x, cpos.y,
		    cpos.z);
	    if (block.getType() != Material.AIR)
		blockList.add(block);
	}

	EntityExplodeEvent event = new EntityExplodeEvent(explode, location,
		blockList, 0.3F);
	world.getServer().getPluginManager().callEvent(event);
	blocks.clear();
	ChunkPosition coords;
	for (Iterator<?> i$ = event.blockList().iterator(); i$.hasNext(); blocks
		.add(coords))
	{
	    org.bukkit.block.Block block = (org.bukkit.block.Block) i$.next();
	    coords = new ChunkPosition(block.getX(), block.getY(), block.getZ());
	}

	if (event.isCancelled())
	{
	    wasCanceled = true;
	    return;
	}
	Iterator<ChunkPosition> iterator = blocks.iterator();
	do
	{
	    if (!iterator.hasNext())
		break;
	    ChunkPosition chunkposition = (ChunkPosition) iterator.next();
	    int i = chunkposition.x;
	    int j = chunkposition.y;
	    int k = chunkposition.z;
	    int l = world.getTypeId(i, j, k);
	    if (flag)
	    {
		double d0 = (float) i + world.random.nextFloat();
		double d1 = (float) j + world.random.nextFloat();
		double d2 = (float) k + world.random.nextFloat();
		double d3 = d0 - posX;
		double d4 = d1 - posY;
		double d5 = d2 - posZ;
		double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
		d3 /= d6;
		d4 /= d6;
		d5 /= d6;
		double d7 = 0.5D / (d6 / (double) size + 0.10000000000000001D);
		d7 *= world.random.nextFloat() * world.random.nextFloat()
			+ 0.3F;
		d3 *= d7;
		d4 *= d7;
		d5 *= d7;
		world.a("explode", (d0 + posX * 1.0D) / 2D,
			(d1 + posY * 1.0D) / 2D, (d2 + posZ * 1.0D) / 2D, d3,
			d4, d5);
		world.a("smoke", d0, d1, d2, d3, d4, d5);
	    }
	    if (l > 0 && l != net.minecraft.server.Block.FIRE.id)
	    {
		if(rand.nextInt(100) <= dropYield) {
		
		    net.minecraft.server.Block.byId[l].dropNaturally(world, i, j, k,
			world.getData(i, j, k), dropYield, 0);
		
		}
		
		
		if (world.setRawTypeIdAndData(i, j, k, 0, 0, world.isStatic))
		    world.applyPhysics(i, j, k, 0);
		net.minecraft.server.Block.byId[l].wasExploded(world, i, j, k);
	    }
	} while (true);
	if (a)
	{
	    iterator = blocks.iterator();
	    do
	    {
		if (!iterator.hasNext())
		    break;
		ChunkPosition chunkposition = (ChunkPosition) iterator.next();
		int i = chunkposition.x;
		int j = chunkposition.y;
		int k = chunkposition.z;
		int l = world.getTypeId(i, j, k);
		int i1 = world.getTypeId(i, j - 1, k);
		if (l == 0 && net.minecraft.server.Block.n[i1] && this.i.nextInt(3) == 0)
		    world.setTypeId(i, j, k, net.minecraft.server.Block.FIRE.id);
	    } while (true);
	}
    }

    public void a()
    {
	if (size < 0.1F)
	    return;
	float f = size;
	HashSet<ChunkPosition> hashset = new HashSet<ChunkPosition>();
	int i;
	int j;
	int k;
	for (i = 0; i < h; i++)
	    for (j = 0; j < h; j++)
		label0: for (k = 0; k < h; k++)
		{
		    if (i != 0 && i != h - 1 && j != 0 && j != h - 1 && k != 0
			    && k != h - 1)
			continue;
		    double d3 = ((float) i / ((float) h - 1.0F)) * 2.0F - 1.0F;
		    double d4 = ((float) j / ((float) h - 1.0F)) * 2.0F - 1.0F;
		    double d5 = ((float) k / ((float) h - 1.0F)) * 2.0F - 1.0F;
		    double d6 = Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
		    d3 /= d6;
		    d4 /= d6;
		    d5 /= d6;
		    float f1 = size * (0.7F + world.random.nextFloat() * 0.6F);
		    double d0 = posX;
		    double d1 = posY;
		    double d2 = posZ;
		    float f2 = 0.3F;
		    do
		    {
			if (f1 <= 0.0F)
			    continue label0;
			int l = MathHelper.floor(d0);
			int i1 = MathHelper.floor(d1);
			int j1 = MathHelper.floor(d2);
			int k1 = world.getTypeId(l, i1, j1);
			if (k1 > 0)
			    f1 -= (net.minecraft.server.Block.byId[k1].a(source) + 0.3F) * f2;
			if (f1 > 0.0F && i1 < 256 && i1 >= 0)
			    hashset.add(new ChunkPosition(l, i1, j1));
			d0 += d3 * (double) f2;
			d1 += d4 * (double) f2;
			d2 += d5 * (double) f2;
			f1 -= f2 * 0.75F;
		    } while (true);
		}

	blocks.addAll(hashset);
	size *= 2.0F;
	i = MathHelper.floor(posX - (double) size - 1.0D);
	j = MathHelper.floor(posX + (double) size + 1.0D);
	k = MathHelper.floor(posY - (double) size - 1.0D);
	int l1 = MathHelper.floor(posY + (double) size + 1.0D);
	int i2 = MathHelper.floor(posZ - (double) size - 1.0D);
	int j2 = MathHelper.floor(posZ + (double) size + 1.0D);
	List<?> list = world.getEntities(source,
		AxisAlignedBB.a().a(i, k, i2, j, l1, j2));
	Vec3D vec3d = Vec3D.a().create(posX, posY, posZ);
	for (int k2 = 0; k2 < list.size(); k2++)
	{
	    net.minecraft.server.Entity entity = (net.minecraft.server.Entity) list
		    .get(k2);
	    double d7 = entity.f(posX, posY, posZ) / (double) size;
	    if (d7 > 1.0D)
		continue;
	    double d0 = entity.locX - posX;
	    double d1 = (entity.locY + (double) entity.getHeadHeight()) - posY;
	    double d2 = entity.locZ - posZ;
	    double d8 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
	    if (d8 == 0.0D)
		continue;
	    d0 /= d8;
	    d1 /= d8;
	    d2 /= d8;
	    double d9 = world.a(vec3d, entity.boundingBox);
	    double d10 = (1.0D - d7) * d9;
	    Entity damagee = entity != null ? entity.getBukkitEntity() : null;
	    int damageDone = (int) (((d10 * d10 + d10) / 2D) * 8D
		    * (double) size + 1.0D);
	    if (damagee == null)
		continue;
	    if (source == null)
	    {
		EntityDamageByBlockEvent event = new EntityDamageByBlockEvent(
			null,
			damagee,
			org.bukkit.event.entity.EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
			damageDone);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled())
		    continue;
		damagee.setLastDamageCause(event);
		entity.damageEntity(DamageSource.EXPLOSION, event.getDamage());
		
		if(entity.getBukkitEntity() instanceof Player) {
		    
		    final Player p = (Player) entity.getBukkitEntity();
		    
		    if(p.getGameMode().equals(GameMode.CREATIVE)); //OR HAS NO KNOCKBACK PERMISSION
		    
		} else {
		entity.motX += d0 * d10;
		entity.motY += d1 * d10;
		entity.motZ += d2 * d10;
		}
		if (entity instanceof EntityHuman)
		    this.k.put((EntityHuman) entity,
			    Vec3D.a().create(d0 * d10, d1 * d10, d2 * d10));
		continue;
	    }
	    Entity damager = source.getBukkitEntity();
	    org.bukkit.event.entity.EntityDamageEvent.DamageCause damageCause;
	    if (damager instanceof TNTPrimed)
		damageCause = org.bukkit.event.entity.EntityDamageEvent.DamageCause.BLOCK_EXPLOSION;
	    else
		damageCause = org.bukkit.event.entity.EntityDamageEvent.DamageCause.ENTITY_EXPLOSION;
	    EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(
		    damager, damagee, damageCause, damageDone);
	    Bukkit.getPluginManager().callEvent(event);
	    if (event.isCancelled())
		continue;
	    entity.getBukkitEntity().setLastDamageCause(event);
	    entity.damageEntity(DamageSource.EXPLOSION, event.getDamage());
	    entity.motX += d0 * d10;
	    entity.motY += d1 * d10;
	    entity.motZ += d2 * d10;
	    if (entity instanceof EntityHuman)
		this.k.put((EntityHuman) entity,
			Vec3D.a().create(d0 * d10, d1 * d10, d2 * d10));
	}

	size = f;
    }

    public boolean a;
    private int h;
    private Random i;
    private net.minecraft.server.World world;
    public double posX;
    public double posY;
    public double posZ;
    public net.minecraft.server.Entity source;
    public float size;
    public List<ChunkPosition> blocks;
    private Map<EntityHuman, Vec3D> k;
    public boolean wasCanceled;
    public int dropYield = 0;
}
