/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tk.ivybits.storm.weather.exc;

/**
 * @author xiaomao
 */
public class WeatherNotFoundException extends Exception {
    public WeatherNotFoundException(String message) {
        super(message);
    }
}
