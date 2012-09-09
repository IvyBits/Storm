package com.github.Icyene.Storm.Acid_Rain.Tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Acid_Rain.AcidRain;

public class DissolverTask {

    private int id;
    private Random rand = new Random();
    private World affectedWorld;
    private Storm storm;
    
    private GlobalVariables glob;
    
    public DissolverTask(Storm storm, World affectedWorld) {
	this.storm = storm;
	this.affectedWorld = affectedWorld;
	glob = Storm.wConfigs.get(affectedWorld.getName());
    }

    public void run() {

	id = Bukkit.getScheduler()
		.scheduleSyncRepeatingTask(
			storm,
			new Runnable()
			{
			    @Override
			    public void run()
			    {
				Chunk[] loadedChunk = affectedWorld
					.getLoadedChunks();

				for (int blocksPerCalculationIndex = 0; blocksPerCalculationIndex <= glob.Acid__Rain_Dissolver_Blocks__To__Deteriorate__Per__Calculation; ++blocksPerCalculationIndex)
				{
				    Chunk chunkToDissolve = loadedChunk[rand
					    .nextInt(loadedChunk.length)];
				    if (rand.nextInt(100) < 100)
				    {
					int x = rand.nextInt(16);
					int z = rand.nextInt(16);

					Block toDeteriorate = affectedWorld
						.getHighestBlockAt(
							chunkToDissolve
								.getBlock(
									x,
									4,
									z)
								.getLocation())
						.getLocation()
						.subtract(0, 1, 0)
						.getBlock();

					if (Storm.util
						.isBlockProtected(toDeteriorate)) {
					    --blocksPerCalculationIndex;
					} else {

					    Biome deteriorationBiome = toDeteriorate
						    .getBiome();

					    if (AcidRain.rainyBiomes
						    .contains(deteriorationBiome))
					    {

						if (rand.nextInt(100) < glob.Acid__Rain_Dissolver_Block__Deterioration__Chance)
						{
						    if (toDeteriorate
							    .getTypeId() != 0)
						    {

							Storm.util
								.transform(
									toDeteriorate,
									glob.Acid__Rain_Dissolver_Block__Transformations);
						    }

						}

					    } else
					    {
						--blocksPerCalculationIndex;
					    }
					}
				    }
				}

			    }
			},
			0,
			glob.Acid__Rain_Scheduler_Dissolver__Calculation__Intervals__In__Ticks);

    }

    public void stop() {
	Bukkit.getScheduler().cancelTask(id);
    }

}
