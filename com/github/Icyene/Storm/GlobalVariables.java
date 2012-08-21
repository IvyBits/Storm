package com.github.Icyene.Storm;

import net.minecraft.server.Block;
import net.minecraft.server.Item;

public class GlobalVariables {

    public static transient String Version = "0.0.3";
    public static String[] Rain_Acid_Allowed__Worlds = new String[] { "world" };
    public static int Rain_Acid_Acid__Rain__Chance = 100;
    public static String Rain_Acid_Message__On__Acid__Rain__Start = "Acid has started to fall from the sky!";

    public static String Rain_Acid_Damager_Message__On__Player__Damaged__By__Acid__Rain = "You have been hurt by the acidic downfall!";
    public static int Rain_Acid_Player_Damage__From__Exposure = 2;
    public static Integer[][] Rain_Acid_Dissolver_Block__Transformations = new Integer[][] {
	    { 18, 0 }, { 2, 3 }, { 1, 4 } };

    public static int Rain_Acid_Dissolver_Blocks__To__Deteriorate__Per__Calculation = 10;
    public static int Rain_Acid_Dissolver_Block__Deterioration__Chance = 100;

    public static int Rain_Acid_Scheduler_Dissolver__Calculation__Intervals__In__Ticks = 100;
    public static int Rain_Acid_Scheduler_Player__Damager__Calculation__Intervals__In__Ticks = 200;

    public static String[] Lightning_Allowed__Worlds = new String[] { "world" };
    public static int Lightning_Damage_Damage = 5;
    public static int Lightning_Damage_Damage__Radius = 10;
    public static String Lightning_Damage_Hit__Message = "You were zapped by lightning. Ouch!";

    public static int Lightning_Attraction_Blocks_AttractionChance = 80;
    public static Integer[] Lightning_Attraction_Blocks_Attractors = new Integer[] { Block.IRON_BLOCK.id };

    public static int Lightning_Attraction_Players_AttractionChance = 80;
    public static Integer[] Lightning_Attraction_Players_Attractors = new Integer[] { Item.IRON_AXE.id };
    public static Integer[][] Lightning_Melter_Block__Transformations = new Integer[][] {
	    { 12, 20 }, { 20, 0 } };

    public static String[] Snow_PassThruBlocks_Allowed__Worlds = new String[] { "world" };
    public static Integer[] Snow_PassThruBlocks_Block__IDS__To__Pass__Through = new Integer[] { 18 };

    public static String[] Natural__Disasters_Meteor_Allowed__Worlds = new String[] { "world" };

    public static double Natural__Disasters_Meteor_Chance__To__Spawn = 8;
    public static int Natural__Disasters_Meteor_Lives = 3;

    public static String Natural__Disasters_Meteor_Message__On__Meteor__Crash = "A meteor has exploded at %x, %y, %z.";
    public static int Natural__Disasters_Meteor_Shockwave_Damage = 10;
    public static int Natural__Disasters_Meteor_Shockwave_Damage__Radius = 100;
    public static String Natural__Disasters_Meteor_Shockwave_Damage__Message = "You have been flattened by a meteor!";
    public static long Natural__Disasters_Meteor_Scheduler_Spawner__Recalculation__Intervals__In__Ticks = 72000;

    public static String[] Natural__Disasters_Wildfires_Allowed__Worlds = new String[] {
	    "world", "nether" };
    public static int Natural__Disasters_Wildfires_Chance__To__Start = 20;
    public static int Natural__Disasters_Wildfires_Spread__Limit = 2;
    public static Integer[] Natural__Disasters_Wildfires_Flammable__Blocks = new Integer[] {
	    Block.FENCE.id, Block.WOOD.id, Block.WOOD_STAIRS.id,
	    Block.WOODEN_DOOR.id, Block.LEAVES.id, Block.BOOKSHELF.id,
	    Block.TNT.id, Block.GRASS.id, Block.WOOL.id, Block.VINE.id,
	    Block.WORKBENCH.id, Block.BIRCH_WOOD_STAIRS.id,
	    Block.BROWN_MUSHROOM.id, Block.CROPS.id, Block.RED_MUSHROOM.id,
	    Block.FENCE_GATE.id, Block.BREWING_STAND.id, Block.NETHERRACK.id };
    public static String Natural__Disasters_Wildfires_Message__On__Start = "A wildfire has been spotted around %x, %y, %z!";
    public static long Natural__Disasters_Wildfires_Scheduler_Spawner__Recalculation__Intervals__In__Ticks = 72000;

    public static boolean Features_Rain_Acid = true;
    public static boolean Features_Lightning_Greater__Range__And__Damage = true;
    public static boolean Features_Lightning_Player__Attraction = true;
    public static boolean Features_Lightning_Block__Attraction = true;
    public static boolean Features_Lightning_Block__Transformations = true;
    public static boolean Features_Snow_PassThruBlocks = true;
    public static boolean Features_Snow_Slow__Players__Down = true;
    public static boolean Features_Meteor = true;
    public static boolean Features_Wildfires = true;

};
