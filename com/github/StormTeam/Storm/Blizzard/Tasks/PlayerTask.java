package com.github.StormTeam.Storm.Blizzard.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.StormTeam.Storm.GlobalVariables;
import com.github.StormTeam.Storm.Storm;

public class PlayerTask {

    private int id;
    private World affectedWorld;
    private Storm storm;
    private GlobalVariables glob;

    public PlayerTask(Storm storm, World spawnWorld) {
        this.storm = storm;
        this.affectedWorld = spawnWorld;
        glob = Storm.wConfigs.get(spawnWorld);

    }

    public void run() {

        id = Bukkit.getScheduler()
                .scheduleSyncRepeatingTask(
                storm,
                new Runnable() {
                    @Override
                    public void run() {

                        for (Player damagee : affectedWorld
                                .getPlayers()) {
                            if (!damagee.getGameMode().equals(
                                    GameMode.CREATIVE)) {

                                //   Player damagee = damagee;
                                if (!Storm.biomes.isTundra(
                                        damagee.getLocation()
                                        .getBlock().getBiome())) {
                                    return;
                                }

                                if (Storm.util
                                        .isPlayerUnderSky(damagee)) {
                                    damagee.addPotionEffect(
                                            new PotionEffect(
                                            PotionEffectType.BLINDNESS,
                                            glob.Blizzard_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks + 60,
                                            glob.Blizzard_Damager_Blindness__Amplitude),
                                            true);
                                }

                                if (glob.Blizzard_Damager_Heating__Blocks.contains(damagee.getItemInHand().getTypeId())) {
                                    return;
                                }

                                final Location loc = damagee.getLocation();
                                final World world = damagee.getWorld();
                                int radius = glob.Blizzard_Damager_Heat__Radius;
                                for (int y = 1; y > -radius; y--) {
                                    for (int x = 1; x > -radius; x--) {
                                        for (int z = 1; z > -radius; z--) {
                                            Block scan = world
                                                    .getBlockAt(
                                                    (int) loc
                                                    .getX()
                                                    + x,
                                                    (int) loc
                                                    .getY()
                                                    + y,
                                                    (int) loc
                                                    .getZ()
                                                    + z);
                                            if (glob.Blizzard_Damager_Heating__Blocks
                                                    .contains(scan
                                                    .getTypeId())) {
                                                return; // Don't damage
                                                // if they are
                                                // near hot
                                                // blocks
                                            }
                                        }
                                    }
                                }

                                damagee.damage(glob.Blizzard_Player_Damage__From__Exposure * 2);
                                Storm.util
                                        .message(
                                        damagee,
                                        glob.Blizzard_Damager_Message__On__Player__Damaged__Cold);

                            }
                        }
                    }
                },
                glob.Blizzard_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks,
                glob.Blizzard_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks);

    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }
}
