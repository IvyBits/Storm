package com.github.Icyene.Storm.Snow.Blocks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.MultiWorld.MultiWorldManager;

import net.minecraft.server.*;

public class SnowLayer extends BlockSnow {

    public SnowLayer(int i, int j) {
	super(i, j);
    }

    /* You are not expected to understand this. */
    
    public void a(final World w, final int x, final int y,
	    final int z, final Entity e)
    {
	// System.out.println("SE: " + paramEntity.getBukkitEntity().getClass()
	// + ": " + paramEntity.getBukkitEntity().getLocation());
	if (!MultiWorldManager.checkWorld(w.getWorld(),
		GlobalVariables.snow_trampable_allowedWorlds)) {
	    // do nothing
	    return;
	}

	//slow down
	
	if(e instanceof EntityPlayer) {
	e.motX = GlobalVariables.snow_slowing_motionDecrease_0x01;
	e.motY = GlobalVariables.snow_slowing_motionDecrease_0x01;
	e.motZ = GlobalVariables.snow_slowing_motionDecrease_0x01;
	((Player) (e.getBukkitEntity())).sendMessage("You have been slowed down!");
	}
	
	//switch ((data = new Location(w.getWorld(), x, y, z).getBlock().getData())) {

//	case 1: {
//	    e.motX = GlobalVariables.snow_slowing_motionDecrease_0x01;
//	    e.motY = GlobalVariables.snow_slowing_motionDecrease_0x01;
//	    e.motZ = GlobalVariables.snow_slowing_motionDecrease_0x01;
//	}
//	case 2: {
//	    e.motX = GlobalVariables.snow_slowing_motionDecrease_0x02;
//	    e.motY = GlobalVariables.snow_slowing_motionDecrease_0x02;
//	    e.motZ = GlobalVariables.snow_slowing_motionDecrease_0x02;
//	}
//	case 3: {
//	    e.motX = GlobalVariables.snow_slowing_motionDecrease_0x03;
//	    e.motY = GlobalVariables.snow_slowing_motionDecrease_0x03;
//	    e.motZ = GlobalVariables.snow_slowing_motionDecrease_0x03;
//	}
//	case 4: {
//	    e.motX = GlobalVariables.snow_slowing_motionDecrease_0x04;
//	    e.motY = GlobalVariables.snow_slowing_motionDecrease_0x04;
//	    e.motZ = GlobalVariables.snow_slowing_motionDecrease_0x04;
//	}
//	case 5: {
//	    e.motX = GlobalVariables.snow_slowing_motionDecrease_0x05;
//	    e.motY = GlobalVariables.snow_slowing_motionDecrease_0x05;
//	    e.motZ = GlobalVariables.snow_slowing_motionDecrease_0x05;
//	}
//	case 6: {
//	    e.motX = GlobalVariables.snow_slowing_motionDecrease_0x06;
//	    e.motY = GlobalVariables.snow_slowing_motionDecrease_0x06;
//	    e.motZ = GlobalVariables.snow_slowing_motionDecrease_0x06;
//	}
//	case 7: {
//	    e.motX = GlobalVariables.snow_slowing_motionDecrease_0x07;
//	    e.motY = GlobalVariables.snow_slowing_motionDecrease_0x07;
//	    e.motZ = GlobalVariables.snow_slowing_motionDecrease_0x07;
//	}
//	case 8: {
//	    e.motX = GlobalVariables.snow_slowing_motionDecrease_0x08;
//	    e.motY = GlobalVariables.snow_slowing_motionDecrease_0x08;
//	    e.motZ = GlobalVariables.snow_slowing_motionDecrease_0x08;
//	}
//	default: {
//	    System.out.println(data);
//	}
	//}

	// if trample chance, decrease data in below block by one. If already at
	// one layer, turn to air.

    }

    // // A test.
    // public boolean canPlace(World world, int i, int j, int k) {
    // int l = world.getTypeId(i, j - 1, k);
    // System.out.println("CanPlace!");
    // return l != 0 && (l == Block.LEAVES.id || Block.byId[l].d()) ? world
    // .getMaterial(i, j - 1, k).isSolid() : false;
    // }

}
