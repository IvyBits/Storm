package com.github.Icyene.Storm.Earthquake.Events;

import com.github.Icyene.Storm.Earthquake.Quake;
import com.github.Icyene.Storm.Pair;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author Giant
 */
public class QuakeLoadEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	private Quake q;
	private World w;
	private Pair<Integer, Integer> e;
	private Pair<Integer, Integer> p1;
	private Pair<Integer, Integer> p2;
	
	public QuakeLoadEvent(Quake q) {
		this.q = q;
		this.w = q.getWorld();
		this.e = q.getEpicenter();
		this.p1 = q.getPointOne();
		this.p2 = q.getPointTwo();
	}
	
	public Quake getQuake() {
		return this.q;
	}
	
	public World getWorld() {
		return this.w;
	}
	
	public Pair<Integer, Integer> getEpicenter() {
		return this.e;
	}
	
	public Pair<Integer, Integer> getPointOne() {
		return this.p1;
	}
	
	public Pair<Integer, Integer> getPointTwo() {
		return this.p2;
	}
	
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}
	
	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
