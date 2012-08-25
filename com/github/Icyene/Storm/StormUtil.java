package com.github.Icyene.Storm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.Packet250CustomPayload;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.github.Icyene.Storm.Rain.Acid.AcidRain;

public class StormUtil
{
    public static final Logger log = Logger.getLogger("Storm");
    static final String prefix = "[Storm] ";
    private static final Random rand = new Random();

    public void log(String logM)
    {
	log.log(Level.INFO, prefix + logM);
    }

    public void log(Level level, String logM)
    {
	log.log(level, prefix + logM);
    }

    public void broadcast(String message) {
	Bukkit.getServer().broadcastMessage(parseColors(message));
    }

    public void message(Player player, String message) {
	player.sendMessage(parseColors(message));
    }

    public String parseColors(String msg) {
	return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public void damageNearbyPlayers(Location location, double radius,
	    int damage, String message) {

	ArrayList<Player> damagees = getNearbyPlayers(location, radius);

	for (Player p : damagees) {

	    if (!p.getGameMode().equals(GameMode.CREATIVE)) {

		p.damage(damage*2);

		this.message(p, message);
	    }
	}
    }

    public ArrayList<Player> getNearbyPlayers(Location location,
	    double radius) {

	ArrayList<Player> playerList = new ArrayList<Player>();

	for (Player p : Bukkit.getServer().getOnlinePlayers()) {
	    if (p.getWorld().equals(location.getWorld())) {
		Location ploc = p.getLocation();
		ploc.setY(location.getY());
		if (ploc.distance(location) <= radius) {
		    playerList.add(p);

		}
	    }
	}

	return playerList;
    }

    public void transform(Block toTransform, List<Integer[]> transformations) {

	final int blockId = toTransform.getTypeId();
	for (Integer[] toCheck : transformations) {
	    if (toCheck[0] == blockId) {
		toTransform.setTypeId(toCheck[1]);
		return;
	    }
	}
    }

    public Chunk pickChunk(final World w) {
	final Chunk[] loadedChunks = w.getLoadedChunks();
	return loadedChunks[rand.nextInt(loadedChunks.length)];
    }

    public World pickWorld(Storm storm, List<String> natural__Disasters_Meteor_Allowed__Worlds){
	  return storm.getServer().getWorld(natural__Disasters_Meteor_Allowed__Worlds.get(new Random().nextInt(natural__Disasters_Meteor_Allowed__Worlds.size())));
	 }

    public boolean verifyVersion(Storm storm) {

	if (!Storm.config.Version
		.equals(storm.getDescription().getVersion())) {
	    return false; // Tell user config should be updated
	} else {
	    return true;
	}

    }

    public void setTexture(Player toSetOn, String pathToTexture) {
	((CraftPlayer) toSetOn).getHandle().netServerHandler
		.sendPacket(new Packet250CustomPayload("MC|TPack",
			(pathToTexture + "\0" + 16).getBytes()));
    }

    public void clearTexture(Player toClear) {
	setTexture(toClear, Storm.config.Textures_Default__Texture__Path);
    }

    public boolean isPlayerInRain(final Player player) {
	final World world = player.getWorld();
	if (world.hasStorm()) {
	    final Location loc = player.getLocation();
	    final Biome biome = world
		    .getBiome(loc.getBlockX(), loc.getBlockZ());
	    if (AcidRain.rainyBiomes.contains(biome)
		    && world.getHighestBlockYAt(loc) <= loc.getBlockY()) {
		return true;
	    }
	}
	return false;
    }

}
