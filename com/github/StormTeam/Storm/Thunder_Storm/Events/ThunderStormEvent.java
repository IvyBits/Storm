package com.github.StormTeam.Storm.Thunder_Storm.Events;

/**
 *
 * @author Tudor
 */

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ThunderStormEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled = false;

    private World affectedWorld;
    private boolean weatherState = false;

    public ThunderStormEvent(World world, boolean state) {
	this.affectedWorld = world;
	this.weatherState = state;

    }

    public HandlerList getHandlers() {
	return handlers;
    }

    public static HandlerList getHandlerList() {
	return handlers;
    }

    @Override
    public boolean isCancelled() {
	return isCancelled;
    }

    @Override
    public void setCancelled(boolean flag) {
	this.isCancelled = flag;
	
    }
    
    public World getAffectedWorld() {
	return this.affectedWorld;
    }

    public boolean getWeatherState() {
	return this.weatherState;
    }

}