package tk.ivybits.storm.nms.v1_7_R1;

import net.minecraft.server.v1_7_R1.EntityEnderDragon;
import net.minecraft.server.v1_7_R1.EntityLargeFireball;
import net.minecraft.server.v1_7_R1.MovingObjectPosition;
import net.minecraft.server.v1_7_R1.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import tk.ivybits.storm.Storm;
import tk.ivybits.storm.utility.StormUtil;
import tk.ivybits.storm.utility.block.BlockTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class v1_7_R1EntityMeteor extends EntityLargeFireball {
    private final HashMap<BlockTransformer.IDBlock, Integer> ores = new HashMap<BlockTransformer.IDBlock, Integer>();
    private float explosionRadius = 50.0F;
    private float trailPower = 20.0F;
    private float brightness = 10.0F;
    private String meteorCrashMessage;
    private int burrowCount = 5;
    private int burrowPower = 10;
    private boolean spawnMeteorOnImpact;
    private int radius = 5;
    private String damageMessage;
    private int shockwaveDamage;
    private int shockwaveDamageRadius;
    private boolean h_lock;
    private boolean h_lock_2;
    private boolean h_lock_3;

    public v1_7_R1EntityMeteor(World world) {
        super(world);
    }

    public v1_7_R1EntityMeteor(org.bukkit.World world, int burrowCount, int burrowPower, float trailPower,
                               float explosionRadius, float brightness, String crashMessage, int shockwaveDamage,
                               int shockwaveDamageRadius, String damageMessage, boolean spawnOnImpact, int radius) {
        super(((CraftWorld) world).getHandle());

        this.burrowPower = burrowPower;
        this.burrowCount = burrowCount;
        this.trailPower = trailPower;
        this.explosionRadius = explosionRadius;
        this.brightness = brightness;
        this.meteorCrashMessage = crashMessage;
        this.shockwaveDamage = shockwaveDamage;
        this.shockwaveDamageRadius = shockwaveDamageRadius;
        this.damageMessage = damageMessage;
        this.damageMessage = damageMessage;
        this.spawnMeteorOnImpact = spawnOnImpact;
        this.radius = radius;
        // For poorly coded plugins which assume fireball.getShooter() != null without first checking
        shooter = new EntityEnderDragon(this.world);

        if (this.spawnMeteorOnImpact)
            for (List ore : Storm.wConfigs.get(world.getName()).Natural__Disasters_Meteor_Ore__Chance__Percentages)
                this.ores.put(new BlockTransformer.IDBlock((String) ore.get(0)), Integer.parseInt((String) ore.get(1)));

        this.world.addEntity(this);
    }

    private static double lengthSq(double x, double y, double z) {
        return x * x + y * y + z * z;
    }

    @Override
    public void h() {
        move();
        super.h();
    }

    private void move() {
        this.h_lock = (!this.h_lock);
        if (!this.h_lock) {
            this.h_lock_2 = (!this.h_lock_2);
            if (!this.h_lock_2) {
                this.h_lock_3 = (!this.h_lock_3);
                if (!this.h_lock_3) {
                    int locY = (int) this.locY;
                    if ((locY & 0xFFFFFF00) != 0) {
                        this.dead = true;
                        return;
                    }

                    if ((locY & 0xFFFFFFE0) == 0) {
                        try {
                            explode();
                        } catch (NullPointerException ignored) {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    if (!StormUtil.isBlockProtected(this.world.getWorld().getBlockAt((int) this.locX, locY, (int) this.locZ))) {
                        world.createExplosion(this, this.locX, this.locY, this.locZ, this.trailPower, true, true);
                    }
                }
            }
        }
        this.motX *= 1.5;
        this.motY *= 1.5;
        this.motZ *= 1.5;
    }

    @Override
    public void a(MovingObjectPosition movingobjectposition) {
        if (this.burrowCount > 0) {
            if (!StormUtil.isBlockProtected(this.world.getWorld().getBlockAt((int) this.locX, (int) this.locY, (int) this.locZ))) {
                world.createExplosion(this, this.locX, this.locY, this.locZ, this.burrowPower, true, true);
            }
            this.burrowCount -= 1;
            return;
        }
        if (!StormUtil.isBlockProtected(this.world.getWorld().getBlockAt((int) this.locX, (int) this.locY, (int) this.locZ))) {
            try {
                explode();
            } catch (NullPointerException ignored) {
            }
        }
    }

    private void explode() {
        world.createExplosion(this, this.locX, this.locY, this.locZ, this.explosionRadius, true, true);

        StormUtil.damageNearbyPlayers(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ), this.shockwaveDamageRadius, this.shockwaveDamage, this.damageMessage, "storm.meteor.immune");

        StormUtil.broadcast(this.meteorCrashMessage.replace("%x", (int) this.locX + "").replace("%z", (int) this.locZ + "").replace("%y", (int) this.locY + ""), this.world.getWorld());

        if (this.spawnMeteorOnImpact) {
            spawnMeteor(new Location(this.world.getWorld(), this.locX, this.locY, this.locZ));
        }
        die();
    }

    private void addOres(ArrayList<BlockTransformer.IDBlock> result, BlockTransformer.IDBlock material, int percentage) {
        for (int i = 0; (i < percentage) &&
                (result.size() < 100); i++) {
            result.add(material);
        }
    }

    private void spawnMeteor(Location explosion) {
        ArrayList<BlockTransformer.IDBlock> orez = new ArrayList<BlockTransformer.IDBlock>();
        for (Map.Entry en : this.ores.entrySet()) {
            addOres(orez, (BlockTransformer.IDBlock) en.getKey(), (Integer) en.getValue());
        }
        while (explosion.getBlock().getTypeId() == 0) {
            explosion.add(0, -1.0, 0);
        }
        explosion.add(0, this.radius + 1, 0);
        makeSphere(explosion, null, this.radius, true, true, orez);
        makeSphere(explosion, Material.OBSIDIAN.getId(), this.radius, false, false, null);
    }

    void makeSphere(Location pos, Integer block, double radius, boolean filled,
                    boolean random, ArrayList<BlockTransformer.IDBlock> m) {
        double radius_ = radius + 0.5D;
        double invRadiusZ;
        double invRadiusY;
        double invRadiusX = invRadiusY = invRadiusZ = 1.0 / radius_;
        int ceilRadiusZ;
        int ceilRadiusY;
        int ceilRadiusX = ceilRadiusY = ceilRadiusZ = (int) radius_ + 1;

        double nextXn = 0;

        label311:
        for (int x = 0; x <= ceilRadiusX; x++) {
            double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextYn = 0;

            for (int y = 0; y <= ceilRadiusY; y++) {
                double yn = nextYn;
                nextYn = (y + 1) * invRadiusY;
                double nextZn = 0;

                for (int z = 0; z <= ceilRadiusZ; z++) {
                    double zn = nextZn;
                    nextZn = (z + 1) * invRadiusZ;

                    double distanceSq = lengthSq(xn, yn, zn);
                    if (distanceSq > 1.0) {
                        if (z != 0) break;
                        if (y != 0) break label311;
                        return;
                    }

                    if ((!filled) && (lengthSq(nextXn, yn, zn) <= 1.0) && (lengthSq(xn, nextYn, zn) <= 1.0D) && (lengthSq(xn, yn, nextZn) <= 1.0D)) {
                        continue;
                    }

                    for (int i = 0; i < 8; i++)
                        (random ? chooseRandom(m) : new BlockTransformer.IDBlock(block.toString())).setBlock(pos.clone().add(-x, -y, -z).getBlock());
                }
            }
        }
    }

    private BlockTransformer.IDBlock chooseRandom(ArrayList<BlockTransformer.IDBlock> mats) {
        return mats.get(Storm.random.nextInt(mats.size()));
    }

    public float d(float f) {
        return this.brightness;
    }
}