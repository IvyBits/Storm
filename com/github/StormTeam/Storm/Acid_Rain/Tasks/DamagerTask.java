package com.github.StormTeam.Storm.Acid_Rain.Tasks;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.StormTeam.Storm.GlobalVariables;
import com.github.StormTeam.Storm.Storm;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class DamagerTask {

    private int id;
    private Random rand = new Random();
    private World affectedWorld;
    private Storm storm;
    private GlobalVariables glob;
    
    //Acid__Rain__Absorbing__Blocks

    public DamagerTask(Storm storm, World affectedWorld) {
        this.storm = storm;
        this.affectedWorld = affectedWorld;
        glob = Storm.wConfigs.get(affectedWorld);
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
                            if (!damagee.getGameMode().equals(GameMode.CREATIVE)) {

                                if (!Storm.util
                                        .isPlayerUnderSky(damagee)) {
                                    return;
                                } else {
                                }

                                
                                 if (glob.Acid__Rain__Absorbing__Blocks.contains(damagee.getItemInHand().getTypeId())) {
                                    return;
                                }

                                final Location loc = damagee.getLocation();
                                final World world = damagee.getWorld();
                                int radius = glob.Acid__Rain__Absorbing__Radius;
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
                                            if (glob.Acid__Rain__Absorbing__Blocks
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
                                
                                
                                damagee.damage(glob.Acid__Rain_Player_Damage__From__Exposure * 2);

                                damagee.addPotionEffect(
                                        new PotionEffect(
                                        PotionEffectType.HUNGER,
                                        rand.nextInt(600) + 300,
                                        1), true);            

                                Storm.util
                                        .message(
                                        damagee,
                                        glob.Acid__Rain_Damager_Message__On__Player__Damaged__By__Acid__Rain);

                            }
                        }
                    }
                },
                glob.Acid__Rain_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks,
                glob.Acid__Rain_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks);

    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(id);
    }
}
