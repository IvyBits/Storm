package com.github.Icyene.Storm.Lightning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

import com.github.Icyene.Storm.GlobalVariables;
import com.github.Icyene.Storm.Storm;

public class LightningUtils {

    final Random rand = new Random();
    Storm storm;
    
    public LightningUtils(Storm storm) {
    	this.storm = storm;
    }
    
    public Location hitMetal(Location oldLoc){
    	Chunk chunk = pickChunk(oldLoc.getWorld());
    	if(chunk != null){
    		Location loc = pickLightningRod(chunk);
    		if(loc != null){
    			return loc;
    		}
    	}
    	return oldLoc;
    }
    
    public void damageNearbyPlayers(Location location, double radius) {

	ArrayList<Player> damagees = getNearbyPlayers(location, radius);
	
	if (Storm.debug);
	System.out.println(damagees.toString());

	for (Player p : damagees) {

	    if (p.getGameMode() != GameMode.CREATIVE) {
			if (Storm.debug);
			System.out.println("Damaging " + p.getName());		
			p.damage((p.getHealth() - GlobalVariables.lightning_damage_strikeDamage));
			p.sendMessage(ChatColor.GRAY + GlobalVariables.lightning_damage_strikeMessage);
	    }
	}
    }

    public ArrayList<Player> getNearbyPlayers(Location location, double radius) {

	ArrayList<Player> playerList = new ArrayList<Player>();

	for (Player p : storm.getServer().getOnlinePlayers()) {
	    Location ploc = p.getLocation();
	    ploc.setY(location.getY());
	    if (ploc.distance(location) <= radius) {
		playerList.add(p);
	    }
	}

	return playerList;
    }
   
    public Location pickLightningRod(Chunk chunk)
    {
      ChunkSnapshot snapshot = chunk.getChunkSnapshot(true, false, false);
      List<Location> list = findLightningRods(chunk);
      if ((list != null) && (!list.isEmpty())) {
    	  Location tmp = list.get(rand.nextInt(list.size()));
          return tmp;
      }
      Entity[] entities = chunk.getEntities();
      if (entities != null){
        for (Entity entity : entities)
        {
          Location loc = entity.getLocation();
          int y = snapshot.getHighestBlockYAt(loc.getBlockX() & 0xF, 
            loc.getBlockZ() & 0xF);
          if (loc.getBlockY() < y - 1)
          {
            continue;
          }

          if (entity instanceof Minecart){
            return loc;
          }
        }
      }

      return null;
    }
    
    public List<Location> findLightningRods(Chunk chunk)
    {
      ArrayList<Location> list = new ArrayList<Location>();

      ChunkSnapshot snapshot = chunk.getChunkSnapshot(true, false, false);

      for (int x = 0; x < 16; x++)
      {
        for (int z = 0; z < 16; z++)
        {
          int y = snapshot.getHighestBlockYAt(x, z);
          int type = snapshot.getBlockTypeId(x, y, z);

          if(Arrays.asList(GlobalVariables.lightning_attraction_blocks_attractors).contains(type)) {
            list.add(chunk.getBlock(x, y, z).getLocation()); } else {
            if (y <= 0) {
              continue;
            }
            y--; 
            type = snapshot.getBlockTypeId(x, y, z);
            if(Arrays.asList(GlobalVariables.lightning_attraction_blocks_attractors).contains(type)) {
              list.add(chunk.getBlock(x, y, z).getLocation());
            }
          }
        }

      }

      return list;
    }
    
    public Chunk pickChunk(World world)
    {
      List<Player> players = world.getPlayers();
      if ((players == null) || (players.isEmpty()))
        return null;
      Player player = players.get(rand.nextInt(players.size()));

      List<Block> blocks = player.getLastTwoTargetBlocks(null, 100);
      if ((blocks != null) && (rand.nextInt(100) < 30)) {
        return world.getChunkAt(
          (blocks.get(rand.nextInt(blocks.size()))).getLocation());
      }
      return world.getChunkAt(player.getLocation());
    }
    
}
