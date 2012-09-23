package com.github.StormTeam.Storm.Blizzard.Tasks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.github.StormTeam.Storm.BlockTickSelector;
import com.github.StormTeam.Storm.GlobalVariables;
import com.github.StormTeam.Storm.Storm;

public class SnowPiler {
	private int id;
	private World affectedWorld;
	private Storm storm;
	private GlobalVariables glob;
	private BlockTickSelector ticker;

	public SnowPiler(Storm storm, World spawnWorld)
	{
		this.storm = storm;
		this.affectedWorld = spawnWorld;
		glob = Storm.wConfigs.get(spawnWorld.getName());
		try {
			ticker = new BlockTickSelector(affectedWorld,
			        glob.Blizzard_Block_Snow__Pile__Chance);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void run()
	{

		id = Bukkit
		        .getScheduler()
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

						                // Check if it is valid to pile the snow
						                if (Storm.util.isSnowy(b.getBiome())
						                        && b.getTypeId() == net.minecraft.server.Block.SNOW.id
						                        && validateData(b)
						                        && validateMaterials(b)) {
							                pileSnow(b);
						                }

					                }
				                } catch (Exception e) {
					                e.printStackTrace();
				                }

			                }

		                },
		                glob.Blizzard_Scheduler_Player__Piler__Calculation__Intervals__In__Ticks,
		                glob.Blizzard_Scheduler_Player__Piler__Calculation__Intervals__In__Ticks);

	}

	public void stop()
	{
		Bukkit.getScheduler().cancelTask(id);
	}

	@SuppressWarnings("serial")
	private ArrayList<BlockFace> dirs = new ArrayList<BlockFace>() {
		{
			add(BlockFace.NORTH);
			add(BlockFace.EAST);
			add(BlockFace.SOUTH);
			add(BlockFace.WEST);
		}
	};

	private boolean validateData(Block b) {
		for (BlockFace f : dirs) {
			if (b.getRelative(f).getData() > b.getData())
				return true;
		}
		return false;
	}

	private boolean validateMaterials(Block b) {
		for (BlockFace f : dirs) {
			if (b.getRelative(f).getType() == b.getType())
				return true;
		}
		return false;
	}

	private void pileSnow(Block snow) {

		byte data = snow.getData();

		if (data == 0x07) {
			snow.setType(Material.SNOW_BLOCK);
			snow.setData((byte) 0);
			snow.getState().update();
			return;
		}

		snow.setData((byte) (data + 1));

	}

}
