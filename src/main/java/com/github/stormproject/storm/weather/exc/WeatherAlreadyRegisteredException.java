/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.stormproject.storm.weather.exc;

/**
 * @author xiaomao
 */
public class WeatherAlreadyRegisteredException extends Exception {
    public WeatherAlreadyRegisteredException(String message) {
        super(message);
    }
}
