package com.github.StormTeam.Storm.Wildfire.Listeners;

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

import com.github.StormTeam.Storm.GlobalVariables;
import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Wildfire.Wildfire;
import static com.github.StormTeam.Storm.Wildfire.Wildfire.wildfireBlocks;

public class WildfireListeners implements Listener {

    // TO USE: Create a fire block and add it to infernink
    private List<Integer> flammableList = Arrays
            .asList(Wildfire.flammableBlocks);

    // Dear future me. Please forgive me.
    // I can't even begin to express how sorry I am.
    @EventHandler
    public void onBlockIgnite(final BlockIgniteEvent event) {

        if (!event.getCause().equals(IgniteCause.SPREAD)) {
            return;
        }
        Location loc = event.getBlock().getLocation();
        World w = loc.getWorld();

        GlobalVariables glob = Storm.wConfigs.get(w.getName());

        if (wildfireBlocks.containsKey(w)
                && (wildfireBlocks.get(w).size() < glob.Natural__Disasters_Maximum__Fires)) {

            boolean doScan = false;

            final int radiuski = glob.Natural__Disasters_Wildfires_Scan__Radius;

            for (int x = -radiuski; x <= radiuski; x++) {
                for (int y = -radiuski; y <= radiuski; y++) {
                    for (int z = -radiuski; z <= radiuski; z++) {
                        if (wildfireBlocks.containsKey(w)
                                && wildfireBlocks.get(w).contains(
                                new Location(w, x + loc.getX(), y
                                + loc.getY(), z + loc.getZ())
                                .getBlock())) {

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

    }

    @EventHandler
    public void onBlockEx(final BlockFadeEvent event) {
        final Block b = event.getBlock();
        final World w = b.getWorld();

        if (wildfireBlocks.containsKey(w) && wildfireBlocks.get(w).contains(b) && b.getRelative(BlockFace.DOWN).getTypeId() == 0) {
            wildfireBlocks.remove(b.getWorld());
        }
    }

    private void scanForIgnitables(final Location loc, final World w,
            int radiuski, int spreadLimit) {
        Block bR;

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
        // No need to check because we checked earlier
        wildfireBlocks.get(toBurn.getWorld()).add(toBurn);

    }

    public boolean canBurn(Block toCheck) {
        if (flammableList.contains(toCheck.getTypeId())) {
            return true;
        }
        return false;
    }
}
