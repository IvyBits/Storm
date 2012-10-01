package com.github.StormTeam.Storm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;

import net.minecraft.server.ChunkCoordIntPair;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.WorldServer;
import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkProviderServer;

public class BlockTickSelector {

    private WorldServer world;
    private Method a, chunk_k;
    private int chan;    
    private int val = new Random().nextInt();

    public BlockTickSelector(World world, int selChance)
            throws NoSuchMethodException,
            SecurityException, NoSuchFieldException, InstantiationException, IllegalAccessException {

        this.world = ((CraftWorld) world).getHandle();
        this.chan = selChance;


        Field que = ChunkProviderServer.class.getDeclaredField("unloadQueue");
        Class<?> cls = que.getType();

        System.out.println(cls);

        if (Storm.version == 1.2) {
            chunk_k = Chunk.class.getDeclaredMethod("o");

        } else {
            chunk_k = Chunk.class.getDeclaredMethod("k");
        }

        a = net.minecraft.server.World.class.getDeclaredMethod("a", int.class, int.class, Chunk.class);
        a.setAccessible(true);

        chunk_k.setAccessible(true);
    }

    public ArrayList<ChunkCoordIntPair> getRandomTickedChunks() throws InvocationTargetException, IllegalAccessException {

        ArrayList<ChunkCoordIntPair> doTick = new ArrayList<ChunkCoordIntPair>();

        if (world.players.isEmpty()) {
            return doTick;
        }

        List<org.bukkit.Chunk> loadedChunks = Arrays.asList(world.getWorld().getLoadedChunks());

        for (Object ob : world.players) {
            EntityHuman entityhuman = (EntityHuman) ob;

            final int eX = (int) Math.floor(entityhuman.locX / 16),
                    eZ = (int) Math.floor(entityhuman.locZ / 16);

            byte range = 7;
            for (int x = -range; x <= range; x++) {
                for (int z = -range; z <= range; z++) {
                    if (loadedChunks.contains(world.getChunkAt(x + eX, z + eZ).bukkitChunk)) {
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

            chunk_k.invoke(chunk);

            int x, y, z, i1;

            if (world.random.nextInt(chan) == 0) {
                val = val * 3 + 1013904223;
                i1 = val >> 2;
                x = i1 & 15;
                z = i1 >> 8 & 15;
                y = world.g(x + xOffset, z + zOffset);
                doTick.add(world.getWorld().getBlockAt(x + xOffset, y,
                        z + zOffset));
            }

        }
        System.out.println(doTick);
        return doTick;
    }
}
