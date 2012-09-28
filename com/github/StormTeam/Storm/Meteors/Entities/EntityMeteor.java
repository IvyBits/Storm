package com.github.StormTeam.Storm.Meteors.Entities;

import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.World;

public class EntityMeteor extends MeteorBase {

    public EntityMeteor(World world) {
        super(world);
    }

    public EntityMeteor(World world, int burrowCount, int burrowPower,
            float trailPower, float explosionRadius, float brightness,
            String crashMessage, int shockwaveDamage,
            int shockwaveDamageRadius, String damageMessage, int snowRadius,
            boolean spawnOnImpact, int radius) {
        super(world, burrowCount, burrowPower, trailPower, explosionRadius, brightness, crashMessage, shockwaveDamage, shockwaveDamageRadius, damageMessage, snowRadius, spawnOnImpact, radius);

    }

    //1.3.x
    public void h_() {
        if (this.move()) {
            updatePosition();
        }
    }

    public void a(MovingObjectPosition movingobjectposition) {
        this.burrow();
        super.a();
    }

    public float c(float f) {
        return this.brightness;
    }

    //1.2.x
    public void F_() {
        if (this.move()) {
            updatePosition();
        }
    }

    public float b(float f) {
        return this.brightness;
    }
}