package tk.ivybits.storm.weather.meteor;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Fireball;
import tk.ivybits.storm.Storm;
import tk.ivybits.storm.WorldVariables;
import tk.ivybits.storm.nms.NMS;
import tk.ivybits.storm.utility.StormUtil;
import tk.ivybits.storm.weather.StormWeather;

/**
 * A meteor weather object.
 */
public class MeteorWeather extends StormWeather {

    private final WorldVariables glob;

    /**
     * Creates a meteor weather object for given world.
     *
     * @param world The world this object will be handling
     */

    public MeteorWeather(String world) {
        super(world);
        glob = Storm.wConfigs.get(world);
        autoKillTicks = 1;
    }

    @Override
    public boolean canStart() {
        return glob.Weathers__Enabled_Natural__Disasters_Meteors;
    }

    /**
     * Called when a meteor is called in the handled world.
     */

    @Override
    public void start() {
        Chunk chunk = StormUtil.pickChunk(Bukkit.getWorld(world));

        if (chunk == null) {
            return;
        }
        Block b = chunk.getBlock(Storm.random.nextInt(16), 4, Storm.random.nextInt(16));
        spawnMeteorNaturallyAndRandomly(b.getX(), b.getZ());
    }

    @Override
    public void end() {

    }

    private void spawnMeteorNaturallyAndRandomly(double x, double z) {
        Fireball meteor = NMS.spawnMeteor(
                bukkitWorld,
                Storm.random.nextInt(7) + 1,
                10,
                Storm.random.nextInt(5) + 5,
                Storm.random.nextInt(50) + 25,
                100,
                glob.Natural__Disasters_Meteor_Messages_On__Meteor__Crash,
                glob.Natural__Disasters_Meteor_Shockwave_Damage,
                80,
                glob.Natural__Disasters_Meteor_Messages_On__Damaged__By__Shockwave,
                glob.Natural__Disasters_Meteor_Meteor__Spawn,
                Storm.random.nextInt(6) + 3);
        meteor.teleport(new Location(
                bukkitWorld,
                (int) x,
                Storm.random.nextInt(100) + 156,
                (int) z,
                (float) Storm.random.nextInt(360),
                -9));
        meteor.setDirection(meteor.getDirection().setY(-1));
    }
}
