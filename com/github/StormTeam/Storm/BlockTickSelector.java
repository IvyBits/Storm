package com.github.StormTeam.Storm;

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
    private int chan;
    private Object crap;
    private Method contains;

    public BlockTickSelector(World world, int selChance)
            throws NoSuchMethodException,
            SecurityException, NoSuchFieldException {

        this.world = ((CraftWorld) world).getHandle();
        this.chan = selChance;

        crap = this.world.chunkProviderServer.unloadQueue;

        Class<?> cls = crap.getClass();
        contains = cls.getDeclaredMethod(Storm.version < 1.2 ? "containsKey" : "contains", int.class, int.class);
        l = net.minecraft.server.World.class.getDeclaredField(Storm.version < 1.2 ? "g" : "l"); //Because inlining is cool like that.
        a = net.minecraft.server.World.class.getDeclaredMethod("a", int.class, int.class, Chunk.class);

        l.setAccessible(true);
        a.setAccessible(true);
        contains.setAccessible(true);
    }

    public ArrayList<ChunkCoordIntPair> getRandomTickedChunks() throws InvocationTargetException, IllegalAccessException {

        ArrayList<ChunkCoordIntPair> doTick = new ArrayList<ChunkCoordIntPair>();

        if (world.players.isEmpty()) {
            return doTick;
        }

        for (Object ob : world.players) {
            EntityHuman entityhuman = (EntityHuman) ob;

            final int eX = (int) Math.floor(entityhuman.locX / 16),
                    eZ = (int) Math.floor(entityhuman.locZ / 16);

            byte range = 7;
            for (int x = -range; x <= range; x++) {
                for (int z = -range; z <= range; z++) {
                    if (!((Boolean) contains.invoke(crap, x + eX, z + eZ))) {
                        doTick.add(new ChunkCoordIntPair(x + eX, z + eZ));
                    }
                }

            }

        }

        return doTick;

    }

    public ArrayList<Block> getRandomTickedBlocks()
            throws IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {

        ArrayList<Block> doTick = new ArrayList<Block>();

        ArrayList<ChunkCoordIntPair> ticked = getRandomTickedChunks();

        if (ticked.isEmpty()) {
            return doTick;
        }

        for (ChunkCoordIntPair pair : ticked) {

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
