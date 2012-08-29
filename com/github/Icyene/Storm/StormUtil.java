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

import com.github.Icyene.Storm.Acid_Rain.AcidRain;

public class StormUtil
{
    public static final Logger log = Logger.getLogger("Storm");
    private static final Random rand = new Random();

    public void log(String logM)
    {
	log.log(Level.INFO, logM);
    }

    public void log(Level level, String logM)
    {
	log.log(level, logM);
    }

    public void broadcast(String message) {
	if(message.isEmpty()) {
	    return;
	}
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

		p.damage(damage * 2);

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

   public void transform(Block toTransform,
	    List<List<String>> transformations) {

	final int blockId = toTransform.getTypeId();
	final int blockData = toTransform.getData();

	for (List<String> toCheck : transformations) {

	    String[] curState = new String[2];
	    String[] toState = new String[2];
	    if (toCheck.get(0).contains(":")) {

		curState = toCheck.get(0).split(":");

	    } else {

		curState[0] = toCheck.get(0);
		curState[1] = "0";

	    }

	    if (toCheck.get(1).contains(":")) {

		toState = toCheck.get(1).split(":");

	    } else {

		toState[0] = toCheck.get(1);
		toState[1] = "0";

	    }

	    if (Integer.valueOf(curState[0]) == blockId
		    && Integer.valueOf(curState[1]) == blockData) {
		toTransform.setTypeIdAndData(Integer.valueOf(toState[0]),
			Byte.parseByte(toState[0]), true); // Ewwww
		return;
	    }

	}

    }

    public Chunk pickChunk(final World w) {

	if (w == null) {
	    Storm.util
		    .log("The universe tried to f*ck with Storm by giving it a null world. Operation aborted :D");
	    return null; // Somehow at times it passes a null world. The dafuq?
	}

	try {
	    final Chunk[] loadedChunks = w.getLoadedChunks();
	    return loadedChunks[rand.nextInt(loadedChunks.length)];
	} catch (Exception e) {
	    Storm.util.log("World " + w.getName()
		    + " has no loaded chunks!");
	}
	return null;

    }

    public World pickWorld(final Storm storm, List<String> world) {

	ArrayList<World> worlds = new ArrayList<World>();
	for (World w : storm.getServer().getWorlds()) {
	    if (MultiWorldManager.checkWorld(w, world)) {
		worlds.add(w);
	    }
	}

	if (worlds.size() == 1) {
	    return worlds.get(0);
	} else {
	    return worlds.get(rand.nextInt(worlds.size()));
	}
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
