package com.github.Icyene.Storm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ReflectConfiguration {

    /*
     * Based on codename_B's non static config 'offering' :-)
     * 
     */
    
    private Plugin plugin;

    public ReflectConfiguration(Plugin storm) {
	this.plugin = storm;
    }

    public void load() {
	if(plugin != null) {
		try {
			onLoad(plugin);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else {
		new InvalidConfigurationException("Plugin cannot be null!").printStackTrace();
	}
    }
    
    private void onLoad(Plugin plugin) throws Exception {
	FileConfiguration conf = plugin.getConfig();
	for(Field field : getClass().getDeclaredFields()) {
		String path = "Storm." + field.getName().replaceAll("__", " ").replaceAll("_", ".");
		if(doSkip(field)) {
		  //  System.out.println("Path " + path + " is skipped.");
		} else if(conf.isSet(path)) {
		 //   System.out.println("Path " + path + " is set.");
			field.set(this, conf.get(path));
		} else {
		//    System.out.println("Path " + path + " has been set.");
			conf.set(path, field.get(this));
		}
	}
	plugin.saveConfig();
}

    private boolean doSkip(Field field) {
	return Modifier.isTransient(field.getModifiers())
		|| Modifier.isStatic(field.getModifiers())
		|| Modifier.isFinal(field.getModifiers())
		|| Modifier.isPrivate(field.getModifiers());
    }

}
