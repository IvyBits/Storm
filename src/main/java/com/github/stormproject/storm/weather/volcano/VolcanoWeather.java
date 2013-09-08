/*
 * This file is part of Storm.
 *
 * Storm is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Storm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Storm.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.github.stormproject.storm.weather.volcano;

import com.github.stormproject.storm.Storm;
import com.github.stormproject.storm.WorldVariables;
import com.github.stormproject.storm.utility.StormUtil;
import com.github.stormproject.storm.weather.StormWeather;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VolcanoWeather extends StormWeather {
    WorldVariables glob;

    /**
     * Constructor. DO NOT CHANGE ARGUMENTS.
     *
     * @param world World name to act opon
     */
    public VolcanoWeather(String world) {
        super(world);
        glob = Storm.wConfigs.get(world);
        autoKillTicks = 1;
    }

    Location victim;
    int size;
    List<Integer> unsafeBlocks = Arrays.asList(Material.WATER.getId(), Material.STATIONARY_WATER.getId());

    @Override
    public boolean canStart() {
        if (!Storm.wConfigs.get(world).Weathers__Enabled_Natural__Disasters_Volcanoes)
            return false;

        size = (int) Storm.random.gauss(25, 8);
        if (size < 5)
            return false;

        ArrayList<Location> candidate = new ArrayList<Location>();
        for (Chunk chunk : Bukkit.getWorld(world).getLoadedChunks()) {
            Location location = chunk.getBlock(Storm.random.nextInt(16), 64, Storm.random.nextInt(16)).getLocation();
            location = StormUtil.getSurface(location);
            if (!StormUtil.isLocationNearBlock(location, unsafeBlocks, 50))
                candidate.add(location);
        }
        if (candidate.size() == 0)
            return false;
        victim = candidate.get(Storm.random.nextInt(candidate.size()));
        return true;
    }

    @Override
    public void start() {
        StormUtil.broadcast(glob.Natural__Disasters_Volcano_Message__On__Volcano__Start
                .replaceAll("%x", victim.getX() + "")
                .replaceAll("%y", victim.getY() + "")
                .replaceAll("%z", victim.getZ() + ""),
                world);
        try {
            Volcano.volcano(victim, size);
        } catch (Exception e) {
            start(); //Try again!
        }
    }

    @Override
    public void end() {
        // Does nothing really
    }
}
