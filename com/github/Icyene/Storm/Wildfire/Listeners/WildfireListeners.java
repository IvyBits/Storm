package com.github.Icyene.Storm.Wildfire.Listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Wildfire.Wildfire;

public class WildfireListeners implements Listener {

    // TO USE: Create a fire block and add it to infernink

    // Private final, so people can't fuck with it without reflection!
    private final int spreadLimit = Storm.config.Natural__Disasters_Wildfires_Spread__Limit;
    // Adopt evil accent to become evil
    private final int radiuski = Storm.config.Natural__Disasters_Wildfires_Scan__Radius;
    private int C = 0;
    public static List<Block> infernink = new ArrayList<Block>();

    // Dear future me. Please forgive me.
    // I can't even begin to express how sorry I am.

    @EventHandler
    public void onBlockIgnite(final BlockIgniteEvent event) {

	if (!event.getCause().equals(IgniteCause.SPREAD)) {
	    return;
	}

	if (!(infernink.size() < Storm.config.Natural__Disasters_Maximum__Fires)) {
	    return;
	}

	boolean doScan = false;

	final Location loc = event.getBlock().getLocation();
	final World w = loc.getWorld();
	
	for (int x = -radiuski; x <= radiuski; x++) {
	    for (int y = -radiuski; y <= radiuski; y++) {
		for (int z = -radiuski; z <= radiuski; z++) {
		    if (infernink.contains(new Location(w, x + loc.getX(), y
			    + loc.getY(), z + loc.getZ()).getBlock())) {

			doScan = true;

		    }

		}
	    }
	}

	if (doScan) {

	    scanForIgnitables(loc, w);

	}

    }

    @EventHandler
    public void onBlockEx(final BlockFadeEvent event) {
	final Block b = event.getBlock();
	if (infernink.contains(b)) {

	    if (b.getRelative(BlockFace.DOWN).getTypeId() == 0) {
		infernink.remove(b);
	    }
	}

    }

    private void scanForIgnitables(final Location loc, final World w)
    {

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

		    if (canBurn(bR) && (C < this.spreadLimit)) {
			burn(bR);
			C++;
		    }

		    bR = bR.getRelative(-1, 0, 0);

		    if (canBurn(bR) && (C < this.spreadLimit)) {
			burn(bR);
			C++;
		    }

		    bR = bR.getRelative(1, 0, 0);

		    if (canBurn(bR) && (C < this.spreadLimit)) {
			burn(bR);
			C++;
		    }

		    bR = bR.getRelative(0, 0, -1);

		    if (canBurn(bR) && (C < this.spreadLimit)) {
			burn(bR);
			C++;
		    }

		    bR = bR.getRelative(0, 0, 1);

		    if (canBurn(bR) || (C < this.spreadLimit)) {
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

    public boolean canBurn(Block toCheck) {
	if (Arrays.asList(
		Wildfire.flammableBlocks)
		.contains(toCheck.getTypeId())) {
	    return true;
	} else {
	    return false;
	}
    }

}
