package com.github.Icyene.Storm.Blizzard.Blocks;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Blizzard.Blizzard;

import net.minecraft.server.*;

public class SnowLayer extends BlockSnow
{

	public SnowLayer(int i, int j)
	{
		super(i, j);
	}

	/* You are not expected to understand this. */

	@Override
	public boolean canPlace(World world, int i, int j, int k)
	{

		int l = world.getTypeId(i, j - 1, k);

		if (l == Block.SNOW.id)
		{
			int data = world.getData(i, j - 1, k);
			if (data == 7)
			{
				world.setTypeId(i, j - 1, k, Block.SNOW_BLOCK.id);
				return false;
			} else
			{
				world.setData(i, j - 1, k, ++data);
				return false;
			}
		}

		return l == 0 || l != Block.LEAVES.id && !Block.byId[l].d() ? false
		        : world.getMaterial(i, j - 1, k).isSolid();
	}

	@Override
	public void a(final World w, final int x, final int y,
	        final int z, final Entity e)
	{
		final CraftWorld cWorld = (CraftWorld) w.getWorld();
		if (!Storm.wConfigs.containsKey(cWorld.getName()))
		{
			return;
		}

		if (!Storm.wConfigs.get(cWorld.getName()).Features_Blizzards_Slowing__Snow)
		{
			return;
		}

		final org.bukkit.entity.Entity inSnow = e.getBukkitEntity();
		if (inSnow instanceof EntityPlayer)
		{
			if (((Player) (inSnow)).getGameMode() == GameMode.CREATIVE)
			{
				return;
			}
		}

		if (Blizzard.blizzardingWorlds.containsKey(cWorld)
		        && Blizzard.blizzardingWorlds.get(cWorld))
		{

			if (!Storm.biomes.isTundra(inSnow.getLocation().getBlock()
			        .getBiome()))
			{
				return;
			}

			inSnow.setVelocity(inSnow
			        .getVelocity()
			        .clone()
			        .multiply(
			                Storm.wConfigs.get(w.getWorld().getName()).Blizzard_Player_Speed__Loss__While__In__Snow));

		}

	}

}
