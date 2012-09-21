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

public class BlockTickSelector {

	private WorldServer world;
	private Field l;
	private Method a;
	private List<ChunkCoordIntPair> chunks = new ArrayList<ChunkCoordIntPair>();
	private int chan;

	public BlockTickSelector(World world, int selChance)
	        throws NoSuchMethodException,
	        SecurityException, NoSuchFieldException {

		this.world = ((CraftWorld) world).getHandle();
		this.chan = selChance;

		l = net.minecraft.server.World.class.getDeclaredField("l");
		a = net.minecraft.server.World.class.getDeclaredMethod("a", int.class, int.class, Chunk.class);
		l.setAccessible(true);
		a.setAccessible(true);
	}

	public ArrayList<Block> getRandomTickedBlocks()
	        throws IllegalArgumentException,
	        IllegalAccessException, InvocationTargetException {

		ArrayList<Block> doTick = new ArrayList<Block>();

		if (world.players.isEmpty())
			return doTick;

		for (Object ob : world.players) {
			EntityHuman entityhuman = (EntityHuman) ob;

			final int eX = (int) Math.floor(entityhuman.locX / 16), 
				eZ = (int) Math.floor(entityhuman.locZ / 16);

			byte range = 7;
			for (int x = -range; x <= range; x++)
			{
				for (int z = -range; z <= range; z++)
				{
					if (!world.chunkProviderServer.unloadQueue.contains(x + eX, z + eZ))
						chunks.add(new ChunkCoordIntPair(x + eX, z + eZ));
				}

			}

		}

		if (chunks.isEmpty())
			return doTick;

		for (ChunkCoordIntPair pair : chunks) {

			final int xOffset = pair.x * 16, zOffset = pair.z * 16;

			Chunk chunk = world.getChunkAt(pair.x, pair.z);

			a.invoke(world, xOffset, zOffset, chunk);
			chunk.k();
			int x, y, z, i1;

			if (world.random.nextInt(chan) == 0) {
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
