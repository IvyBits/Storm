package tk.ivybits.storm.nms;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import tk.ivybits.storm.Storm;
import tk.ivybits.storm.utility.Reflection;

public class NMS {
    public static final NMSHooks CURRENT_IMPLEMENTATION;
    public static final String MC_SLUG;

    static {
        String slug = null;
        _outer:
        for (int major = 1; major != 10; major++) {
            for (int minor = 6; minor != 10; minor++) {
                for (int r = 0; r != 10; r++) {
                    try {
                        slug = String.format("%s_%s_R%s", major, minor, r);
                        Class.forName("net.minecraft.server." + slug + ".World");
                        break _outer;
                    } catch (ReflectiveOperationException ignored) {

                    }
                }
            }
        }
        MC_SLUG = slug;
        Class hook = null;
        try {
            hook = Class.forName("tk.ivybits.storm.nms." + slug + "." + slug + "Hooks");
        } catch (ClassNotFoundException ignored) {
        }

        if (hook != null) {
            CURRENT_IMPLEMENTATION = (NMSHooks) Reflection.constructor()
                    .in(hook)
                    .newInstance();
            try {
                CURRENT_IMPLEMENTATION.patchMeteor();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        } else
            CURRENT_IMPLEMENTATION = null;


    }

    public static boolean isSupported() {
        return CURRENT_IMPLEMENTATION != null;
    }

    public static void setBlockFastDelayed(final World world,
                                           final int x,
                                           final int y,
                                           final int z,
                                           final int id,
                                           final byte data,
                                           long delay) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Storm.instance, new Runnable() {
            public void run() {
                if (id == world.getBlockTypeIdAt(x, y, z))
                    setBlockFast(world, x, y, z, id, data);
            }
        }, delay);
    }

    public static void setBlockFast(World world,
                                    int x,
                                    int y,
                                    int z,
                                    int typeId,
                                    byte data) {
        CURRENT_IMPLEMENTATION.setBlockFast(world, x, y, z, typeId, data);
    }

    public static void updateChunkClient(Player player,
                                         int x,
                                         int y) {
        CURRENT_IMPLEMENTATION.updateChunkClient(player, x, y);
    }

    public static Fireball spawnMeteor(World world,
                                       int burrowCount,
                                       int burrowPower,
                                       float trailPower,
                                       float explosionRadius,
                                       float brightness,
                                       String crashMessage,
                                       int shockwaveDamage,
                                       int shockwaveDamageRadius,
                                       String damageMessage,
                                       boolean spawnOnImpact,
                                       int radius) {

        return Reflection.method("getBukkitEntity")
                .in(Reflection.constructor()
                        .in(CURRENT_IMPLEMENTATION.getMeteorImplementation())
                        .withParameters(World.class,
                                int.class, int.class, float.class, float.class, float.class,
                                String.class, int.class, int.class, String.class, boolean.class, int.class)
                        .newInstance(world, burrowCount, burrowPower, trailPower, explosionRadius, brightness,
                                crashMessage, shockwaveDamage, shockwaveDamageRadius, damageMessage, spawnOnImpact, radius))
                .withReturnType(Fireball.class)
                .invoke();
    }
}
