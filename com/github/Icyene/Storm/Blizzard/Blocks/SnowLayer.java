package com.github.Icyene.Storm.Blizzard.Blocks;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.github.Icyene.Storm.Storm;
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

	final CraftWorld cWorld = (CraftWorld) e.getBukkitEntity().getWorld();
	final org.bukkit.entity.Entity inSnow = e.getBukkitEntity();
	if (inSnow instanceof EntityPlayer) {
	    if (((Player) (inSnow)).getGameMode()==GameMode.CREATIVE) {
		return;
	    }
	}

	if (Blizzard.blizzardingWorlds.containsKey(cWorld) && Blizzard.blizzardingWorlds.get(cWorld)) {
	    
	    final Vector preVector = inSnow.getVelocity();

	    if (!Blizzard.snowyBiomes.contains(inSnow.getLocation().getBlock()
		    .getBiome())) {
		return;
	    } 
	    Vector motionTranslation = preVector.clone();
	    motionTranslation = motionTranslation.multiply(Storm.config.Blizzard_Player_Speed__Loss__While__In__Snow);
	    motionTranslation.setY(preVector.getY());
	    inSnow.setVelocity(motionTranslation);

	} 

    }
 
}
