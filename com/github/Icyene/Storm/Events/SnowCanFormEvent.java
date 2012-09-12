package com.github.Icyene.Storm.Events;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SnowCanFormEvent extends Event
	implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled = false;
    private net.minecraft.server.World world;
    private int x;
    private int y;
    private int z;

    public SnowCanFormEvent(net.minecraft.server.World world2, int x, int y, int z)
    {
	this.world = world2;

	this.x = x;
	this.y = y;
	this.z = z;
    }

    public World getWorld()
    {
	return this.world.getWorld();
    }

    public Location getLocation() {
	return new Location(this.world.getWorld(), this.x, this.y, this.z);
    }

    public HandlerList getHandlers() {
	return handlers;
    }

    public static HandlerList getHandlerList() {
	return handlers;
    }

    public boolean isCancelled()
    {
	return this.isCancelled;
    }

    public void setCancelled(boolean flag)
    {
	this.isCancelled = flag;
    }
}