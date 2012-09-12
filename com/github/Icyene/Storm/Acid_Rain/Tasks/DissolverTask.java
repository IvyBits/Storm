package com.github.Icyene.Storm.Acid_Rain.Tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;

public class DissolverTask
{

	private int	            id;
	private Random	        rand	= new Random();
	private World	        affectedWorld;
	private Storm	        storm;

	private GlobalVariables	glob;

	public DissolverTask(Storm storm, World affectedWorld)
	{
		this.storm = storm;
		this.affectedWorld = affectedWorld;
		glob = Storm.wConfigs.get(affectedWorld.getName());
	}

	public void run()
	{

		id = Bukkit.getScheduler()
		        .scheduleSyncRepeatingTask(
		                storm,
		                new Runnable()
		                {
			                @Override
			                public void run()
			                {
				                final Chunk[] loadedChunk = affectedWorld
				                        .getLoadedChunks();

				                for (int index = 0; index <= glob.Acid__Rain_Dissolver_Blocks__To__Deteriorate__Per__Calculation; ++index)
				                {
					                if (rand.nextInt(100) <= glob.Acid__Rain_Acid__Rain__Chance)
					                {
						                int x = rand.nextInt(16);
						                int z = rand.nextInt(16);

						                Block tran = affectedWorld
						                        .getHighestBlockAt(
						                                loadedChunk[rand
						                                        .nextInt(loadedChunk.length)]
						                                        .getBlock(x, 4,
						                                                z)
						                                        .getLocation()
						                        ).getRelative(BlockFace.DOWN);

						                if (Storm.util.isBlockProtected(tran))
						                {
							                --index;
						                } else
						                {
							                if (Storm.biomes.isRainy(tran
							                                .getBiome())
							                        && rand.nextInt(100) < glob.Acid__Rain_Dissolver_Block__Deterioration__Chance
							                        && tran.getTypeId() != 0)
							                {

								                Storm.util
								                        .transform(
								                                tran,
								                                glob.Acid__Rain_Dissolver_Block__Transformations);

							                } else
							                {
								                --index;
							                }
						                }
					                }
				                }
			                }
		                },
		                0,
		                glob.Acid__Rain_Scheduler_Dissolver__Calculation__Intervals__In__Ticks);

	}

	public void stop()
	{
		Bukkit.getScheduler().cancelTask(id);
	}

}
