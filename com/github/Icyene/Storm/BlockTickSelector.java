package com.github.Icyene.Storm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.World;
import net.minecraft.server.WorldServer;
import net.minecraft.server.Chunk;

public class BlockTickSelector {

	private WorldServer world;
	private Field chunkTickList, l;
	private Method a;

	public BlockTickSelector(World world) {
		this.world = (WorldServer) world;
		Class<?> serverClass = world.getClass();
		try {
			chunkTickList = serverClass.getDeclaredField("chunkTickList");
			l = serverClass.getDeclaredField("l");
			a = serverClass.getDeclaredMethod("a");
			chunkTickList.setAccessible(true);
			l.setAccessible(true);
			a.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<org.bukkit.block.Block> getRandomTickedBlocks()
	        throws IllegalArgumentException,
	        IllegalAccessException, InvocationTargetException {

		@SuppressWarnings("unchecked")
		Iterator<ChunkCoordIntPair> iterator = ((List<ChunkCoordIntPair>) (chunkTickList
		        .get(null))).iterator();

		ArrayList<org.bukkit.block.Block> doTick = new ArrayList<org.bukkit.block.Block>();

		while (iterator.hasNext()) {
			ChunkCoordIntPair chunkcoordintpair = iterator
			        .next();
			int xOffset = chunkcoordintpair.x * 16;
			int zOffset = chunkcoordintpair.z * 16;
			Chunk chunk = world.getChunkAt(chunkcoordintpair.x,
			        chunkcoordintpair.z);

			a.invoke(world, l, chunk);

			chunk.k();

			int x, y, z, i1;

			if (world.random.nextInt(16) == 0) {
				int val = (Integer) l.get(null);
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
