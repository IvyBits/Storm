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

/*
 * This file is part of storm.
 *
 * storm is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * storm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with storm.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

/*
 * This file is part of storm.
 *
 * storm is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * storm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with storm.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.github.stormproject.storm.utility;

import com.google.common.io.Files;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Stack;
import java.util.logging.Level;

/**
 * A class for saving/loading lazy configuration classes/files.
 * <p/>
 * Based on codename_B's non static config 'offering' :)
 * <p/>
 * Inspired by md_5's easy config
 * <p/>
 * <p/>
 * <p/>
 * Node commenting by Xiaomao, fixed by Icyene
 * <p/>
 * Integer limiting by Icynene
 */

public class ReflectConfiguration {

    private final Plugin plugin;
    private final String name;
    private final String prefix;
    private final String header;
    private final String folder;
    private final Object mutex = new Object();


    /**
     * Creates a ReflectConfiguration file based on given name
     *
     * @param plugin The plugin
     * @param name   The name of the file to write to
     */

    public ReflectConfiguration(Plugin plugin, String name) {
        this(plugin, name, null);
    }

    public ReflectConfiguration(Plugin plugin, String name, String folder) {
        this.plugin = plugin;
        this.name = name;
        this.prefix = plugin.getName();
        this.header = prefix + " configuration file: '" + name + "'.\nGenerated for " + prefix + " version " + plugin.getDescription().getVersion() + ".";
        this.folder = folder;
    }

    /**
     * Loads the object.
     */

    public void load() {
        try {
            synchronized (mutex) {
                plugin.getLogger().log(Level.INFO, "Loading configuration file: " + name);
                onLoad(plugin);
            }
        } catch (Exception e) {
            ErrorLogger.alert(e);
        }

    }

    private void onLoad(Plugin plugin) throws Exception {
        synchronized (mutex) {
            File fold;
            if (folder != null)
                fold = new File(plugin.getDataFolder() + folder.replace(".", File.separator));
            else fold = plugin.getDataFolder();

            if (!fold.exists()) {
                fold.mkdir();
            }
            File worldFile = new File(fold.getAbsoluteFile(), File.separator + name + ".yml");
            YamlConfiguration worlds = YamlConfiguration.loadConfiguration(worldFile);
            ((FileConfiguration) worlds).options().header(header);

            for (Field field : getClass().getDeclaredFields()) {
                if (doSkip(field))
                    continue;
                String path = prefix + "." + field.getName().replaceAll("__", " ").replaceAll("_", ".");
                if (worlds.isSet(path)) {
                    LimitInteger lim;
                    if ((lim = field.getAnnotation(LimitInteger.class)) != null) {
                        int limit = lim.limit();
                        boolean doCorrect = false;
                        try {
                            if (worlds.getInt(path) > limit) {
                                doCorrect = true;
                            }
                        } catch (Exception e) {
                            doCorrect = true;
                        }
                        if (doCorrect) {
                            plugin.getLogger().log(Level.SEVERE, lim.warning().replace("%node", "'" + path.substring(6) + "'").replace("%limit", limit + ""));
                            if (lim.correct())
                                worlds.set(path, lim.limit());
                        }
                    }
                    field.set(this, worlds.get(path));
                } else
                    worlds.set(path, field.get(this));
            }

            worlds.save(worldFile);
            Files.write(StringUtils.join(addComments(Files.toString(worldFile, Charset.defaultCharset()).split("\n")), "\n"), worldFile, Charset.defaultCharset());
        }
    }

    Collection<String> addComments(String[] lines) {
        int prevlevel = 0;
        final int indent = 2;
        LinkedList<String> outlines = new LinkedList<String>();
        Stack<String> hierarchy = new Stack<String>();
        for (String line : lines) {
            String content = StringUtils.stripStart(line, " ");
            int spaces = line.length() - content.length(),
                    level = spaces / indent + 1;
            String[] tokens = content.split(":", 2);
            String name = tokens[0];
            if (level <= prevlevel) {
                for (int i = 0; i <= prevlevel - level; ++i) {
                    hierarchy.pop();
                }
            }
            hierarchy.push(name);
            prevlevel = level;
            String id = StringUtils.join(hierarchy, "_").replaceAll(" ", "__").replaceAll(prefix + ".", "");
            boolean modified = false;

            Comment comment = null;
            try {
                comment = getClass().getDeclaredField(id).getAnnotation(Comment.class);
            } catch (NoSuchFieldException ignored) {
            }
            if (comment != null) {
                String indentPrefix = StringUtils.repeat(" ", spaces);
                if (comment.location() == Comment.CommentLocation.TOP)
                    for (String data : comment.value())
                        outlines.add(indentPrefix + "# " + data);

                if (comment.location() == Comment.CommentLocation.INLINE) {
                    String[] comments = comment.value();
                    outlines.add(line + " # " + comments[0]);
                    for (int i = 1; i < comments.length; ++i)
                        outlines.add(StringUtils.repeat(" ", line.length() + 1) + "# " + comments[i]);
                } else
                    outlines.add(line);

                if (comment.location() == Comment.CommentLocation.BOTTOM)
                    for (String data : comment.value())
                        outlines.add(indentPrefix + "# " + data);

                modified = true;

            }
            if (!modified)
                outlines.add(line);
        }
        return outlines;
    }

    public boolean doSkip(Field field) {
        int mod = field.getModifiers();
        return Modifier.isFinal(mod) || Modifier.isTransient(mod) || Modifier.isVolatile(mod);
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface LimitInteger {
        int limit() default 100;

        String warning() default "%node cannot be over %limit! Defaulted to value of %limit.";

        boolean correct() default true;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Comment {
        String[] value();

        CommentLocation location() default CommentLocation.TOP;

        public enum CommentLocation {
            INLINE(1),
            TOP(2),
            BOTTOM(3);

            CommentLocation(int location) {
            }
        }
    }
}
