package com.github.Icyene.Storm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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

    public static void addConfigToStats(final Plugin plugin,
	    Class<?> toConfigurate, String base) {

	for (Field field : toConfigurate.getDeclaredFields()) {
	    final int mod = field.getModifiers();
	    if (Modifier.isStatic(mod)
		    && !Modifier.isTransient(mod) && !Modifier.isVolatile(mod)) {

		final String path = field.getName().replaceAll("_", ".");
		final String basePath = base + path;
		try {

		    Storm.stats.add(basePath + ": " + field.get(null));

		} catch (Exception e) {

		}
	    }
	}

    }

}
