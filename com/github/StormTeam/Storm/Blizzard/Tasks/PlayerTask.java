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
    private PotionEffect blindness;
    
    public PlayerTask(Storm storm, World spawnWorld) {
        this.storm = storm;
        this.affectedWorld = spawnWorld;
        glob = Storm.wConfigs.get(spawnWorld);
        blindness = new PotionEffect(
                PotionEffectType.BLINDNESS,
                glob.Blizzard_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks + 60,
                glob.Blizzard_Damager_Blindness__Amplitude);
        
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
                                    GameMode.CREATIVE) && Storm.util
                                    .isPlayerUnderSky(damagee) && Storm.util.isSnowy(damagee.getLocation().getBlock().getBiome())) {
                                
                                if (glob.Blizzard_Damager_Heating__Blocks.contains(damagee.getItemInHand().getTypeId()) || Storm.util.isLocationNearBlock(damagee.getLocation(),
                                        glob.Blizzard_Damager_Heating__Blocks, glob.Blizzard_Damager_Heat__Radius)) {
                                    return;
                                }
                                
                                damagee.damage(glob.Blizzard_Player_Damage__From__Exposure * 2);
                                damagee.addPotionEffect(blindness, true);
                                Storm.util
                                        .message(damagee, glob.Blizzard_Damager_Message__On__Player__Damaged__Cold);
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
