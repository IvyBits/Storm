package com.github.StormTeam.Storm;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.Packet250CustomPayload;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import java.lang.reflect.Field;
import net.minecraft.server.WorldData;
import org.bukkit.craftbukkit.CraftWorld;

public class StormUtil extends BiomeGroups {

    private final Random rand = new Random();
    private WorldGuardPlugin wg;
    private boolean hasWG = false;
    private HashMap<String, BlockTickSelector> blockTickers = new HashMap<String, BlockTickSelector>();
    private Field isRaining;
    private Logger log;

    /**
     * Creates a util object.
     *
     * @param plugin The plugin.
     */
    public StormUtil(Plugin plugin) {

        final Plugin wgp = plugin.getServer().getPluginManager().getPlugin(
                "WorldGuard");
        hasWG = wgp == null ? false : true; // Short and sweet
        if (hasWG) {
            wg = (WorldGuardPlugin) wgp;
        }

        for (World w : Bukkit.getWorlds()) {
            String world = w.getName();
            BlockTickSelector ticker;
            try {
                ticker = new BlockTickSelector(w, 16);
                blockTickers.put(world, ticker);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        try {
            isRaining = WorldData.class.getDeclaredField("isRaining");
            isRaining.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log = plugin.getLogger();

    }

    public void setStormNoEvent(World world, boolean flag) {
        try {
            isRaining.set(((CraftWorld) world).getHandle().worldData, flag);
        } catch (Exception ex) {
            world.setStorm(true); //Can still set the storm
        }
    }

    public void log(String logM) {
        log.log(Level.INFO, logM);
    }

    public void log(Level level, String logM) {
        log.log(level, logM);
    }

    public void broadcast(String message) {
        if (!message.isEmpty()) {
            Bukkit.getServer().broadcastMessage(parseColors(message));
        }
    }

    public void message(Player player, String message) {
        if (!message.isEmpty()) {
            player.sendMessage(parseColors(message));
        }
    }

    public String parseColors(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public void damageNearbyPlayers(Location location, double radius, int damage, String message) {
        for (Player p : getNearbyPlayers(location, radius)) {
            damagePlayer(p, message, damage);
        }
    }

    public void damagePlayer(Player p, String m, int d) {
        if (p.getGameMode() != GameMode.CREATIVE && p.getHealth() != 0) {
            p.damage(d * 2);
            this.message(p, m);
        }
    }

    public boolean isBlockProtected(Block b) {
        return hasWG && wg.getGlobalRegionManager().get(b.getWorld()).getApplicableRegions(BukkitUtil.toVector(b.getLocation())).size() > 0;
    }

    public ArrayList<Player> getNearbyPlayers(Location location, double radius) {
        ArrayList<Player> playerList = new ArrayList<Player>();
        World locWorld = location.getWorld();

        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getWorld().equals(locWorld)) {
                if (p.getLocation().distance(location) <= radius) {
                    playerList.add(p);
                }
            }
        }

        return playerList;
    }

    public void transform(Block toTransform, List<List<String>> transformations) {
        if (isBlockProtected(toTransform)) {
            return;
        }

        for (List<String> toCheck : transformations) {
            ArrayList<String[]> stateIndex = new ArrayList<String[]>();

            for (int i = 0; i != 2; ++i) {
                String got = toCheck.get(i);

                if (got.contains(":")) // Check for data value appended.
                {
                    stateIndex.add(got.split(":"));
                } else {
                    stateIndex.add(new String[]{got, "0"});
                }
            }

            String[] curState = stateIndex.get(0), toState = stateIndex.get(1);

            if (Integer.valueOf(curState[0]) == toTransform.getTypeId()
                    && Integer.valueOf(curState[1]) == toTransform.getData()) {
                toTransform.setTypeIdAndData(Integer.valueOf(toState[0]), Byte
                        .parseByte(toState[1]), true);
                return;
            }

        }

    }

    public Chunk pickChunk(World w) {
        Chunk[] loadedChunks = w.getLoadedChunks();
        return loadedChunks[rand.nextInt(loadedChunks.length)];
    }

    public void setTexture(Player toSetOn, String pathToTexture) {
        if (Storm.version >= 1.3) {
            ((CraftPlayer) toSetOn).getHandle().netServerHandler.sendPacket(new Packet250CustomPayload("MC|TPack", (pathToTexture + "\0" + 16).getBytes()));
        }
    }

    public void clearTexture(Player toClear) {
        setTexture(toClear, Storm.wConfigs.get(toClear.getWorld()).Textures_Default__Texture__Path);
    }

    public boolean isPlayerUnderSky(Player player) {
        Location loc = player.getLocation();
        return player.getWorld().getHighestBlockYAt(loc) <= loc.getBlockY();

    }

    public ArrayList<Block> getRandomTickedBlocks(World world)
            throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {

        return blockTickers.get(world.getName()).getRandomTickedBlocks();
    }

    public boolean isLocationNearBlock(Location loc, List<Integer> blocks, int radius) {
        World world = loc.getWorld();
        int x = (int) loc.getX(), y = (int) loc.getY(), z = (int) loc.getZ();

        for (int ox = 0; ox > -radius; ox--) {
            for (int oz = 0; oz > -radius; oz--) {
                if (blocks.contains(world.getBlockAt(x + ox, y, z + oz).getTypeId())) {
                    return true;
                }
            }
        }
        return false;
    }
}
