package com.github.StormTeam.Storm.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.Semaphore;
import org.bukkit.World;

/**
 *
 * @author Tudor
 */
public class StormAPI {

    private Map<String, StormWeather> registeredWeathers = new HashMap<String, StormWeather>();
    private Map<String, Set<String>> activeWeather = new HashMap<String, Set<String>>();
    private Semaphore mutex = new Semaphore(1);

    public void registerWeather(StormWeather weather, String name) {
        try {
            mutex.acquire();
            registeredWeathers.put(name, weather);
            mutex.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerWeather(Class<? extends StormWeather> weather, String name) {
        try {
            registerWeather(weather.newInstance(), name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Set<String> getActiveWeathers(String world) {
        if (activeWeather.containsKey(world)) {
            return activeWeather.get(world);
        } else {
            return new HashSet<String>();
        }
    }

    public Set<String> getActiveWeathers(World world) {
        return getActiveWeathers(world.getName());
    }

    public boolean isWeatherRegistered(String weather) {
        return registeredWeathers.containsKey(weather);
    }

    public boolean isConflictingWeather(String w1, String w2) {
        if (!isWeatherRegistered(w1) || !isWeatherRegistered(w2))
            return false;
        if (registeredWeathers.get(w1).conflicts.contains(w2) ||
            registeredWeathers.get(w2).conflicts.contains(w1))
                return true;
        return false;
    }
}
