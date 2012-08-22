package com.github.Icyene.Storm.Blizzard.Blocks;

import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.util.Vector;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Blizzard.Blizzard;

import net.minecraft.server.*;

public class SnowLayer extends BlockSnow {

    public SnowLayer(int i, int j) {
	super(i, j);
    }

    /* You are not expected to understand this. */

    @Override
    public void a(final World w, final int x, final int y,
	    final int z, final Entity e)
    {

	final CraftWorld cWorld = w.getWorld();

	if (Blizzard.blizzardingWorlds.containsKey(cWorld)) {

	    final org.bukkit.entity.Entity inSnow = e.getBukkitEntity();
	    final Vector preVector = inSnow.getVelocity();
	    
	    if(!Blizzard.snowyBiomes.contains(inSnow.getLocation().getBlock().getBiome())) {
		return;
	    }

	    final Vector motionTranslation = preVector
		    .multiply(
			    GlobalVariables.Blizzard_Player_Speed__Loss__While__In__Snow)
		    .setY(preVector.getY());
	    inSnow.setVelocity(motionTranslation);

	}

    }

}
