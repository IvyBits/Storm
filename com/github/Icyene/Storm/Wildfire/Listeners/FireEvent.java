package com.github.Icyene.Storm.Wildfire.Listeners;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.MultiWorld.MultiWorldManager;

public class FireEvent implements Listener {

    // TO USE: Create a fire block and add it to infernink

    // Private final, so people can't fuck with it without reflection!
    private final int spreadLimit = GlobalVariables.naturalDisasters_wildfire_spreadLimit;
    // Adopt evil accent to become evil
    private final int radiuski = 3;
    private int C = 0;
    private List<Block> infernink = new ArrayList<Block>();
    List<Integer> canBurn = Arrays
	    .asList(GlobalVariables.naturalDisasters_wildfire_burnableBlocks);

    // Dear future me. Please forgive me.
    // I can't even begin to express how sorry I am.

    @EventHandler
    public void onBlockIgnite(final BlockIgniteEvent event) {

	if (!event.getCause().equals(IgniteCause.SPREAD)) {
	    return;
	}
	
	final Location loc = event.getBlock().getLocation();
	final World w = loc.getWorld();
	
	if(!MultiWorldManager.checkWorld(w, GlobalVariables.naturalDisasters_wildfire_allowedWorlds)) {
	    return;
	}
	
	for (int x = -radiuski; x <= radiuski; x++) {
	    for (int y = -radiuski; y <= radiuski; y++) {
		for (int z = -radiuski; z <= radiuski; z++) {

		    if (infernink.contains(new Location(w, x, y, z).getBlock())) {

			    scanForIgnitables(loc, w);			

		    }

		}
	    }
	}

    }

    @EventHandler
    public void onBlockEx(final BlockFadeEvent event) {
	final Block b = event.getBlock();
	if (infernink.contains(b)) {
	    infernink.remove(b);
	}

    }

    private void scanForIgnitables(final Location loc, final World w) {
	Block bR = w.getBlockAt(loc);

	/* Abandon all ye hope those who venture beyond this point */

	for (int y = 1; y > -2; y--)
	{
	    for (int x = 1; x > -2; x--)
	    {
		for (int z = 1; z > -2; z--)
		{
		    bR = w.getBlockAt((int) loc.getX() + x,
			    (int) loc.getY() + y, (int) loc.getZ() + z);
		    if (bR.getTypeId() != 0) {
			continue;
		    }

		    bR = bR.getRelative(0, -1, 0);

		    if (canBurn.contains(bR.getTypeId()) && (C < this.spreadLimit)) {
			burn(bR);
			C++;
		    }

		    bR = bR.getRelative(-1, 0, 0);

		    if (canBurn.contains(bR.getTypeId())  && (C < this.spreadLimit)) {
			burn(bR);
			C++;
		    }

		    bR = bR.getRelative(1, 0, 0);

		    if (canBurn.contains(bR.getTypeId()) && (C < this.spreadLimit)) {
			burn(bR);
			C++;
		    }

		    bR = bR.getRelative(0, 0, -1);

		    if (canBurn.contains(bR.getTypeId()) && (C < this.spreadLimit)) {
			burn(bR);
			C++;
		    }

		    bR = bR.getRelative(0, 0, 1);

		    if (canBurn.contains(bR.getTypeId()) || (C < this.spreadLimit)) {
			burn(bR);
			C++;
		    }

		    burn(bR);
		    C++;

		}
	    }
	}
    }

    public void burn(final Block toBurn) {

	toBurn.setTypeId(51);
	infernink.add(toBurn);

    }

}
