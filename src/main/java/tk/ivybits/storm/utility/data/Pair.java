package tk.ivybits.storm.utility.data;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * An object for the Pair data type.
 *
 * @param <Left>  The left _ of the Pair
 * @param <Right> The right _ of the Pair
 */
public class Pair<Left, Right> {

    /**
     * The left part of the Pair.
     */
    public Left LEFT;
    /**
     * The right part of the Pair.
     */
    public Right RIGHT;

    /**
     * Creates a Pair object.
     *
     * @param left  The _ of the right part
     * @param right The _ of the left part
     */
    public Pair(Left left, Right right) {
        this.LEFT = left;
        this.RIGHT = right;
    }

    public static Location toLocation(World world, int y, Pair<Integer, Integer> par) {
        return new Location(world, par.LEFT, y, par.RIGHT);
    }
}