package com.github.StormTeam.Storm.Blizzard.Blocks;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Blizzard.Blizzard;

import net.minecraft.server.*;

public class SnowLayer extends BlockSnow {

    public SnowLayer(int i, int j) {
        super(i, j);
    }

    @Override
    public void a(final World w, final int x, final int y,
            final int z, final Entity e) {

        final CraftWorld cWorld = (CraftWorld) w.getWorld();

        if(!Blizzard.blizzardingWorlds.containsKey(cWorld)) {
            return;
        }
        
        if (!Storm.wConfigs.containsKey(cWorld) && !Storm.wConfigs.get(cWorld).Features_Blizzards_Slowing__Snow) {
            return;
        }

        final org.bukkit.entity.Entity inSnow = e.getBukkitEntity();
        if (inSnow instanceof Player && ((Player) (inSnow)).getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (!Storm.biomes.isTundra(inSnow.getLocation().getBlock().getBiome())) {
            return;
        }

        inSnow.setVelocity(inSnow.getVelocity().clone()
                .multiply(Storm.wConfigs.get(w.getWorld()).Blizzard_Player_Speed__Loss__While__In__Snow));

    }
}
