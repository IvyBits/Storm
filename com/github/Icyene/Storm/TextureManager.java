package com.github.Icyene.Storm;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.Icyene.Storm.Acid_Rain.AcidRain;
import com.github.Icyene.Storm.Acid_Rain.Events.AcidRainEvent;
import com.github.Icyene.Storm.Blizzard.Blizzard;
import com.github.Icyene.Storm.Blizzard.Events.BlizzardEvent;

public class TextureManager implements Listener
{

	@EventHandler
	public void worldEvent(PlayerChangedWorldEvent e){

		final Player hopper = e.getPlayer();
		final World toWorld = hopper.getWorld();
		final World fromWorld = e.getFrom();

		if (!toWorld.equals(fromWorld)) {
			if (Blizzard.blizzardingWorlds.containsKey(toWorld)
			        && Blizzard.blizzardingWorlds.get(toWorld)) {

				Storm.util
				        .setTexture(
				                hopper,
				                Storm.wConfigs.get(toWorld.getName()).Textures_Blizzard__Texture__Path);

				return;

			}

			if (AcidRain.acidicWorlds.containsKey(toWorld)
			        && AcidRain.acidicWorlds.get(toWorld)) {

				Storm.util
				        .setTexture(
				                hopper,
				                Storm.wConfigs.get(toWorld.getName()).Textures_Acid__Rain__Texture__Path);

				return;

			}
			Storm.util.clearTexture(hopper);
		}
	}

	@EventHandler
	public void loginEvent(PlayerJoinEvent e) {
		final Player hopper = e.getPlayer();
		final World world = hopper.getWorld();

		if (Blizzard.blizzardingWorlds.containsKey(world)
		        && Blizzard.blizzardingWorlds.get(world)) {

			Storm.util
			        .setTexture(
			                hopper,
			                Storm.wConfigs.get(world.getName()).Textures_Blizzard__Texture__Path);

			return;

		}

		if (AcidRain.acidicWorlds.containsKey(world)
		        && AcidRain.acidicWorlds.get(world)) {
			Storm.util
			        .setTexture(
			                hopper,
			                Storm.wConfigs.get(world.getName()).Textures_Acid__Rain__Texture__Path);

			return;

		}
		Storm.util.clearTexture(hopper);

	}

	@EventHandler
	public void setAcidTexture(AcidRainEvent event) {		
		
		final World world = event.getAffectedWorld();

		if (event.getWeatherState()) {

			for (Player p : world.getPlayers()) {
				Storm.util
				        .setTexture(
				                p,
				                Storm.wConfigs.get(p.getWorld().getName()).Textures_Acid__Rain__Texture__Path);

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
				Storm.util
				        .setTexture(
				                p,
				                Storm.wConfigs.get(p.getWorld().getName()).Textures_Blizzard__Texture__Path);

			}
		} else {

			for (Player p : world.getPlayers()) {
				Storm.util.clearTexture(p);
			}
		}
	}
}
