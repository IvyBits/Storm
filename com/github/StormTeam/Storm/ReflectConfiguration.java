package com.github.StormTeam.Storm;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ReflectConfiguration {

    /*
     * Based on codename_B's non static config 'offering' :-)
     */
    private Plugin plugin;
    private String name;

    public ReflectConfiguration(Plugin storm, String name) {
        this.plugin = storm;
        this.name = name;
    }

    public void load() {

        try {
            onLoad(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onLoad(Plugin plugin) throws Exception {

        File worldFile = new File(plugin.getDataFolder(), name + ".yml");

        FileConfiguration worlds = YamlConfiguration
                .loadConfiguration(worldFile);

        for (Field field : getClass().getDeclaredFields()) {
            String path = "Storm."
                    + field.getName().replaceAll("__", " ")
                    .replaceAll("_", ".");
            if (doSkip(field)) {
            } else if (worlds.isSet(path)) {
                field.set(this, worlds.get(path));
            } else {
                worlds.set(path, field.get(this));
            }
        }

        final FileConfiguration finalWorlds = worlds;
        final File finalWorldFile = worldFile;

        // Nasty, no good fix for the stupidity of FileConfiguration.save not
        // using BufferedOutputStream when saving. Causes only one file to be
        // generated.

        Bukkit.getScheduler()
                .scheduleAsyncDelayedTask(
                plugin,
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            finalWorlds.save(finalWorldFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 1);
    }

    private boolean doSkip(Field field) {
        int mod = field.getModifiers();
        return Modifier.isTransient(mod)
                || Modifier.isStatic(mod)
                || Modifier.isFinal(mod)
                || Modifier.isPrivate(mod);
    }
}
