package com.github.StormTeam.Storm.Blizzard.Tasks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
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
                            if (damagee.getGameMode() != GameMode.CREATIVE) {

                                //   Player damagee = damagee;
                                if (!Storm.biomes.isTundra(
                                        damagee.getLocation()
                                        .getBlock().getBiome())) {
                                    System.out.println("Not in tundra. In biome: " + damagee.getLocation()
                                            .getBlock().getBiome());
                                    return;
                                }

                                if (glob.Blizzard_Damager_Heating__Blocks.contains(damagee.getItemInHand().getTypeId())) {
                                    System.out.println("Has hot item in hand, returning.");
                                    return;
                                }

                                if (Storm.util.isLocationNearBlock(damagee.getLocation(),
                                        glob.Blizzard_Damager_Heating__Blocks, glob.Blizzard_Damager_Heat__Radius)) {
                                    System.out.println("IS near hot blocks, returning.");
                                    return;
                                }


                                if (Storm.util
                                        .isPlayerUnderSky(damagee)) {
                                    damagee.damage(glob.Blizzard_Player_Damage__From__Exposure * 2);
                                    damagee.addPotionEffect(
                                            new PotionEffect(
                                            PotionEffectType.BLINDNESS,
                                            glob.Blizzard_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks + 60,
                                            glob.Blizzard_Damager_Blindness__Amplitude),
                                            true);
                                    Storm.util
                                            .message(
                                            damagee,
                                            glob.Blizzard_Damager_Message__On__Player__Damaged__Cold);
                                } else {
                                    System.out.println("Player is not under sky.");
                                }


                            } else {
                                System.out.println("Player is in creative mode.");
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
