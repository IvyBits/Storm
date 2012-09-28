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
import net.minecraft.server.ChunkProviderServer;
import net.minecraft.server.LongHashMap;

public class BlockTickSelector {

    private WorldServer world;
    private Field l;
    private Method a, contains;
    private int chan;
    private Object crap;

    public BlockTickSelector(World world, int selChance)
            throws NoSuchMethodException,
            SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException {

        this.world = ((CraftWorld) world).getHandle();
        this.chan = selChance;


        Field que = ChunkProviderServer.class.getDeclaredField("unloadQueue");
        Class<?> cls = que.getType();

        System.out.println(cls);

        if (Storm.version == 1.2) {

            contains = cls.getDeclaredMethod("containsKey", long.class);

        } else {
            contains = cls.getDeclaredMethod("contains", int.class, int.class);
        }

        l = net.minecraft.server.World.class.getDeclaredField(Storm.version == 1.2 ? "l" : "g"); //Because inlining is cool like that.
        a = net.minecraft.server.World.class.getDeclaredMethod("a", int.class, int.class, Chunk.class);

        l.setAccessible(true);
        a.setAccessible(true);
        contains.setAccessible(true);
    }

    private long toLong(int msw, int lsw) {
        return (msw << 32) + lsw - -2147483648L;
    }

    private boolean contains(Object ob, int x, int z) throws IllegalAccessException, IllegalAccessException, IllegalArgumentException, IllegalArgumentException, InvocationTargetException {

        if (Storm.version == 1.2) {
            return (Boolean) contains.invoke(ob, toLong(x, z));
        }

        return (Boolean) contains.invoke(ob, x, z);
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
                    if (!(contains(world.chunkProviderServer.unloadQueue, x + eX, z + eZ))) {
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
