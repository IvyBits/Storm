/*
 * Copyright 2011 Tyler Blair. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 * of conditions and the following disclaimer in the documentation and/or other materials
 * provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */
package com.github.StormTeam.Storm;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.logging.Level;

public class MetricsLite {

    private final static int REVISION = 5;
    private static final String BASE_URL = "http://mcstats.org";
    private static final String REPORT_URL = "/report/%s";
    private final static int PING_INTERVAL = 10;
    private final Plugin plugin;
    private final YamlConfiguration configuration;
    private final File configurationFile;
    private final String guid;
    private final Object optOutLock = new Object();
    private volatile int taskId = -1;

    public MetricsLite(Plugin plugin) throws IOException {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }

        this.plugin = plugin;

        configurationFile = getConfigFile();
        configuration = YamlConfiguration.loadConfiguration(configurationFile);

        configuration.addDefault("opt-out", false);
        configuration.addDefault("guid", UUID.randomUUID().toString());

        if (configuration.get("guid", null) == null) {
            configuration.options().header("http://mcstats.org").copyDefaults(true);
            configuration.save(configurationFile);
        }

        guid = configuration.getString("guid");
    }

    public boolean start() {
        synchronized (optOutLock) {

            if (isOptOut()) {
                return false;
            }

            if (taskId >= 0) {
                return true;
            }

            taskId = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
                private boolean firstPost = true;

                public void run() {
                    try {

                        synchronized (optOutLock) {

                            if (isOptOut() && taskId > 0) {
                                plugin.getServer().getScheduler().cancelTask(taskId);
                                taskId = -1;
                            }
                        }

                        postPlugin(!firstPost);

                        firstPost = false;
                    } catch (IOException e) {
                        Bukkit.getLogger().log(Level.INFO, "[Metrics] " + e.getMessage());
                    }
                }
            }, 0, PING_INTERVAL * 1200);

            return true;
        }
    }

    public boolean isOptOut() {
        synchronized (optOutLock) {
            try {
                // Reload the metrics file
                configuration.load(getConfigFile());
            } catch (IOException ex) {
                Bukkit.getLogger().log(Level.INFO, "[Metrics] {0}", ex.getMessage());
                return true;
            } catch (InvalidConfigurationException ex) {
                Bukkit.getLogger().log(Level.INFO, "[Metrics] {0}", ex.getMessage());
                return true;
            }
            return configuration.getBoolean("opt-out", false);
        }
    }

    public void enable() throws IOException {

        synchronized (optOutLock) {

            if (isOptOut()) {
                configuration.set("opt-out", false);
                configuration.save(configurationFile);
            }

            if (taskId < 0) {
                start();
            }
        }
    }

    public void disable() throws IOException {
        synchronized (optOutLock) {
            if (!isOptOut()) {
                configuration.set("opt-out", true);
                configuration.save(configurationFile);
            }

            if (taskId > 0) {
                this.plugin.getServer().getScheduler().cancelTask(taskId);
                taskId = -1;
            }
        }
    }

    public File getConfigFile() {
        return new File(new File(plugin.getDataFolder().getParentFile(), "PluginMetrics"), "config.yml");
    }

    private void postPlugin(boolean isPing) throws IOException {

        final PluginDescriptionFile description = plugin.getDescription();

        final StringBuilder data = new StringBuilder();
        data.append(encode("guid")).append('=').append(encode(guid));
        encodeDataPair(data, "version", description.getVersion());
        encodeDataPair(data, "server", Bukkit.getVersion());
        encodeDataPair(data, "players", Integer.toString(Bukkit.getServer().getOnlinePlayers().length));
        encodeDataPair(data, "revision", String.valueOf(REVISION));

        if (isPing) {
            encodeDataPair(data, "ping", "true");
        }

        URL url = new URL(BASE_URL + String.format(REPORT_URL, encode(plugin.getDescription().getName())));

        URLConnection connection;

        if (isMineshafterPresent()) {
            connection = url.openConnection(Proxy.NO_PROXY);
        } else {
            connection = url.openConnection();
        }

        connection.setDoOutput(true);

        final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(data.toString());
        writer.flush();

        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        final String response = reader.readLine();

        writer.close();
        reader.close();

        if (response == null || response.startsWith("ERR")) {
            throw new IOException(response);
        }
    }

    private boolean isMineshafterPresent() {
        try {
            Class.forName("mineshafter.MineServer");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void encodeDataPair(final StringBuilder buffer, final String key, final String value) throws UnsupportedEncodingException {
        buffer.append('&').append(encode(key)).append('=').append(encode(value));
    }

    private static String encode(final String text) throws UnsupportedEncodingException {
        return URLEncoder.encode(text, "UTF-8");
    }
}