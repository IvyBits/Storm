package com.github.Icyene.Storm;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
}
