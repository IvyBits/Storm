package com.github.Icyene.Storm.Snow.Blocks;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.MultiWorld.MultiWorldManager;

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

	// Check for multiworld enabled
	if (!MultiWorldManager.checkWorld(w.getWorld(),
		GlobalVariables.snow_trampable_allowedWorlds)) {
	    return;
	}

	// Apply a vector multiplied by 0.2 to the Entity to slow them down.
	final org.bukkit.entity.Entity inSnow = ((org.bukkit.entity.Entity) (e
		.getBukkitEntity()));
	inSnow.setVelocity(inSnow.getVelocity().multiply(
		GlobalVariables.snow_slowing_motionDecrease_0x01));

    }

    // TODO if trample chance, decrease data in below block by one. If
    // already at
    // one layer, turn to air.

}
