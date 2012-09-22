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

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.Icyene.Storm.Acid_Rain.AcidRain;
import com.github.Icyene.Storm.Blizzard.Blizzard;
import com.github.Icyene.Storm.Database.Database;
import com.github.Icyene.Storm.Earthquake.Earthquake;
import com.github.Icyene.Storm.Email.EmailLogger;
import com.github.Icyene.Storm.Email.EmailVariables;
import com.github.Icyene.Storm.Lightning.Lightning;
import com.github.Icyene.Storm.Meteors.Meteor;
import com.github.Icyene.Storm.Wildfire.Wildfire;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.logging.Level;

public class Storm extends JavaPlugin
{

	public static HashMap<String, GlobalVariables> wConfigs = new HashMap<String, GlobalVariables>();
	public static BiomeGroups biomes;
	public static StormUtil util;
	public static Commands cmds;
	public static EmailVariables eV;
	public static EmailLogger eL = new EmailLogger();
	private Database db;

	@Override
	public void onEnable() {

		// Make per-world configuration files
		for (World w : Bukkit.getWorlds()) {
			String world = w.getName();
			GlobalVariables config = new GlobalVariables(this, world);
			config.workaroundLists(); // Stupid workaround for config
			config.load();
			wConfigs.put(world, config);
		}
		eV = new EmailVariables(this, "emailLogging");
		eV.load();

		if (eV.Email_Email__Developers__On__Crash) {
			Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

				private String getStackTrace(Throwable aThrowable) {
					final Writer result = new StringWriter();
					final PrintWriter printWriter = new PrintWriter(result);
					aThrowable.printStackTrace(printWriter);
					return result.toString();
				}

				@Override
				public void uncaughtException(Thread arg0, Throwable arg1) {
					Storm.eL.sendStackTrace(getStackTrace(arg1));

				}
			});
		}

		util = new StormUtil(this);
		biomes = new BiomeGroups();
		db = Database.Obtain(this, null);
		cmds = new Commands(this);

		getCommand("meteor").setExecutor(cmds);
		getCommand("wildfire").setExecutor(cmds);
		getCommand("acidrain").setExecutor(cmds);
		getCommand("blizzard").setExecutor(cmds);

		// Stats
		try {
			new MetricsLite(this).start();
		} catch (IOException e) {}

		AcidRain.load(this);
		Lightning.load(this);
		Wildfire.load(this);
		Blizzard.load(this);
		Meteor.load(this);
		Earthquake.load(this);
		this.getServer().getPluginManager().registerEvents(
		        new TextureManager(), this);
	}

	@Override
	public void onDisable() {
		Blizzard.unload();
		this.db.getEngine().close();
	}

	public void crashDisable(String crash) {
		util.log(Level.SEVERE, crash + " Storm disabled.");
		this.setEnabled(false);
	}

}