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

import com.github.StormTeam.Storm.Acid_Rain.AcidRain;
import com.github.StormTeam.Storm.Blizzard.Blizzard;
import com.github.StormTeam.Storm.Database.Database;
import com.github.StormTeam.Storm.Lightning.Lightning;
import com.github.StormTeam.Storm.Meteors.Meteor;
import com.github.StormTeam.Storm.Thunder_Storm.ThunderStorm;
import com.github.StormTeam.Storm.Wildfire.Wildfire;

import java.util.HashMap;
import java.util.logging.Level;
import org.bukkit.plugin.PluginManager;

public class Storm extends JavaPlugin {

    public static HashMap<World, GlobalVariables> wConfigs = new HashMap<World, GlobalVariables>();  
    public static BiomeGroups biomes;
    public static StormUtil util;
    public static Commands cmds;
    private Database db;
    public static PluginManager pm;
    public static double version;

    @Override
    public void onEnable() {
        try {
            pm = getServer().getPluginManager();

            String v = getServer().getVersion();
            if (v.contains("1.2.")) {
                version = 1.2;
                getLogger().log(Level.INFO, "Loading with MC 1.2.X compatibility.");
            } else {
                if (v.contains("1.3.")) {
                    version = 1.3;
                    getLogger().log(Level.INFO, "Loading with MC 1.3.X compatibility.");
                } else {
                    getLogger().log(Level.SEVERE, "Unsupported MC version detected!");

                }
            }

            util = new StormUtil(this);

            biomes = new BiomeGroups();
            db = Database.Obtain(this, null);
            cmds = new Commands(this);
         
            // Make per-world configuration files
            System.out.println(Bukkit.getWorlds());
            for (World w : Bukkit.getWorlds()) {
                String world = w.getName();
                GlobalVariables config = new GlobalVariables(this, world);
                config.load();
                wConfigs.put(w, config);
            }


            getCommand("meteor").setExecutor(cmds);
            getCommand("wildfire").setExecutor(cmds);
            getCommand("acidrain").setExecutor(cmds);
            getCommand("blizzard").setExecutor(cmds);
            getCommand("thunderstorm").setExecutor(cmds);
                    

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
            ThunderStorm.load(this);
           
            pm.registerEvents(new TextureManager(), this);
            pm.registerEvents(new WorldMemoryManager(this), this);
             
        } catch (Exception e) {
            e.printStackTrace();
        }
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