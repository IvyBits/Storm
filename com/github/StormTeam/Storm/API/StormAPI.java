package com.github.StormTeam.Storm.API;

import com.github.StormTeam.Storm.Pair;
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

    private Map<String, Pair<Class<? extends StormWeather>, Map<String, StormWeather>>> registeredWeathers = new HashMap<String, Pair<Class<? extends StormWeather>, Map<String, StormWeather>>>();
    private Map<String, Set<String>> activeWeather = new HashMap<String, Set<String>>();
    private Semaphore mutex = new Semaphore(1);

    public void registerWeather(Class<? extends StormWeather> weather, String name, Iterable<String> allowedWorlds) {
        try {
            mutex.acquire();
            Map<String, StormWeather> instances = new HashMap<String, StormWeather>();
            for (String world : allowedWorlds) {
                instances.put(world, weather.newInstance());
            }
            registeredWeathers.put(name, new Pair<Class<? extends StormWeather>, Map<String, StormWeather>>(weather, instances));
            mutex.release();
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
        try {
            if (!isWeatherRegistered(w1) || !isWeatherRegistered(w2)) {
                return false;
            }
            if ((Boolean)registeredWeathers.get(w1).LEFT.getDeclaredMethod("conflicts").invoke(w2)
                    || (Boolean)registeredWeathers.get(w2).LEFT.getDeclaredMethod("conflicts").invoke(w1)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
