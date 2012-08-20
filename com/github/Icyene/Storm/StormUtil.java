package com.github.Icyene.Storm;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class StormUtil
{
    public static final Logger log = Logger.getLogger("Minecraft");
    static final String prefix = "[Storm] ";

    public static void log(String logM)
    {
	log.log(Level.INFO, prefix + logM);
    }

    public static void log(Level level, String logM)
    {
	log.log(level, prefix + logM);
    }

    public static void broadcast(String message) {
	Bukkit.getServer().broadcastMessage(parseColors(message));
    }

    public static void message(Player player, String message) {
	player.sendMessage(parseColors(message));
    }

    public static String parseColors(String msg) {
	return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void damageNearbyPlayers(Location location, double radius,
	    int damage, String message) {

	ArrayList<Player> damagees = getNearbyPlayers(location, radius);

	for (Player p : damagees) {

	    if (p.getGameMode() != GameMode.CREATIVE) {
		p.damage((p.getHealth() - damage));
		StormUtil.message(p,
			message);
	    }
	}
    }

    public static ArrayList<Player> getNearbyPlayers(Location location,
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

    public static void transform(Block toTransform, Integer[][] transformations) {

	final int blockId = toTransform.getTypeId();
	for (Integer[] toCheck : transformations) {
	    if (toCheck[0] == blockId) {
		System.out.println("Transformed a blockzor.");
		toTransform.setTypeId(toCheck[1]);
		return;
	    }
	}
    }
    
    public static boolean verifyVersion(Storm storm) {
	
	if(!GlobalVariables.Version.equals(storm.getDescription().getVersion())) {
	    return false; //Tell user config should be updated
	} else {
	    return true;
	}
	
    }
    

}
