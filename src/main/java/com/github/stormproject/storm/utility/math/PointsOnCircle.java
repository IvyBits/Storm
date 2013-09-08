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

package com.github.stormproject.storm.utility.math;

import com.github.stormproject.storm.Storm;
import org.bukkit.util.Vector;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PointsOnCircle implements Iterator<Vector>, Iterable<Vector> {
    private int radius, points = 1, i = 0;
    private double theta = 0, delta = 2 * Math.PI;

    public PointsOnCircle(int radius) {
        if (radius < 0)
            throw new UnsupportedOperationException("Radius can't be 0!");
        this.radius = radius;
        if (radius == 0) {
            return;
        }
        this.points = radius * 8;
        this.delta /= this.points;
    }

    @Override
    public boolean hasNext() {
        return i < points;
    }

    @Override
    public Vector next() {
        if (i >= points)
            throw new NoSuchElementException();
        if (radius == 0) {
            ++i;
            return new Vector(0, 0, 0);
        }
        int x, z;
        x = (int) (radius * Math.cos(theta));
        z = (int) (radius * Math.sin(theta));
        ++i;
        theta += delta;
        return new Vector(x, 0, z);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("You can't remove an element from this iterator!");
    }

    @Override
    public Iterator<Vector> iterator() {
        return this;
    }

    final static double TWOPI = Math.PI * 2;

    public static Vector random() {
        return random(1);
    }

    public static Vector random(double r) {
        double t = TWOPI * Storm.random.nextDouble();
        double u = Storm.random.nextDouble() + Storm.random.nextDouble();
        r *= u > 1 ? 2 - u : u;
        return new Vector(r * Math.cos(t), 0, r * Math.sin(t));
    }
}
