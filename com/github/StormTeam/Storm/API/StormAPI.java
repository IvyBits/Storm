package com.github.StormTeam.Storm.API;

import com.github.StormTeam.Storm.Pair;
import com.github.StormTeam.Storm.Triplet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import org.bukkit.World;

/**
 *
 * @author Tudor
 */
public class StormAPI {

    private ArrayList<Triplet<String, StormWeather, List<String>>> registeredWeathers = new ArrayList<Triplet<String, StormWeather, List<String>>>();
    private ArrayList<Pair<String, ArrayList<String>>> activeWeather = new ArrayList<Pair<String, ArrayList<String>>>();
    private Semaphore mutex = new Semaphore(1);
    //One ArrayList to hold them. Pairs of World_Name, List<Active_Weather_Names>

    public void registerWeather(Class<? extends StormWeather> weather, String name, List<String> conflicts) {
        try {
            mutex.acquire();
            registeredWeathers.add(new Triplet<String, StormWeather, List<String>>(name, weather.newInstance(), conflicts));
            mutex.release();
        } catch (Exception e) {
            //Throw new RegistrationException
        };
    }

    public ArrayList<String> getActiveWeathers(String world) {
        for (Pair par : activeWeather) {
            if (par.LEFT.equals(world)) {
                return (ArrayList<String>) par.RIGHT;
            }
        }
        return new ArrayList<String>();
    }

    public ArrayList<String> getActiveWeathers(World world) {
        return getActiveWeathers(world.getName());
    }

    public boolean isWeatherRegistered(String weather) {

        for (Triplet par : registeredWeathers) {
            if (par.x.equals(weather)) {
                return true;
            }
        }

        return false;
    }

    public boolean isConflictingWeather(String weather, String conflict) {
        for (Triplet par : registeredWeathers) {
            if (par.x.equals(weather)) {
                return true;
            }
        }

        return false;
    }
}
