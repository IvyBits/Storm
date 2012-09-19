package com.github.Icyene.Storm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.plugin.Plugin;

import net.minecraft.server.Block;
import net.minecraft.server.Item;

public class GlobalVariables extends ReflectConfiguration {

    public GlobalVariables(Plugin storm, String world) {
	super(storm, world);
    }

    private String configurationWorld;
    
    public void setWorld(String w) {
	configurationWorld = w;
    }
    
    public String getWorld() {
	return configurationWorld;
    }
    
    public void workaroundLists() {

	List<String> leavesToAir = Arrays.asList(new String[] { "18", "0" });
	List<String> grassToDirt = Arrays.asList(new String[] { "2", "3" });
	List<String> stoneToCobble = Arrays.asList(new String[] { "1", "4" });
	List<String> brickSlabToCobbleSlab = Arrays.asList(new String[] {
		"44:5", "44:3" });
	List<String> cobbleToMossy = Arrays.asList(new String[] { "4", "48" });
	
	Acid__Rain_Dissolver_Block__Transformations.add(leavesToAir);
	Acid__Rain_Dissolver_Block__Transformations.add(grassToDirt);
	Acid__Rain_Dissolver_Block__Transformations.add(stoneToCobble);
	Acid__Rain_Dissolver_Block__Transformations.add(brickSlabToCobbleSlab);
	Acid__Rain_Dissolver_Block__Transformations.add(cobbleToMossy);

	List<String> sandToGlass = Arrays.asList(new String[] { "12", "20" });
	Lightning_Melter_Block__Transformations.add(sandToGlass);

    }

     public int Acid__Rain_Acid__Rain__Chance = 5;
    public String Acid__Rain_Message__On__Acid__Rain__Start = "Acid has started to fall from the sky!";

    public String Acid__Rain_Damager_Message__On__Player__Damaged__By__Acid__Rain = "You have been hurt by the acidic downfall!";
    public int Acid__Rain_Player_Damage__From__Exposure = 2;
    public List<List<String>> Acid__Rain_Dissolver_Block__Transformations = new ArrayList<List<String>>();

    public int Acid__Rain_Dissolver_Blocks__To__Deteriorate__Per__Calculation = 10;

    public int Acid__Rain_Dissolver_Block__Deterioration__Chance = 100;

    public int Acid__Rain_Scheduler_Dissolver__Calculation__Intervals__In__Ticks = 100;
    public int Acid__Rain_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks = 200;

    public int Blizzard_Blizzard__Chance = 20;
    public String Blizzard_Message__On__Blizzard__Start = "It has started to snow violently! Seek a warm biome for safety!";
    public String Blizzard_Damager_Message__On__Player__Damaged__Cold = "You are freezing!";
    public int Blizzard_Damager_Blindness__Amplitude = 5;

    public List<Integer> Blizzard_Damager_Heating__Blocks = Arrays.asList(
	    Block.FIRE.id, Block.LAVA.id, Block.STATIONARY_LAVA.id,
	    Block.BURNING_FURNACE.id);

    public int Blizzard_Player_Damage__From__Exposure = 2;
    public double Blizzard_Player_Speed__Loss__While__In__Snow = 0.4D;
    public int Blizzard_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks = 200;

    public int Lightning_Damage_Damage = 5;
    public int Lightning_Damage_Damage__Radius = 10;
    public String Lightning_Damage_Hit__Message = "You were zapped by lightning. Ouch!";

    public int Lightning_Attraction_Blocks_AttractionChance = 80;
    public List<Integer> Lightning_Attraction_Blocks_Attractors = Arrays
	    .asList(new Integer[] { Block.IRON_BLOCK.id });

    public int Lightning_Attraction_Players_AttractionChance = 80;
    public List<Integer> Lightning_Attraction_Players_Attractors = Arrays
	    .asList(new Integer[] { Item.IRON_AXE.id });
    public List<List<String>> Lightning_Melter_Block__Transformations = new ArrayList<List<String>>();

    public double Natural__Disasters_Meteor_Chance__To__Spawn = 8;

    public String Natural__Disasters_Meteor_Message__On__Meteor__Crash = "A meteor has exploded at %x, %y, %z.";
    public int Natural__Disasters_Meteor_Shockwave_Damage = 10;
    public int Natural__Disasters_Meteor_Shockwave_Damage__Radius = 100;
    public String Natural__Disasters_Meteor_Shockwave_Damage__Message = "You have been flattened by a meteor!";
    public long Natural__Disasters_Meteor_Scheduler__Recalculation__Intervals__In__Ticks = 72000;
    public boolean Natural__Disasters_Meteor_Meteor_Spawn = true;
    public int Natural__Disasters_Meteor_Meteor_Radius = 4;
    
    public int Natural__Disasters_Wildfires_Chance__To__Start = 20;
    public int Natural__Disasters_Wildfires_Spread__Limit = 2;
    public int Natural__Disasters_Wildfires_Scan__Radius = 2;
    public String Natural__Disasters_Wildfires_Message__On__Start = "A wildfire has been spotted around %x, %y, %z!";
    public long Natural__Disasters_Wildfires_Scheduler__Recalculation__Intervals__In__Ticks = 72000;
    public int Natural__Disasters_Maximum__Fires = 100;
  
//   public int Natural__Disasters_Earthquakes_Chance__To__Spawn = 1;
//   public String Natural__Disasters_Earthquakes_Message__On__Earthquake__Start = "The ground beneath you begins quaking! Run mortal, run!";
//   public List<Integer> Natural__Disasters_Earthquakes_Blocks__Can__Fall = Arrays.asList(Block.STONE.id, Block.COBBLESTONE.id);
//   public long Natural__Disasters_Earthquake_Scheduler_Recalculation__Intervals__In__Ticks = 72000;
   
    // TPACKS
    public String Textures_Acid__Rain__Texture__Path = "http://dl.dropbox.com/u/67341745/Storm/Acid_Rain.zip";
    public String Textures_Blizzard__Texture__Path = "http://dl.dropbox.com/u/67341745/Storm/Blizzard.zip";
    public String Textures_Default__Texture__Path = "http://dl.dropbox.com/u/67341745/Storm/Default.zip";

    // Features
    public boolean Features_Acid__Rain_Dissolving__Blocks = true;
    public boolean Features_Acid__Rain_Player__Damaging = true;
    public boolean Features_Lightning_Greater__Range__And__Damage = true;
    public boolean Features_Lightning_Player__Attraction = true;
    public boolean Features_Lightning_Block__Attraction = true;
    public boolean Features_Lightning_Block__Transformations = true;
    public boolean Features_Snow_Slow__Players__Down = true;
    public boolean Features_Blizzards_Player__Damaging = true;
    public boolean Features_Blizzards_Slowing__Snow = true;
    public boolean Features_Meteor = true;
    public boolean Features_Wildfires = true;
    public boolean Features_Force__Weather__Textures = true;

};
