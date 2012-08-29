/*
 * Storm
 * Copyright (C) 2012 Icyene
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.Icyene.Storm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.Icyene.Storm.Acid_Rain.AcidRain;
import com.github.Icyene.Storm.Blizzard.Blizzard;
import com.github.Icyene.Storm.Lightning.Lightning;
import com.github.Icyene.Storm.Meteors.Meteor;
import com.github.Icyene.Storm.Wildfire.Wildfire;

public class Storm extends JavaPlugin implements Listener
{
    public static List<String> stats = new ArrayList<String>();
    public static GlobalVariables config;
    public static StormUtil util = new StormUtil();

    @Override
    public void onEnable()
    {
	
	config = new GlobalVariables(this);
	config.workaroundLists(); //Stupid workaround for config
	config.load();

	Commands cmds = new Commands();
	
	getCommand("meteor").setExecutor(cmds);
	getCommand("wildfire").setExecutor(cmds);
	
	// Stats
	try {
	    MetricsLite metrics = new MetricsLite(this);
	    metrics.start();
	} catch (IOException e) {
	    // Failed to submit the stats :-(
	}

	try {
	    if (!util.verifyVersion(this)) {
		util.log(Level.WARNING,
			"--------------------------------------------");
		util.log(Level.WARNING,
			"Your configuration is outdated, so some  ");
		util.log(Level.WARNING,
			"of Storm's features may not work correctly! ");
		util.log(Level.WARNING,
			"Delete it, or risk the consequences!      ");
		util.log(Level.WARNING, " ");
		util.log(Level.WARNING, "Thanks for using Storm!");
		util.log(Level.WARNING,
			"--------------------------------------------");
	    }

	    AcidRain.load(this);
	    Lightning.load(this);
	    Wildfire.load(this);
	    Blizzard.load(this);
	    Meteor.load(this);
	    this.getServer().getPluginManager().registerEvents(new TextureManager(), this);
	    this.getServer().getPluginManager().registerEvents(new MultistateManager(), this);
	
	  
	} catch (Exception e) {

	    e.printStackTrace();
	    crashDisable("Failed to initialize subplugins.");

	}
    }

    @Override
    public void onDisable() {
	Blizzard.unload();	
    }

    public void crashDisable(String crash)
    {
	util.log(Level.SEVERE, crash + " Storm disabled.");
	this.setEnabled(false);
    }

    @EventHandler
    public void hitThatPlayer(PlayerInteractEvent e) {

    }

}