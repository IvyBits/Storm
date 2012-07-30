package com.github.Icyene.Storm.Meteors.Events;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FireballMoveEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled = false;

    private Fireball fireball;
    private Location from;
    private Location to;

    public FireballMoveEvent(Fireball fireball, Location from, Location to) {
	this.fireball = fireball;
	this.from = from;
	this.to = to;
    }

    public HandlerList getHandlers() {
	return handlers;
    }

    public static HandlerList getHandlerList() {
	return handlers;
    }

    public Fireball getFireball() {
	return this.fireball;
    }

    public Location getTo() {
	return this.to;
    }

    public Location getFrom() {
	return this.from;
    }

    public boolean isCancelled() {
	return this.isCancelled;
    }

    public void setCancelled(boolean cancelled) {
	this.isCancelled = cancelled;
    }
}