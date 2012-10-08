package com.github.StormTeam.Storm.Weather;

import com.github.StormTeam.Storm.Pair;
import com.github.StormTeam.Storm.Storm;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Weather Manager for Storm. All members thread-safe unless documented.
 *
 * @author Tudor
 * @author xiaomao
 */
public class WeatherManager implements Listener {

    private Map<String, Pair<Class<? extends StormWeather>, Map<String, StormWeather>>> registeredWeathers = new HashMap<String, Pair<Class<? extends StormWeather>, Map<String, StormWeather>>>();
    private Map<String, Set<String>> activeWeather = new HashMap<String, Set<String>>();
    private Map<String, Map<String, Pair<Integer, WeatherTrigger>>> weatherTriggers = new HashMap<String, Map<String, Pair<Integer, WeatherTrigger>>>();
    private Storm storm;
    public static Random random = new Random();
    private Map<String, String> worldTextures = new HashMap<String, String>();

    public WeatherManager(Storm storm) {
        this.storm = storm;
    }

    /**
     * Registers a weather. allowedWorlds is any Iterable<String> that contains
     * the names of the allowed worlds.
     *
     * @param weather Weather clas
     * @param name Weather name
     * @param allowedWorlds Allowed worlds
     * @throws WeatherAlreadyRegisteredException
     */
    public void registerWeather(Class<? extends StormWeather> weather, String name, Iterable<String> allowedWorlds, int chance, int recalculationTicks) throws WeatherAlreadyRegisteredException {
        synchronized (this) {
            if (registeredWeathers.containsKey(name)) {
                throw new WeatherAlreadyRegisteredException(String.format("Weather %s is already registered", name));
            }
            try {
                Map<String, StormWeather> instances = new HashMap<String, StormWeather>();
                Map<String, Pair<Integer, WeatherTrigger>> triggers = new HashMap<String, Pair<Integer, WeatherTrigger>>();
                for (String world : allowedWorlds) {
                    instances.put(world, weather.getConstructor(Storm.class, String.class).newInstance(storm, world));
                    WeatherTrigger trigger = new WeatherTrigger(this, name, world, chance);
                    int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(storm, trigger, recalculationTicks, recalculationTicks);
                    triggers.put(world, new Pair<Integer, WeatherTrigger>(id, trigger));
                }
                weatherTriggers.put(name, triggers);
                registeredWeathers.put(name, new Pair<Class<? extends StormWeather>, Map<String, StormWeather>>(weather, instances));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets all active weathers on world.
     *
     * @param world world name as String
     * @return A newly constructed Set<String> containing the active weathers
     */
    public Set<String> getActiveWeathers(String world) {
        synchronized (this) {
            return Collections.unmodifiableSet(getActiveWeathersReal(world));
        }
    }

    /**
     * A getter function for data member activeWeather, with on demand
     * construction.
     *
     * @param world World name
     * @return A Set<String> containing the active weathers.
     */
    protected Set<String> getActiveWeathersReal(String world) {
        if (!activeWeather.containsKey(world)) {
            activeWeather.put(world, new HashSet<String>());
        }
        return activeWeather.get(world);
    }

    /**
     * Gets all active weathers on world.
     *
     * @param world world object
     * @return A newly constructed Set<String> containing the active weathers
     */
    public Set<String> getActiveWeathers(World world) {
        return getActiveWeathers(world.getName());
    }

    /**
     * Determines if a weather is registered.
     *
     * @param weather weather name
     * @return whether the weather is registered
     */
    public boolean isWeatherRegistered(String weather) {
        synchronized (this) {
            return registeredWeathers.containsKey(weather);
        }
    }

    /**
     * Determines if a weather conflicts with another. If one weather reports
     * itself to be conflicting with another, they two are considered
     * conflicting.
     *
     * @param w1 weather #1
     * @param w2 weather #2
     * @return whether the two are conflicting
     */
    public boolean isConflictingWeather(String w1, String w2) {
        synchronized (this) {
            try {
                if (!isWeatherRegistered(w1) || !isWeatherRegistered(w2)) {
                    return false;
                }
                if ((Boolean) registeredWeathers.get(w1).LEFT.getDeclaredMethod("conflicts").invoke(null, w2)
                        || (Boolean) registeredWeathers.get(w2).LEFT.getDeclaredMethod("conflicts").invoke(null, w1)) {
                    return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * Starts a weather on a single world with conflict checking.
     *
     * @param name weather name
     * @param world world name
     * @return whether the weather is actually started
     * @throws WeatherNotFoundException
     * @throws WeatherNotAllowedException
     */
    public boolean startWeather(String name, String world) throws WeatherNotFoundException, WeatherNotAllowedException {
        Set<String> started = startWeather(name, Arrays.asList(world));
        return started.isEmpty();
    }

    /**
     * Starts a world on a collection of worlds with conflict checking.
     *
     * @param name Weather name
     * @param worlds_ Collection of worlds
     * @return A set of world the weather is actually started on
     * @throws WeatherNotFoundException
     * @throws WeatherNotAllowedException
     */
    public Set<String> startWeather(String name, Collection<String> worlds_) throws WeatherNotFoundException, WeatherNotAllowedException {
        synchronized (this) {
            Set<String> worlds = new HashSet<String>(worlds_);
            for (String world : worlds) {
                for (String weather : getActiveWeathersReal(world)) {
                    if (isConflictingWeather(name, weather)) {
                        worlds.remove(world);
                        break;
                    }
                }
            }
            startWeatherReal(name, worlds);
            return worlds;
        }
    }

    /**
     * Starts a weather on a single world *without* conflict checking.
     *
     * @param name weather name
     * @param world world name
     * @throws WeatherNotFoundException
     * @throws WeatherNotAllowedException
     */
    public void startWeatherForce(String name, String world) throws WeatherNotFoundException, WeatherNotAllowedException {
        synchronized (this) {
            startWeatherReal(name, Arrays.asList(world));
        }
    }

    /**
     * Starts a world on a collection of worlds *without* conflict checking.
     *
     * @param name Weather name
     * @param worlds_ Collection of worlds
     * @throws WeatherNotFoundException
     * @throws WeatherNotAllowedException
     */
    public void startWeatherForce(String name, Collection<String> worlds) throws WeatherNotFoundException, WeatherNotAllowedException {
        synchronized (this) {
            startWeatherReal(name, worlds);
        }
    }

    /**
     * Starts a weather without locking or conflict checking.
     *
     * @param name Weather name
     * @param worlds Collection of worlds
     * @throws WeatherNotFoundException
     * @throws WeatherNotAllowedException
     */
    protected void startWeatherReal(String name, Collection<String> worlds) throws WeatherNotFoundException, WeatherNotAllowedException {
        Pair<Class<? extends StormWeather>, Map<String, StormWeather>> weatherData = registeredWeathers.get(name);
        if (weatherData == null) {
            throw new WeatherNotFoundException(String.format("Weather %s not found", name));
        }
        for (String world : worlds) {
            StormWeather weather = weatherData.RIGHT.get(world);
            if (weather == null) {
                throw new WeatherNotAllowedException(String.format("Weather %s not allowed in %s", name, world));
            }
            if (!getActiveWeathersReal(world).contains(name)) {
                weather.start();
                String texture = weather.getTexture();
                if (texture != null) {
                    for (Player player : Bukkit.getWorld(world).getPlayers()) {
                        Storm.util.setTexture(player, texture);
                    }
                    worldTextures.put(world, texture);
                }
                getActiveWeathersReal(world).add(name);
            }
        }
    }

    /**
     * Stops a weather on a single world.
     *
     * @param name weather name
     * @param world world name
     * @throws WeatherNotFoundException
     */
    public void stopWeather(String name, String world) throws WeatherNotFoundException {
        synchronized (this) {
            stopWeatherReal(name, Arrays.asList(world));
        }
    }

    /**
     * Stops a world on a collection of worlds.
     *
     * @param name Weather name
     * @param worlds_ Collection of worlds
     * @throws WeatherNotFoundException
     */
    public void stopWeather(String name, Collection<String> worlds) throws WeatherNotFoundException {
        synchronized (this) {
            stopWeather(name, worlds);
        }
    }

    protected void stopWeatherReal(String name, Collection<String> worlds) throws WeatherNotFoundException {
        Pair<Class<? extends StormWeather>, Map<String, StormWeather>> weatherData = registeredWeathers.get(name);
        if (weatherData == null) {
            throw new WeatherNotFoundException(String.format("Weather %s not found", name));
        }
        for (String world : worlds) {
            StormWeather weather = weatherData.RIGHT.get(world);
            if (weather == null) {
                continue;
            }
            if (getActiveWeathersReal(world).contains(name)) {
                weather.end();
                String texture = weather.getTexture();
                if (texture != null) {
                    for (Player player : Bukkit.getWorld(world).getPlayers()) {
                        Storm.util.clearTexture(player);
                    }
                    worldTextures.put(world, null);
                }
                getActiveWeathersReal(world).remove(name);
            }
        }
    }
    
    /**
     * Event Handler to set appropriate texture for world when player switched
     * worlds.
     * 
     * @param e Event object
     */
    @EventHandler
    public void worldEvent(PlayerChangedWorldEvent e) {
        final Player hopper = e.getPlayer();
        final World target = hopper.getWorld();
        final World source = e.getFrom();
        
        if (target.equals(source)) {
            return;
        }
        
        final String texture = worldTextures.get(target.getName());
        if (texture == null)
            Storm.util.clearTexture(hopper);
        else
            Storm.util.setTexture(hopper, texture);
    }
    
    /**
     * Event Handler to set appropriate texture for the weather in the world
     * the player just logged on to.
     * 
     * @param e Event object
     */
    @EventHandler
    public void loginEvent(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        final World world = player.getWorld();
        final String texture = worldTextures.get(world.getName());
        if (texture != null)
            Storm.util.setTexture(player, texture);
    }
}
