package com.github.Icyene.Storm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;

import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.WorldServer;
import net.minecraft.server.Chunk;

public class BlockTickSelectors {

	private WorldServer world;
	private Field l;
	private Method a;
	private List<ChunkCoordIntPair> chunkTickList = new ArrayList<ChunkCoordIntPair>();

	public BlockTickSelectors(World world) throws NoSuchMethodException,
	        SecurityException, NoSuchFieldException {
		
		this.world = ((CraftWorld) world).getHandle();
	
		Class<?> serverSuperClass = world.getClass().getSuperclass();

		l = serverSuperClass.getDeclaredField("l");
		a = serverSuperClass.getDeclaredMethod("a", int.class, int.class,
		        Chunk.class);
		l.setAccessible(true);
		a.setAccessible(true);

	}

	public ArrayList<Block> getRandomTickedBlocks()
	        throws IllegalArgumentException,
	        IllegalAccessException, InvocationTargetException {

		ArrayList<Block> doTick = new ArrayList<Block>();

		if (world.players.size() == 0) {
			return doTick;
		}

		for (Object ob : world.players) {
			EntityHuman entityhuman = (EntityHuman) ob;
			int eX = (int) Math.floor(entityhuman.locX / 16);
			int eZ = (int) Math.floor(entityhuman.locZ / 16);
			byte b0 = 7;
			for (int x = -b0; x <= b0; x++)
			{
				for (int z = -b0; z <= b0; z++)
				{
					if (!world.chunkProviderServer.unloadQueue.contains(x + eX, z + eZ))
						chunkTickList.add(new ChunkCoordIntPair(x + eX, z + eZ));
				}

			}

		}

		if (chunkTickList.size() == 0) {
			return doTick;
		}

		for (ChunkCoordIntPair cPair : chunkTickList) {

			int xOffset = cPair.x * 16;
			int zOffset = cPair.z * 16;
			Chunk chunk = world.getChunkAt(cPair.x, cPair.z);

			a.invoke(world, xOffset, zOffset, chunk);
			chunk.k();
			int x, y, z, i1;

			if (world.random.nextInt(16) == 0) {
				int val = (Integer) l.get(world);
				val = val * 3 + 1013904223;
				i1 = val >> 2;
				x = i1 & 15;
				z = i1 >> 8 & 15;
				y = world.g(x + xOffset, z + zOffset);
				this.l.set(world, val);
				doTick.add(world.getWorld().getBlockAt(x + xOffset, y,
				        z + zOffset));
			}

		}
		return doTick;
	}
}
