package com.github.StormTeam.Storm.Acid_Rain.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.github.StormTeam.Storm.BlockTickSelector;
import com.github.StormTeam.Storm.GlobalVariables;
import com.github.StormTeam.Storm.Storm;

public class DissolverTask
{

	private int id;
	private Storm storm;

	private GlobalVariables glob;
	private BlockTickSelector ticker;

	public DissolverTask(Storm storm, World affectedWorld)
	{
		this.storm = storm;
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
