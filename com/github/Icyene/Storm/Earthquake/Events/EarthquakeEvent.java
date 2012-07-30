package com.github.Icyene.Storm.Earthquake.Events;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EarthquakeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled = false;

    private World affectedWorld;
    private double magnitude;
    private double length;

    public EarthquakeEvent(World world, double magnit, double len) {

	this.affectedWorld = world;
	this.magnitude = magnit;
	this.length = len;

    }

    public HandlerList getHandlers() {
	return handlers;
    }

    public static HandlerList getHandlerList() {
	return handlers;
    }

    public World getWorld() {
	return this.affectedWorld;
    }

    public double getMagnitude() {
	return this.magnitude;
    }

    public double getLength() {
	return this.length;
    }

    public boolean isCancelled() {
	return this.isCancelled;
    }

    public void setCancelled(boolean cancelled) {
	this.isCancelled = cancelled;
    }

}
