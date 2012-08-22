package com.github.Icyene.Storm.Whirlwinds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;

public class PlotPoints {

    public void plotPoints(int cx, int cy, int cz, int r) {

    }

    public static void smoke(Location spawnLocation, int data, int radius) {
	spawnLocation.getWorld().playEffect(spawnLocation, Effect.SMOKE, data,
		radius);
    }

    public static List<Triple<Double, Double, Double>> pointOnCircle(
	    double radius,
	    int cx, int cy, int cz, int layers, double rIncrement,
	    double yIncrement, double divisor)
    {

	List<Triple<Double, Double, Double>> plotValues = new ArrayList<Triple<Double, Double, Double>>();
	double rx = 0;
	double rz = 0;
	double ry = cy;

	double rIX = 0;
	double rIZ = 0;

	for (int l = 0; l <= layers; ++l) {
	    for (double i = 0.0; i < 2 * Math.PI; i += Math.PI / divisor) {
		rx = (radius * Math.cos(i))
			+ cx;
		rz = (radius * Math.sin(i))
			+ cz;
		ry = ry + yIncrement;

		System.out.println("[" + (rx + rIX) + ", " + ry + ", "
			+ (rz + rIZ) + "]");

		plotValues.add(new Triple<Double, Double, Double>(rx + rIX, ry,
			rz + rIZ));

	    }
	    rIX = rIX + rIncrement;
	    rIZ = rIZ + rIncrement;

	}

	return plotValues;
    }
}
