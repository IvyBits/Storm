/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.StormTeam.Storm.API;

import com.github.StormTeam.Storm.Pair;
import com.github.StormTeam.Storm.Triplet;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Tudor
 */
public class StormAPI {

    private LinkedList<Triplet<String, StormWeather, List<String>>> registeredWeathers = new LinkedList<Triplet<String, StormWeather, List<String>>>();

    public void registerWeather(Class<? extends StormWeather> weather, String name, List<String> conflicts) throws InstantiationException, IllegalAccessException {
        registeredWeathers.add(new Triplet<String, StormWeather, List<String>>(name, weather.newInstance(), conflicts));
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
