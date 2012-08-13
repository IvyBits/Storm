package com.github.Icyene.Storm.Snow.Insubstantial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.MultiWorld.MultiWorldManager;

public class SnowThroughBlocks extends JavaPlugin implements Listener
{
    static List<Integer> passThru = new ArrayList<Integer>();

    public SnowThroughBlocks()
    {
	passThru = Arrays
		.asList(GlobalVariables.snow_insubstantial_passThroughBlockIds);
    }

    public void snowifyLoadedChunks()
    {
	for (final World world : getServer().getWorlds())
	{
	    if (MultiWorldManager.checkWorld(world, GlobalVariables.snow_insubstantial_allowedWorlds)) {
		for (final Chunk chunk : world.getLoadedChunks())
		{
		    chunkSnowifier(chunk);
		}
	    }
	}
    }

    @EventHandler
    public void chunkLoad(final ChunkLoadEvent event)
    {
	if (!MultiWorldManager.checkWorld(event.getWorld(),
		GlobalVariables.snow_insubstantial_allowedWorlds)) {	
	    return;
	}
	chunkSnowifier(event.getChunk()); //LINE 62
    }

    @EventHandler
    public void snowForm(final BlockFormEvent event)
    {
	if (MultiWorldManager.checkWorld(event.getBlock().getWorld(),
		GlobalVariables.snow_insubstantial_allowedWorlds)) {
	    if (event.getNewState().getTypeId() != 78)
		return;
	    placeSnow(event.getBlock());
	}
    }

    private void chunkSnowifier(final Chunk chunk)
    {
	final ChunkSnapshot snowifierChunk = chunk.getChunkSnapshot(true, //line 81
		false, false);

	for (int x = 0; x < 16; x++)
	{
	    for (int z = 0; z < 16; z++)
	    {
		final int y = snowifierChunk.getHighestBlockYAt(x, z);

		if (snowifierChunk.getBlockTypeId(x, y, z) == 78)
		    placeSnow(chunk, snowifierChunk, x, y, z);
	    }
	}
    }

    private void placeSnow(final Block block)
    {
	final Location loc = block.getLocation();
	final Chunk chunk = block.getChunk();

	placeSnow(chunk, chunk.getChunkSnapshot(true, false, false),
		Math.abs((chunk.getX() * 16) - loc.getBlockX()),
		loc.getBlockY(),
		Math.abs((chunk.getZ() * 16) - loc.getBlockZ()));
    }

    private void placeSnow(Chunk chunk, ChunkSnapshot snowChunk, final int x,
	    int y, final int z)
    {
	if (y <= 1) // No blocks at void
	    return;

	int type = snowChunk.getBlockTypeId(x, --y, z);

	if (!passThru.contains(type)) // if it cant pass through the block, too
				      // bad
	    return;

	int lastType = type;

	while (true)
	{
	    type = snowChunk.getBlockTypeId(x, --y, z);

	    switch (Material.getMaterial(type))
	    {
	    case AIR: // ignore air and snow
	    case SNOW:
		break;

	    case LEAVES: // check leaves if they have air above them to place
			 // snow
	    {
		if (lastType == 0)
		    chunk.getBlock(x, y + 1, z).setTypeId(78);
		break;
	    }

	    case STONE:
	    case GRASS:
	    case DIRT:
	    case COBBLESTONE:
	    case WOOD:
	    case BEDROCK:
	    case SAND:
	    case GRAVEL:
	    case GOLD_ORE:
	    case IRON_ORE:
	    case COAL_ORE:
	    case LOG:
	    case SPONGE:
	    case GLASS:
	    case LAPIS_ORE:
	    case LAPIS_BLOCK:
	    case DISPENSER:
	    case SANDSTONE:
	    case NOTE_BLOCK:
	    case WOOL:
	    case GOLD_BLOCK:
	    case IRON_BLOCK:
	    case DOUBLE_STEP:
	    case BRICK:
	    case TNT:
	    case BOOKSHELF:
	    case MOSSY_COBBLESTONE:
	    case OBSIDIAN:
	    case MOB_SPAWNER:
	    case DIAMOND_ORE:
	    case DIAMOND_BLOCK:
	    case WORKBENCH:
	    case FURNACE:
	    case REDSTONE_ORE:
	    case SNOW_BLOCK:
	    case CLAY:
	    case JUKEBOX:
	    case PUMPKIN:
	    case NETHERRACK:
	    case SOUL_SAND:
	    case GLOWSTONE:
	    case SMOOTH_BRICK:
	    case MELON_BLOCK:
	    case NETHER_BRICK:
	    case ENDER_STONE:
	    case REDSTONE_LAMP_OFF: {
		if (lastType == 0)
		    chunk.getBlock(x, y + 1, z).setTypeId(78);
		return;
	    }

	    default:
		return;
	    }

	    lastType = type;
	}
    }
}
