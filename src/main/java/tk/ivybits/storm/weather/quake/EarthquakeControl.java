/*
 * This file is part of Storm.
 *
 * Storm is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Storm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Storm.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package tk.ivybits.storm.weather.quake;

import tk.ivybits.storm.Storm;
import tk.ivybits.storm.utility.StormUtil;
import tk.ivybits.storm.utility.block.Cuboid;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class EarthquakeControl implements Listener {
    public static Set<Quake> quakes = new HashSet<Quake>();

    public static Quake loadQuake(Location epicenter, int magnitude) {
        Quake quake = new Quake(epicenter, magnitude);
        quake.start();
        quakes.add(quake);
        return quake;
    }

    public void forget() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void unloadWorld(WorldUnloadEvent e) {
        if (!e.isCancelled())
            for (Quake quake : quakes) {
                if (quake.world.equals(e.getWorld())) {
                    quake.stop();
                    quakes.remove(quake);
                }
            }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (!Storm.wConfigs.get(e.getBlock().getWorld().getName()).Natural__Disasters_Earthquake_Flying__Blocks || !e.isCancelled())
            handleBlock(e.getBlock(), e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        if (!Storm.wConfigs.get(e.getBlock().getWorld().getName()).Natural__Disasters_Earthquake_Flying__Blocks || !e.isCancelled())
            handleBlock(e.getBlock(), e.getPlayer());
    }

    @EventHandler
    public void removeSpeed(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (p.getMetadata("storm_speed").size() > 0)
            p.setWalkSpeed(p.getMetadata("storm_speed").get(0).asFloat());
    }

    public void handleBlock(final Block block, Player p) {
        for (Quake quake : quakes)
            if (p.getGameMode() == GameMode.CREATIVE || !quake.isQuaking(block.getLocation()) || StormUtil.isBlockProtected(block) || !isBounceable(block))
                return;

        FallingBlock fB = block.getWorld().spawnFallingBlock(block.getLocation(), block.getType(), block.getData());
        fB.setDropItem(true);
        block.setTypeId(0);
        fB.setVelocity(new Vector(Math.random() - 0.5, 0.3, Math.random() - 0.5));
    }

    public static boolean isQuaking(Location location) {
        for (Quake quake : quakes) {
            if (quake.isQuaking(location))
                return true;
        }
        return false;
    }

    public static boolean isBounceable(Block b) {
        switch (b.getType()) {
            case BED_BLOCK:

            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:

            case FIRE:

            case GRASS:

            case IRON_DOOR_BLOCK:

            case LADDER:
            case LAVA:
            case LEVER:

            case PAINTING:
            case PISTON_BASE:
            case PISTON_STICKY_BASE:
            case POWERED_RAIL:

            case SAPLING:
            case SIGN_POST:
            case STATIONARY_WATER:
            case STATIONARY_LAVA:
            case STONE_BUTTON:
            case STONE_PLATE:

            case TORCH:
            case TRAP_DOOR:
            case TRIPWIRE:
            case TRIPWIRE_HOOK:

            case RAILS:
            case REDSTONE_WIRE:
            case REDSTONE_TORCH_ON:

            case WALL_SIGN:
            case WATER:
            case WOOD_PLATE:
            case WOODEN_DOOR:
                return true;
            default:
                return false;
        }
    }

    public static void crack(Location location, int length, int width, int depth) {
        Cuboid area = new Cuboid(location, location);
        area = area.expand(BlockFace.UP, 256).expand(BlockFace.DOWN, 256);
        area = area.expand(BlockFace.NORTH, length);
        area = area.expand(BlockFace.EAST, length);
        area = area.expand(BlockFace.SOUTH, length);
        area = area.expand(BlockFace.WEST, length);
        new RuptureTask(area, location, length, width, depth).start();
    }
}
