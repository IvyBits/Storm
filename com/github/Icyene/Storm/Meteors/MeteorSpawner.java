package com.github.Icyene.Storm.Meteors;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.Server;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.Icyene.Storm.Meteors.Entities.CustomFireball;




public class MeteorSpawner extends JavaPlugin {
    Logger log = Logger.getLogger("Minecraft");

    public static Server server;
    public int distance = 50;
    public int radius = 10;
    public int spawnChance = 8;
    public int spawnDelay = 1200;
    public int yield = 5;
    // Materials left behind when meteor explodes. Left is the IDs, right is
    // chance to spawn
    public int[][] explosionMaterials = { { 49, 73, 14, 15, 56 },
	    { 200, 48, 31, 64, 15 } };
    // How far away from the explosion radius ores will be left behind
    public int oreThreshold = 2;

    
    public static void makeMeteor(Player player) {
	
    }
   

}