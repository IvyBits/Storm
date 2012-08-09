package com.github.Icyene.Storm;

import net.minecraft.server.Block;
import net.minecraft.server.Item;

public class GlobalVariables {

    public static String[] rain_acid_allowedWorlds = new String[] { "world" };
    public static int rain_acid_acidRainChance = 100;
    public static boolean rain_acid_acidRain = true;
    public static boolean rain_acid_acidSnow = true;
    public static String rain_acid_acidRainMessage = "Acid has started to fall from the sky!";
    public static int rain_acid_damager_damagerDamage = 2;
    public static int rain_acid_damager_playerDamageChance = 100;
    public static boolean rain_acid_damager_getHungry = true;
    public static int rain_acid_damager_hungerTicks = 300;
    public static int rain_acid_damager_playerHungerChance = 100;
    public static String rain_acid_damager_acidRainPoisonMessage = "You have been damaged and poisoned by the acidic downfall!";
    public static String rain_acid_damager_acidRainHurtMessage = "You have been damaged by the acidic downfall!";
    public static Integer[][] rain_acid_dissolver_blockTransformations = new Integer[][] {{ 18, 0 }, { 2, 3 }, { 1, 4 }};
    public static int rain_acid_dissolver_chunkDissolveChance = 100;
    public static int rain_acid_dissolver_chunksToCalculate = 4;
    public static int rain_acid_dissolver_blocksPerChunk = 10;
    public static int rain_acid_dissolver_deteriorationChance = 100;
    public static int rain_acid_scheduler_dissolverDelayTicks = 100;
    public static int rain_acid_scheduler_playerDamagerDelayTicks = 500; 
    public static String[] lightning_allowedWorlds = new String[] { "world" };
    public static int lightning_damage_strikeDamage = 5;
    public static int lightning_damage_strikeRadius = 10;
    public static String lightning_damage_strikeMessage = "You were zapped by lightning. Ouch!";
    public static int lightning_attraction_blocks_attractionChance = 100;
    public static Integer[] lightning_attraction_blocks_attractors = new Integer[] {Block.IRON_BLOCK.id};
    public static int lightning_attraction_players_attractionChance = 100;
    public static Integer[] lightning_attraction_players_attractors = new Integer[] {Item.IRON_AXE.id};
    public static Integer[][] lightning_melter_blockTransformations = new Integer[][] {{ 12, 20 },  { 20, 0 } };
    public static String[] snow_insubstantial_allowedWorlds = new String[] { "world" };
    public static Integer[] snow_insubstantial_passThroughBlockIds = new Integer[] { 18 };
    public boolean snow_piling = true;
    public static String[] snow_piling_allowedWorlds = new String[] { "world" };
    public static int snow_piling_pilerDelayTicks = 30;
    public static int snow_piling_chunksToCalculate = 4;
    public static int snow_piling_blocksPerChunk = 3;
    public static String[] meteorites_meteor_allowedWorlds = new String[] { "world" };
    public static float meteorites_meteor_trailPower = 3.0F;
    public static float meteorites_meteor_impactExplosionRadius = 30;
    public static double meteorites_meteor_spawnChance = 100;
    public static long meteorites_meteor_recalculationDelayTicks = 200;
    public static int meteorites_meteor_spawnHeight = 160;
    public static float meteorites_meteor_pitch = -10;
    public static float meteorites_meteor_brightness = 50;
    public static float meteorites_meteor_accelarationY = -1;
    public static float meteorites_meteor_yield = 0.1F;
    public static String meteorites_meteor_impactMessage = "A meteor has exploded at <x>, <y>, <z>.";

}