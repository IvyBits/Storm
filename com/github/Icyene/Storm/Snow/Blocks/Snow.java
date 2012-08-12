package com.github.Icyene.Storm.Snow.Blocks;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.MultiWorld.MultiWorldManager;

import net.minecraft.server.*;

public class Snow extends BlockSnow {

    public Snow(int i, int j) {
	super(i, j);
    }

    // Speed. 0.4D = SoulSand speed
    public void a(World paramWorld, int paramInt1, int paramInt2,
	    int paramInt3, Entity paramEntity)
    {
	System.out.println("Slowing entity down!");
	if(!MultiWorldManager.checkWorld(paramWorld.getWorld(), GlobalVariables.snow_trampable_allowedWorlds)) {
	    //do nothing
	    return;
	}
	paramEntity.motX *= GlobalVariables.snow_trampable_motionDecreaseX;
	paramEntity.motZ *= GlobalVariables.snow_trampable_motionDecreaseZ;
	
	//if trample chance, decrease data in below block by one. If already at one layer, turn to air.
	
    }

    // A test.
    public boolean canPlace(World world, int i, int j, int k) {
	int l = world.getTypeId(i, j - 1, k);
	System.out.println("CanPlace!");
	return l != 0 && (l == Block.LEAVES.id || Block.byId[l].d()) ? world
		.getMaterial(i, j - 1, k).isSolid() : false;
    }

}
