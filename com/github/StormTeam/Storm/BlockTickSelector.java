package com.github.StormTeam.Storm;

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

/*
 * @author Icyene
 * My pride and joy :3
 */

public class BlockTickSelector {

    private WorldServer world;
    private Method recheckGaps;
    private int chan;
    private final Random rand = new Random();

    public BlockTickSelector(World world, int selChance)
            throws NoSuchMethodException,
            SecurityException, NoSuchFieldException,
            InstantiationException, IllegalAccessException {

        this.world = ((CraftWorld) world).getHandle();
        recheckGaps = Chunk.class.getDeclaredMethod(Storm.version == 1.3 ? "k" : "o");  //If 1.3.X, method is named "k", else "o".
        recheckGaps.setAccessible(true); //Is private by default
    }

    public ArrayList<ChunkCoordIntPair> getRandomTickedChunks() throws InvocationTargetException, IllegalAccessException {

        ArrayList<ChunkCoordIntPair> doTick = new ArrayList<ChunkCoordIntPair>();

        if (world.players.isEmpty()) {
            return doTick;
        }

        List<org.bukkit.Chunk> loadedChunks = Arrays.asList(world.getWorld().getLoadedChunks());

        for (Object ob : world.players) {
            
            EntityHuman entityhuman = (EntityHuman) ob;
            int eX = (int) Math.floor(entityhuman.locX / 16), eZ = (int) Math.floor(entityhuman.locZ / 16);

            for (int x = -7; x <= 7; x++) {
                for (int z = -7; z <= 7; z++) {
                    if (loadedChunks.contains(world.getChunkAt(x + eX, z + eZ).bukkitChunk)) { //Check if the bukkit chunk exists
                        doTick.add(new ChunkCoordIntPair(x + eX, z + eZ));
                    }
                }
            }
        }
        return doTick; //Empty list = failiure
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

            int xOffset = pair.x << 4, zOffset = pair.z << 4; //Multiply by 4, obviously.
            Chunk chunk = world.getChunkAt(pair.x, pair.z);

            recheckGaps.invoke(chunk); //Recheck gaps

            if (rand.nextInt(100) <= chan) {
                int x = rand.nextInt(15), z = rand.nextInt(15);
                doTick.add(world.getWorld().getBlockAt(x + xOffset, world.g(x + xOffset, z + zOffset), z + zOffset));
            }
        }
        return doTick;
    }
}
