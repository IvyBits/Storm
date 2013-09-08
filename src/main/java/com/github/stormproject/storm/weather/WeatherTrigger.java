/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.stormproject.storm.weather;

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.utility.ErrorLogger;

/**
 * @author xiaomao
 */
class WeatherTrigger implements Runnable {
    public WeatherTrigger(WeatherManager manager, String weather, String world, int chance) {
        this.manager = manager;
        this.weather = weather;
        this.world = world;
        this.chance = chance;
    }

    /**
     * Runs a task that triggers the weather in world with percent chance if no
     * conflict weather is running, where weather, world, and chance are as
     * specified in constructor.
     */
    public void run() {
        if (Storm.random.nextInt(100) < chance) {
            try {
                manager.startWeather(weather, world);
            } catch (Exception e) {
                // Should not happen, but still catching just in case
                ErrorLogger.alert(e);
            }
        }
    }

    private final WeatherManager manager;
    private final String weather;
    private final String world;
    private final int chance;
}
