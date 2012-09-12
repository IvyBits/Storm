package com.github.Icyene.Storm.Wildfire.Listeners;

import java.util.Arrays;
import java.util.HashMap;
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

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Wildfire.Wildfire;

public class WildfireListeners implements Listener {

    // TO USE: Create a fire block and add it to infernink

    public static HashMap<World, List<Block>> infernink = new HashMap<World, List<Block>>();
    private List<Integer> flammableList = Arrays
	    .asList(Wildfire.flammableBlocks);

    // Dear future me. Please forgive me.
    // I can't even begin to express how sorry I am.

    @EventHandler
    public void onBlockIgnite(final BlockIgniteEvent event) {

	if (!event.getCause().equals(IgniteCause.SPREAD)) {
	    return;
	}
	final Location loc = event.getBlock().getLocation();
	final World w = loc.getWorld();

	GlobalVariables glob = Storm.wConfigs.get(w.getName());

	if (infernink.containsKey(w)
		&& !(infernink.get(w).size() < glob.Natural__Disasters_Maximum__Fires)) {
	    return;
	}

	boolean doScan = false;

	final int radiuski = glob.Natural__Disasters_Wildfires_Scan__Radius;

	for (int x = -radiuski; x <= radiuski; x++) {
	    for (int y = -radiuski; y <= radiuski; y++) {
		for (int z = -radiuski; z <= radiuski; z++) {
		    if (infernink.containsKey(w)
				&& infernink.get(w).contains(
			    new Location(w, x + loc.getX(), y
				    + loc.getY(), z + loc.getZ()).getBlock())) {

			doScan = true;

		    }

		}
	    }
	}

	if (doScan) {

	    scanForIgnitables(loc, w, radiuski,
		    glob.Natural__Disasters_Wildfires_Spread__Limit);

	}

    }

    @EventHandler
    public void onBlockEx(final BlockFadeEvent event) {
	final Block b = event.getBlock();
	final World w = b.getWorld();
	if (infernink.containsKey(w) && infernink.get(w).contains(b)) {

	    if (b.getRelative(BlockFace.DOWN).getTypeId() == 0) {
		infernink.remove(b);
	    }
	}

    }

    private void scanForIgnitables(final Location loc, final World w,
	    int radiuski, int spreadLimit)
    {
	Block bR = w.getBlockAt(loc);

	int C = 0;

	for (int x = -radiuski; x <= radiuski; x++) {
	    for (int y = -radiuski; y <= radiuski; y++) {
		for (int z = -radiuski; z <= radiuski; z++) {

		    bR = w.getBlockAt((int) loc.getX() + x,
			    (int) loc.getY() + y, (int) loc.getZ() + z);
		    if (bR.getTypeId() != 0) {
			continue;
		    }

		    bR = bR.getRelative(0, -1, 0);

		    if (canBurn(bR) && (C < spreadLimit)) {
			burn(bR);
			C++;
		    }

		    bR = bR.getRelative(-1, 0, 0);

		    if (canBurn(bR) && (C < spreadLimit)) {
			burn(bR);
			C++;
		    }

		    bR = bR.getRelative(1, 0, 0);

		    if (canBurn(bR) && (C < spreadLimit)) {
			burn(bR);
			C++;
		    }

		    bR = bR.getRelative(0, 0, -1);

		    if (canBurn(bR) && (C < spreadLimit)) {
			burn(bR);
			C++;
		    }

		    bR = bR.getRelative(0, 0, 1);

		    if (canBurn(bR) || (C < spreadLimit)) {
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
	infernink.get(toBurn.getWorld()).add(toBurn);

    }

    public boolean canBurn(Block toCheck) {
	if (flammableList.contains(toCheck.getTypeId())) {
	    return true;
	} else {
	    return false;
	}
    }

}