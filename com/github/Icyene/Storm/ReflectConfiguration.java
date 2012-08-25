package com.github.Icyene.Storm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ReflectConfiguration {

    /**
     * Inspired by md_5
     * 
     * An awesome super-duper-lazy Config lib!
     * Just extend it, set some (non-static) variables
     * 
     * @author codename_B
     */

    	private transient Plugin plugin = null;
    	
    	/**
    	 * When using this constructor, remember that
    	 * config.load(); will be unavailable
    	 */
    	public ReflectConfiguration() {
    		// don't do anything here
    	}
    	
    	/**
    	 * This constructor stores an reference to your
    	 * plugin, so you can be even more lazy!
    	 */
    	public ReflectConfiguration(Plugin plugin) {
    		this.plugin = plugin;
    	}
    	
    	/**
    	 * Lazy load
    	 */
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
    	
    	/**
    	 * Load a specific config
    	 * @param plugin
    	 */
    	public void load(Plugin plugin) {
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
    	
    	/**
    	 * Lazy save
    	 */
    	public void save() {
    		if(plugin != null) {
    			try {
    				onSave(plugin);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		} else {
    			new InvalidConfigurationException("Plugin cannot be null!").printStackTrace();
    		}
    	}
    	
    	/**
    	 * Save a specific config
    	 * @param plugin
    	 */
    	public void save(Plugin plugin) {
    		if(plugin != null) {
    			try {
    				onSave(plugin);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		} else {
    			new InvalidConfigurationException("Plugin cannot be null!").printStackTrace();
    		}
    	}
    	
    	/**
    	 * Internal method - used by both load() and load(Plugin plugin)
    	 * @param plugin
    	 * @throws Exception
    	 */
    	private void onLoad(Plugin plugin) throws Exception {
    		FileConfiguration conf = plugin.getConfig();
    		for(Field field : getClass().getDeclaredFields()) {
    			String path = "Storm." + field.getName().replaceAll("_", " ").replaceAll("_", ".");
    			if(doSkip(field)) {
    				// don't touch it
    			} else if(conf.isSet(path)) {
    				field.set(this, conf.get(path));
    			} else {
    				conf.set(path, field.get(this));
    			}
    		}
    		plugin.saveConfig();
    	}
    	
    	/**
    	 * Internal method - used by both save() and save(Plugin plugin)
    	 * @param plugin
    	 * @throws Exception
    	 */
    	private void onSave(Plugin plugin) throws Exception {
    		FileConfiguration conf = plugin.getConfig();
    		for(Field field : getClass().getDeclaredFields()) {
    			String path = "Storm." + field.getName().replaceAll("_", " ").replaceAll("_", ".");
    			if(doSkip(field)) {
    				// don't touch it
    			} else {
    				conf.set(path, field.get(this));
    			}
    		}
    		plugin.saveConfig();
    	}
    	
    	/**
    	 * A little internal method to save re-using code
    	 * @param field
    	 * @return skip
    	 */
    	private boolean doSkip(Field field) {
    		return Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()) || Modifier.isPrivate(field.getModifiers());
    	}
    	
    }


