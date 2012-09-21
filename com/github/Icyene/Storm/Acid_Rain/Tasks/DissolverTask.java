package com.github.Icyene.Storm.Acid_Rain.Tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.github.Icyene.Storm.BlockTickSelector;
import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;

public class DissolverTask
{

	private int id;
	private Random rand = new Random();
	private World affectedWorld;
	private Storm storm;

	private GlobalVariables glob;
	private BlockTickSelector ticker;

	public DissolverTask(Storm storm, World affectedWorld)
	{
		this.storm = storm;
		this.affectedWorld = affectedWorld;
		glob = Storm.wConfigs.get(affectedWorld.getName());
		try {
			ticker = new BlockTickSelector(affectedWorld,
			        glob.Acid__Rain_Dissolver_Block__Deterioration__Chance);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

				                try {
					                for (Block b : ticker
					                        .getRandomTickedBlocks()) {

						                if (!Storm.util.isBlockProtected(b))
						                {

						                }
						                if (Storm.biomes.isRainy(b
						                        .getBiome())						                        
						                        && b.getTypeId() != 0)
						                {

							                Storm.util
							                        .transform(
							                                b,
							                                glob.Acid__Rain_Dissolver_Block__Transformations);

						                }
					                }
				                } catch (Exception e) {
					                e.printStackTrace();
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
