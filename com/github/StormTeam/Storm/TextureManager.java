package com.github.StormTeam.Storm;

import com.github.StormTeam.Storm.Storm;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.StormTeam.Storm.Acid_Rain.AcidRain;
import com.github.StormTeam.Storm.Acid_Rain.Events.AcidRainEvent;
import com.github.StormTeam.Storm.Blizzard.Blizzard;
import com.github.StormTeam.Storm.Blizzard.Events.BlizzardEvent;

public class TextureManager implements Listener {

    @EventHandler
    public void worldEvent(PlayerChangedWorldEvent e) {

        Player hopper = e.getPlayer();
        World toWorld = hopper.getWorld();

        if (!toWorld.equals(e.getFrom())) {

            if (setBlizzardTPack(toWorld, hopper)) {
                return;
            }

            if (setAcidTPack(toWorld, hopper)) {
                return;
            }

            Storm.util.clearTexture(hopper);
        }
    }

    @EventHandler
    public void loginEvent(PlayerJoinEvent e) {
        Player hopper = e.getPlayer();

        if (setBlizzardTPack(hopper.getWorld(), hopper)) {
            return;
        }
        if (setAcidTPack(hopper.getWorld(), hopper)) {
            return;
        }
    }

    @EventHandler
    public void setAcidTexture(AcidRainEvent event) {

        final World world = event.getAffectedWorld();

        if (event.getWeatherState()) {

            for (Player p : world.getPlayers()) {
                setAcidTPack(world, p);
            }
        } else {

            for (Player p : world.getPlayers()) {
                Storm.util.clearTexture(p);
            }
        }
    }

    @EventHandler
    public void setBlizzardTexture(BlizzardEvent event) {

        final World world = event.getAffectedWorld();

        if (event.getWeatherState()) {

            for (Player p : world.getPlayers()) {
                setBlizzardTPack(world, p);
            }
        } else {

            for (Player p : world.getPlayers()) {
                Storm.util.clearTexture(p);
            }
        }
    }

    private boolean setAcidTPack(World world, Player hopper) {
        if (AcidRain.acidicWorlds.containsKey(world)
                && AcidRain.acidicWorlds.get(world)) {
            Storm.util
                    .setTexture(
                    hopper,
                    Storm.wConfigs.get(world.getName()).Textures_Acid__Rain__Texture__Path);

            return true;

        }
        return false;


    }

    private boolean setBlizzardTPack(World world, Player hopper) {

        if (Blizzard.blizzardingWorlds.containsKey(world)
                && Blizzard.blizzardingWorlds.get(world)) {

            Storm.util
                    .setTexture(
                    hopper,
                    Storm.wConfigs.get(world.getName()).Textures_Blizzard__Texture__Path);

            return true;

        }
        return false;
    }
}
