package com.github.Icyene.Storm;

import net.minecraft.server.Block;

public class GlobalVariables {

    // use transient flag for values not to go in config

    // Acid rain

    // TODO Put a list/Int[] of all children of

    public static String[] storm_rain_acid_allowedWorlds = new String[] { "world" };
    public static int storm_rain_acid_acidRainChance = 100;
    public static boolean storm_rain_acid_acidRain = true;
    public static boolean storm_rain_acid_acidSnow = true;
    public static String storm_rain_acid_acidRainMessage = "Acid has started to fall from the sky!";

    public static int storm_rain_acid_damager_damagerDamage = 2;
    public static int storm_rain_acid_damager_playerDamageChance = 100;

    public static boolean storm_rain_acid_damager_getHungry = true;
    public static int storm_rain_acid_damager_hungerTicks = 300;
    public static int storm_rain_acid_damager_playerHungerChance = 100;
    public static String storm_rain_acid_damager_acidRainPoisonMessage = "You have been damaged and poisoned by the acidic downfall!";
    public static String storm_rain_acid_damager_acidRainHurtMessage = "You have been damaged by the acidic downfall!";

    public static Integer[][] storm_rain_acid_dissolver_blockTransformations = new Integer[][] {
	    new Integer[] {
		    18, 0 }, new Integer[] {
		    2, 3 }, new Integer[] {
		    1, 4 } };

    public static int storm_rain_acid_dissolver_chunkDissolveChance = 100;
    public static int storm_rain_acid_dissolver_chunksToCalculate = 4;
    public static int storm_rain_acid_dissolver_blocksPerChunk = 10;
    public static int storm_rain_acid_dissolver_deteriorationChance = 100;

    public static int storm_rain_acid_scheduler_dissolverDelayTicks = 100;
    public static int storm_rain_acid_scheduler_playerDamagerDelayTicks = 500;
    // TODO Add block transformations in config

    // end acid rain config

    // Lightning
    public static String[] storm_lightning_allowedWorlds = new String[] { "world" };
    public static int storm_lightning_damage_strikeDamage = 5;
    public static int storm_lightning_damage_strikeRadius = 10;
    public static String storm_lightning_damage_strikeMessage = "You were zapped by lightning. Ouch!";

    public static int storm_lightning_attraction_blocks_attractionChance = 100;

    public static Integer[] storm_lightning_attraction_blocks_attractors = new Integer[] {
	    Block.IRON_BLOCK.id };

    // public static Integer[]
    // storm_lightning_melter_blockTransformations_sandToGlass = new Integer[] {
    // 12, 20 };
    // public static Integer[]
    // storm_lightning_melter_blockTransformations_glassToAir = new Integer[] {
    // 20, 0 };
    public static Integer[][] storm_lightning_melter_blockTransformations = new Integer[][] {
	    new Integer[] { 12, 20 }, new Integer[] { 20, 0 } };

    // TODO Add block transformations in config, add player attraction

    // end lightning configuration

    // Snow configuration

    
    public static String[] storm_snow_insubstantial_allowedWorlds = new String[] { "world" };
    public static Integer[] storm_snow_insubstantial_passThroughBlockIds = new Integer[] { 18 };
    
    public static String[] storm_snow_piling_allowedWorlds = new String[] { "world" };
    public static int storm_snow_piling_pilerDelayTicks = 30;
    public static int storm_snow_piling_chunksToCalculate = 4;
    public static int storm_snow_piling_blocksPerChunk = 3;

    // End snow configuration

    // Meteor config
    public static String[] storm_meteorites_meteor_allowedWorlds = new String[] { "world" };
    public static float storm_meteorites_meteor_trailPower = 3.0F;
    public static float storm_meteorites_meteor_impactExplosionRadius = 30.0F;
    public static double storm_meteorites_meteor_spawnChance = 100D;
    public static long storm_meteorites_meteor_recalculationDelayTicks = 200;
    public static int storm_meteorites_meteor_spawnHeight = 160;
    public static float storm_meteorites_meteor_pitch = -10;
    public static float storm_meteorites_meteor_brightness = 50F;
    public static String storm_meteorites_meteor_impactMessage = "A meteor has exploded at <x>, <y>, <z>.";

}