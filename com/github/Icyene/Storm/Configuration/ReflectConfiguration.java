package com.github.Icyene.Storm.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ReflectConfiguration {
    public static void load(final Plugin plugin, Class<?> toConfigurate) {
	final FileConfiguration configuration = plugin.getConfig();
	for (Field field : toConfigurate.getDeclaredFields()) {
	    final int mod = field.getModifiers();
	    if (Modifier.isStatic(mod)
		    && !Modifier.isTransient(mod) && !Modifier.isVolatile(mod)) {
		final String path = field.getName().replaceAll("_", ".");

		try {
		    if (configuration.isSet(path)) {
			field.set(null, configuration.get(path));

		    } else {
			configuration.set(path, field.get(null));
		    }
		} catch (Exception e) {

		}
	    }
	}
	plugin.saveConfig();
    }
}
