package com.github.Icyene.Storm.Events;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SnowCanFormEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled = false;
   
    private World world;
    private Block block;   

    public SnowCanFormEvent(World world, Block block) {
		this.world = world;
		this.block = block;
	}

    /**
     * Gets the world this event occured in
     * 
     * @return The world
     */
    
    public World getWorld() {
	   return this.world;
    }

    /**
     * Gets the block place that snow is trying to be placed in, generally air
     * 
     * @return The block 
     */
    
    public Block getBlock() {
	   return this.block;
    }  

    public boolean isCancelled() {
	   return this.isCancelled;
    }

    public void setCancelled(boolean flag) {
	   this.isCancelled = flag;
    }  
    
    public HandlerList getHandlers() {
	   return handlers;
    }

    public static HandlerList getHandlerList() {
	   return handlers;
    }
}