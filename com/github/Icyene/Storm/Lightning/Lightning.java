package com.github.Icyene.Storm.Lightning;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.Block;

import org.bukkit.plugin.PluginManager;

import com.github.Icyene.Storm.Storm;
import com.github.Icyene.Storm.Lightning.Listeners.StrikeListener;

public class Lightning {

    public static int strikeDamage = 5;
    public static int strikeRadius = 10;
    public static String strikeMessage = "You were zapped by lightning. Ouch!";

    public boolean powerRedstone = true;
    public int redstoneTicks = 20;

    public int attractionChance = 100;

    public static List<Integer> attractors = new ArrayList<Integer>();

    public static void load(Storm storm)
    {
	PluginManager pm = storm.getServer().getPluginManager();
	pm.registerEvents(new StrikeListener(storm), storm);
	LightningUtils.targetBlocks.add(Block.IRON_BLOCK.id);
	LightningUtils.targetBlocks.add(Block.DIAMOND_BLOCK.id);
	LightningUtils.targetBlocks.add(Block.GOLD_BLOCK.id);
	LightningUtils.targetBlocks.add(Block.DIODE_ON.id);

    }

}
