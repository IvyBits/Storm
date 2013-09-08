package com.github.stormproject.storm.weather;

import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event for StormWeather starting/ending.
 */

class WeatherEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    private final World affectedWorld;
    private final boolean weatherState;
    private final String weatherName;

    /**
     * Constructs a WeatherEvent.
     *
     * @param world   The World involved in the event
     * @param state   The state of the weather; true = weather turned on, false = turned off
     * @param weather The name of the weather
     */
    public WeatherEvent(World world, boolean state, String weather) {
        this.affectedWorld = world;
        this.weatherState = state;
        this.weatherName = weather;
    }

    /**
     * The handler list for the event.
     *
     * @return Returns the handler list for this event
     */
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Returns whether or not the event has been cancelled.
     *
     * @return A boolean; true = has been cancelled, false = hasn't been cancelled
     */
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Sets the cancelled flag.
     *
     * @param flag The flag for whether the event has been cancelled or not
     */
    @Override
    public void setCancelled(boolean flag) {
        this.isCancelled = flag;

    }

    /**
     * Returns the affected world.
     *
     * @return A World object of the affected world
     */
    public World getAffectedWorld() {
        return this.affectedWorld;
    }

    /**
     * Gets the name of the weather involved in the event.
     *
     * @return The registered name of the weather involved in the event
     */
    public String getWeather() {
        return this.weatherName;
    }

    /**
     * Gets the weather state this event handled.
     *
     * @return True = weather set to on; false = weather set to off
     */
    public boolean getWeatherState() {
        return this.weatherState;
    }

}