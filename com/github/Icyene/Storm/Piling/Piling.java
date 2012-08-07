package com.github.Icyene.Storm.Snow.Piling;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Piling {

    final BlockFace[] dirsToCheck = new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    
    //Returns true if the data of the blocks in the given directions is >= the given blocks data.
    public boolean canBuildSnowData(Block currentBlock) {

	for (BlockFace bf : dirsToCheck) {
                    if (currentBlock.getRelative(bf).getData() < currentBlock.getData())
                	return true;  //return true if at least one blockface is greater to or equal
            }
	return false;
    }

    //Returns true if the blocks in the given directions are of the same material and their respective data is >= the given blocks data
    public boolean canBuildSnowMaterial(Block currentBlock) {
	for (BlockFace bf : dirsToCheck) {
                    if (currentBlock.getRelative(bf).getType() == currentBlock.getType()) 
                	return true;
            }

            return false;
    }
    
    public boolean isMaterialInGivenDirectionsAir(Block currentBlock) {
        
	for (BlockFace bf : dirsToCheck) {
                    if (currentBlock.getRelative(bf).getType() == Material.AIR) 
                	return true;
            }

            return false;
    }
    
    public void formSnow(Location toForm) {
	final Block lBlock = toForm.getBlock();
	
	if(canBuildSnowData(lBlock)) { //Makes sure that we dont have a layer of 1 next to a layer of 5
	    
	    if(canBuildSnowMaterial(lBlock) || isMaterialInGivenDirectionsAir(lBlock)) { //checks if blocks around are snow OR air
		
		placeSnow(lBlock);//place snow
		
	    } 
	    
	}
	
    }
    
    public void placeSnow(Block toPlaceAt) {
	
	if(toPlaceAt.getType() == Material.SNOW) {
	    
	    final byte data = toPlaceAt.getData();
	    if(data == (byte)(7)) { //next is a snow BLOCK
		
		toPlaceAt.setType(Material.SNOW_BLOCK);
		
		return;
		
	    }
	    toPlaceAt.setData((byte)(data+1)); //grow level
	    
	    return;
	}
	
	if(toPlaceAt.getType() == Material.AIR) {
	    
	     toPlaceAt.setType(Material.SNOW); //place snow
	    
	    return;
	}
	
	if(toPlaceAt.getType() == Material.SNOW_BLOCK) {
	    
	   toPlaceAt.getRelative(BlockFace.UP).setType(Material.SNOW);
	    
	   return;
	}
	
	
    }
    
}
