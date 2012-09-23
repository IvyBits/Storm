/*
 * Storm
 * Copyright (C) 2012 Icyene, Thidox
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
package com.github.StormTeam.Storm;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.github.StormTeam.Storm.Acid_Rain.AcidRain;
import com.github.StormTeam.Storm.Blizzard.Blizzard;
import com.github.StormTeam.Storm.Database.Database;
import com.github.StormTeam.Storm.Earthquake.Earthquake;
import com.github.StormTeam.Storm.Lightning.Lightning;
import com.github.StormTeam.Storm.Meteors.Meteor;
import com.github.StormTeam.Storm.Wildfire.Wildfire;
import github.StormTeam.Storm.UpdaterVariables;

import java.util.HashMap;

public class Storm extends JavaPlugin {

    public static HashMap<String, GlobalVariables> wConfigs = new HashMap<String, GlobalVariables>();
    public static BiomeGroups biomes = new BiomeGroups();
    public static StormUtil util;
    public Commands cmds;
    private Database db;
    public static PluginManager pm = Bukkit.getPluginManager();
    public static UpdaterVariables updater;

    @Override
    public void onEnable() {

        // Make per-world configuration files
        for (World world : Bukkit.getWorlds()) {
            String name = world.getName();
            GlobalVariables config = new GlobalVariables(this, name);
            config.load();
            wConfigs.put(name, config);
        }

        updater = new UpdaterVariables(this, "updater");
        updater.load();


        boolean update = false;

        if (updater.Updater_Check__For__Updates) {
            Updater up = new Updater(this, "storm", this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
            update = up.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
            if (update) {
                util.log("A new version of Storm is available: " + up.getLatestVersionString() + " (" + up.getFileSize() + " bytes).");
            }

            if (update && updater.Updater_Automagically__Update) {
                Updater dl = new Updater(this, "storm", this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
            } else {
                util.log("It is highly reccomended that you download and install this update.");
            }

        }


        util = new StormUtil(this);
        db = Database.Obtain(this, null);
        cmds = new Commands(this);

        getCommand("meteor").setExecutor(cmds);
        getCommand("wildfire").setExecutor(cmds);
        getCommand("acidrain").setExecutor(cmds);
        getCommand("blizzard").setExecutor(cmds);

        // Stats
        try {
            new MetricsLite(this).start();
        } catch (Exception e) {
        }

        //Modularity FTW!
        AcidRain.load(this);
        Lightning.load(this);
        Wildfire.load(this);
        Blizzard.load(this);
        Meteor.load(this);
        Earthquake.load(this);
        //Puddles.load(this);
        pm.registerEvents(new TextureManager(), this);
    }

    @Override
    public void onDisable() {
        Blizzard.unload();
        this.db.getEngine().close();
    }
}