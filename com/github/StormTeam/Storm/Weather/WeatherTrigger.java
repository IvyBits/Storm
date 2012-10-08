/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.StormTeam.Storm.Weather;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xiaomao
 */
public class WeatherTrigger implements Runnable {
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
    @Override
    public void run() {
        if (WeatherManager.random.nextInt(100) < chance) {
            try {
                manager.startWeather(weather, world);
            } catch (Exception e) {
                // Should not happen, but still catching just in case
                e.printStackTrace();
            }
        }
    }
    
    WeatherManager manager;
    String weather, world;
    int chance;
}
