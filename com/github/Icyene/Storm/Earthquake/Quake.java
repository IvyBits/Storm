package com.github.Icyene.Storm.Earthquake;

import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.github.Icyene.Storm.Pair;
import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Earthquake.Exceptions.InvalidWorldException;
import com.github.Icyene.Storm.Earthquake.Listeners.BlockListener;
import com.github.Icyene.Storm.Earthquake.Listeners.MobListener;

import java.util.List;
import java.util.logging.Level;

public class Quake {

	private Storm storm;
	
	private Integer quakeID;
	
	private String world;
	private Pair<Integer, Integer> point1;
	private Pair<Integer, Integer> point2;
	private Pair<Integer, Integer> epicenter;
	
	private MobListener mL;
	private BlockListener bL;
	
	private Integer tID;
	private Boolean isLoading = false;
	private Boolean isRunning = false;
	
	private void load() {
		this.isLoading = true;
		
		World w = storm.getServer().getWorld(world);
		int x = (point1.LEFT + point2.LEFT) / 2;
		int z = (point1.RIGHT + point2.RIGHT) / 2;
		this.epicenter = new Pair<Integer, Integer>(x, z);
		
		// Calculate blocks
		Chunk c = w.getChunkAt(x, z);
		
		
		
		// Creepers will not attack player during quake!
		mL = new MobListener(this);
		
		// Register events
		storm.getServer().getPluginManager().registerEvents(mL, storm);
		
		tID = storm.getServer().getScheduler().scheduleSyncDelayedTask(storm, new Runnable() {
			
			@Override
			public void run() {
				start();
			}
			
		}, 20L);//18000L);
	}
	
	private void go() {
		// Blocks will bounce everywhere in the quake!
		bL = new BlockListener(this, storm);
		storm.getServer().getPluginManager().registerEvents(bL, storm);
		
		storm.getLogger().log(Level.SEVERE, "Quake started at: [" + this.point1.LEFT + " - " + this.point1.RIGHT + "] - [" + this.point2.LEFT + " - " + this.point2.RIGHT + "]");
		
		tID = storm.getServer().getScheduler().scheduleSyncRepeatingTask(storm, new Runnable() {
			
			int i = 0;
			
			@Override
			public void run() {
				List<Player> players = storm.getServer().getWorld(world).getPlayers();
				for(Player p : players) {
					// Don't bother creative players
					if(p.getGameMode() == GameMode.CREATIVE)
						continue;
					
					// Player isn't quaking...
					if(!isQuaking(p.getLocation()))
						continue;
					
					if(i == 0) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2, 5), true);
					}else{
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, 5), true);
					}
				}
			}
			
		}, 0L, 2L);
	}
	
	public Quake(Storm storm, Integer qID, Location point1, Location point2) throws InvalidWorldException {
		this.storm = storm;
		this.quakeID = qID;
		
		String w = point1.getWorld().getName();
		String w2 = point2.getWorld().getName();
		if(w.equals(w2)) {
			int minX = Math.min(point1.getBlockX(), point2.getBlockX());
			int minZ = Math.min(point1.getBlockZ(), point2.getBlockZ());
			int maxX = Math.max(point1.getBlockX(), point2.getBlockX());
			int maxZ = Math.max(point1.getBlockZ(), point2.getBlockZ());
			this.world = w;
			this.point1 = new Pair<Integer, Integer>(minX, minZ);
			this.point2 = new Pair<Integer, Integer>(maxX, maxZ);
			
			this.load();
			storm.getLogger().log(Level.SEVERE, "Quake loading at: [" + this.point1.LEFT + " - " + this.point1.RIGHT + "] - [" + this.point2.LEFT + " - " + this.point2.RIGHT + "]");
		}else{
			throw new InvalidWorldException("World " + w + " and World " + w2 + " do not match!");
		}
	}
	
	public void start() {
		this.isLoading = false;
		this.isRunning = true;
		this.go();
	}
	
	public void stop() {
		this.isLoading = false;
		this.isRunning = false;
		if(null != mL) {
			mL.forget();
		}
		
		if(null != bL) {
			bL.forget();
		}
		
		if(tID != null) {
			storm.getServer().getScheduler().cancelTask(tID);
		}
	}
	
	public Boolean isRunning() {
		return this.isRunning;
	}
	
	public Boolean isLoading() {
		return this.isLoading;
	}
	
	public Boolean isQuaking(Location point) {
		if(!point.getWorld().getName().equals(this.world))
			return false;
		
		/*storm.getLogger().severe("========= DEBUG ========");
		storm.getLogger().severe("loc x: " + point.getBlockX());
		storm.getLogger().severe("loc z: " + point.getBlockZ());
		storm.getLogger().severe("quake min x: " + point1.LEFT);
		storm.getLogger().severe("quake min z: " + point1.RIGHT);
		storm.getLogger().severe("quake max x: " + point2.LEFT);
		storm.getLogger().severe("quake max z: " + point2.RIGHT);*/
		
		
		return (point.getBlockX() >= point1.LEFT && point.getBlockZ() >= point1.RIGHT
				&& point.getBlockX() <= point2.LEFT && point.getBlockZ() <= point2.RIGHT);
	}
}
