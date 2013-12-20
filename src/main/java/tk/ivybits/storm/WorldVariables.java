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

package tk.ivybits.storm;

import tk.ivybits.storm.utility.ReflectConfiguration;
import static org.bukkit.Material.*;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Container class for all configurable storm variables.
 */

public class WorldVariables extends ReflectConfiguration {


    /**
     * Creates a GlobalVariable object with the given name.
     *
     * @param plugin Plugin: used to know which folder to save to
     * @param name   The name of the file
     */

    public WorldVariables(Plugin plugin, String name, String folder) {
        super(plugin, name, folder);
    }

    //Acid Rain
    @LimitInteger
    @Comment("The chance for acid rain to occur.")
    public int Acid__Rain_Acid__Rain__Chance = 5;
    @Comment("The base intervals between acid rain tries to start.")
    public int Acid__Rain_Acid__Rain__Base__Interval = 72000;
    @Comment("The message broadcast when acid rain starts.")
    public String Acid__Rain_Messages_On__Acid__Rain__Start = "Acid has started to fall from the sky!";
    @Comment("The message broadcast when acid rain ends.")
    public String Acid__Rain_Messages_On__Acid__Rain__Stop = "Acid rain ceases to fall!";
    @Comment("The message sent to a player when they are damaged by acid rain.")
    public String Acid__Rain_Messages_On__Player__Damaged__By__Acid__Rain = "You have been hurt by the acidic downfall!";
    @Comment("The damage a player is dealt when they are acid rain, measured in half hearts.")
    public int Acid__Rain_Player_Damage__From__Exposure = 1;
    @Comment("The damage an entity is dealt when it is in acid rain, measured in half hearts.")
    public int Acid__Rain_Entity_Damage__From__Exposure = 1;
    @Comment({"The list of block transformations that occur during acid rain. Key is the FROM block, value is TO block.", "18 -> " +
            "0 means leaves (ID of 18) will get turned into air (ID of 0)"})
    public List<List<String>> Acid__Rain_Dissolver_Block__Transformations = new ArrayList<List<String>>() {
        {
            add(Arrays.asList("18", "0"));
            add(Arrays.asList("102", "0"));
            add(Arrays.asList("111", "0"));
            add(Arrays.asList("12", "0"));
            add(Arrays.asList("20", "0"));
            add(Arrays.asList("2", "3"));
            add(Arrays.asList("1", "4"));
            add(Arrays.asList("4", "48"));
            add(Arrays.asList("6", "31"));
            add(Arrays.asList("31", "31"));
            add(Arrays.asList("37", "31"));
            add(Arrays.asList("38", "31"));
            add(Arrays.asList("39", "31"));
            add(Arrays.asList("40", "31"));
            add(Arrays.asList("59", "31"));
            add(Arrays.asList("60", "3"));

        }
    };
    @Comment("A list of blocks that entities will not take damage when within specified radius.")
    public List<Integer> Acid__Rain_Absorbing__Blocks = new ArrayList<Integer>() {
        {
            add(GOLD_BLOCK.getId());
        }
    };
    @Comment("The radius the absorbing blocks protect.")
    public int Acid__Rain_Absorbing__Radius = 2;
    @LimitInteger
    @Comment("The chance for a block to be deteriorated.")
    public int Acid__Rain_Dissolver_Block__Deterioration__Chance = 10;
    @LimitInteger(limit = 16)
    @Comment("The amount of a chunk that can be deteriorated. An integral number from 0->16")
    public int Acid__Rain_Dissolver_Block__Deterioration__Area = 16;
    @Comment("The delay between sets of block deteriorations, in ticks.")
    public int Acid__Rain_Scheduler_Dissolver__Calculation__Intervals__In__Ticks = 10;
    @Comment("The delay between damaging entities, in ticks.")
    public int Acid__Rain_Scheduler_Damager__Calculation__Intervals__In__Ticks = 40;

    public boolean Acid__Rain_Features_Dissolving__Blocks = true;
    public boolean Acid__Rain_Features_Player__Damaging = true;
    public boolean Acid__Rain_Features_Entity__Damaging = true;
    public boolean Acid__Rain_Features_Entity__Shelter__Pathfinding = true;

    //Thunder Storms
    @LimitInteger
    @Comment("The chance for thunder storm to occur.")
    public int Thunder__Storm_Thunder__Storm__Chance = 4;
    @Comment("The base intervals between thunder storm tries to start.")
    public int Thunder__Storm_Thunder__Storm__Base__Interval = 72000;
    @Comment("The message to send when thunder storm is started.")
    public String Thunder__Storm_Messages_On__Thunder__Storm__Start = "An electrical storm has started! Get inside for safety!";
    @Comment("The message to send when thunder storm is stopped.")
    public String Thunder__Storm_Messages_On__Thunder__Storm__Stop = "Zeus has stopped bowling!";
    @LimitInteger
    @Comment("The chance of being stroke by thunder storm.")
    public int Thunder__Storm_Strike__Chance = 5;
    @Comment("The delay between strikes, in ticks.")
    public int Thunder__Storm_Scheduler_Striker__Calculation__Intervals__In__Ticks = 30;
    public boolean Thunder__Storm_Features_Thunder__Striking = true;
    public boolean Thunder__Storm_Features_Entity__Shelter__Pathfinding = true;
    //Blizzards
    @LimitInteger
    @Comment("The chance for blizzard to occur.")
    public int Blizzard_Blizzard__Chance = 20;
    @Comment("The base intervals between blizzard tries to start.")
    public int Blizzard_Blizzard__Base__Interval = 72000;
    @Comment("The message to send when blizzard is started.")
    public String Blizzard_Messages_On__Blizzard__Start = "It has started to snow violently! Seek a warm biome for safety!";
    @Comment("The message to send when blizzard is stopped.")
    public String Blizzard_Messages_On__Blizzard__Stop = "The blizzard has stopped!";
    @Comment("The message to send when a player is damaged by cold.")
    public String Blizzard_Messages_On__Player__Damaged__Cold = "You are freezing!";
    @Comment("The list of blocks that can prevent damage by blizzard when the player is close.")
    public List<Integer> Blizzard_Heating__Blocks = Arrays.asList(
            FIRE.getId(), LAVA.getId(), STATIONARY_LAVA.getId(),
            BURNING_FURNACE.getId());
    @Comment("The effective radius of blocks that can prevent damage by blizzard.")
    public int Blizzard_Heat__Radius = 2;
    @Comment("The amount of damage experienced by players in a blizzard, in hearts.")
    public int Blizzard_Player_Damage__From__Exposure = 2;
    @Comment("The amount of damage experienced by entities in a blizzard, in hearts.")
    public int Blizzard_Entity_Damage__From__Exposure = 2;
    @Comment("The speed loss experienced by players in a blizzard.")
    public double Blizzard_Player_Speed__Loss__While__In__Snow = 0.4D;
    @Comment("The delay between damages by blizzard, in ticks.")
    public int Blizzard_Scheduler_Damager__Calculation__Intervals__In__Ticks = 40;

    public boolean Blizzard_Features_Player__Damaging = true;
    public boolean Blizzard_Features_Entity__Damaging = true;
    public boolean Blizzard_Features_Entity__Shelter__Pathfinding = true;
    public boolean Blizzard_Features_Slowing__Snow = true;
    //Natural Disasters
    //-meteor
    @LimitInteger
    @Comment("The chance that meteor will occur.")
    public int Natural__Disasters_Meteor_Chance__To__Spawn = 8;
    @Comment("The base intervals between meteor tries to start.")
    public int Natural__Disasters_Meteor_Meteor__Base__Interval = 72000;
    @Comment("The message sent when a meteor hits ground. %x, %y, and %z is replaced with coordinates.")
    public String Natural__Disasters_Meteor_Messages_On__Meteor__Crash = "A meteor has exploded at %x, %y, %z.";
    @Comment("The message sent when a player is damaged by meteor.")
    public String Natural__Disasters_Meteor_Messages_On__Damaged__By__Shockwave = "You have been flattened by a meteor!";
    @Comment("The number of hearts worth of damage that will be dealt when flattened by a meteor")
    public int Natural__Disasters_Meteor_Shockwave_Damage = 10;
    @Comment("The radius where players and entities alike will be damaged.")
    public int Natural__Disasters_Meteor_Shockwave_Damage__Radius = 100;
    @Comment("Will solid meteor spawn?")
    public boolean Natural__Disasters_Meteor_Meteor__Spawn = true;
    @Comment("The ores to deposit and the chances of the ores being deposited.")
    public List<List<String>> Natural__Disasters_Meteor_Ore__Chance__Percentages = new ArrayList<List<String>>() {
        {
            //block ID, chance
            add(Arrays.asList("1", "68"));
            add(Arrays.asList("56", "5"));
            add(Arrays.asList("14", "5"));
            add(Arrays.asList("15", "5"));
            add(Arrays.asList("16", "10"));
            add(Arrays.asList("129", "7"));
        }
    };
    @LimitInteger
    public int Natural__Disasters_Wildfires_Chance__To__Start = 20;
    public int Natural__Disasters_Wildfires_Wildfire__Base__Interval = 72000;
    public int Natural__Disasters_Wildfires_Spread__Limit = 5;
    public int Natural__Disasters_Wildfires_Scan__Radius = 2;
    public String Natural__Disasters_Wildfires_Messages_On__Start = "A wildfire has been spotted around %x, %y, %z!";
    public int Natural__Disasters_Wildfires_Maximum__Fires = 100;
    public List<Integer> Natural__Disasters_Wildfires_Flammable__Blocks = Arrays.asList(FENCE.getId(), WOOD.getId(), WOOD_STAIRS.getId(),
            WOODEN_DOOR.getId(), LEAVES.getId(), BOOKSHELF.getId(),
            GRASS.getId(), WOOL.getId());
    @LimitInteger
    public int Natural__Disasters_Earthquakes_Chance__To__Start = 1;
    public int Natural__Disasters_Earthquakes_Earthquake__Base__Interval = 72000;
    public String Natural__Disasters_Earthquakes_Message__On__Earthquake__Start = "The ground beneath you begins quaking with a magnitude of %m! Run mortal, run!";
    public String Natural__Disasters_Earthquakes_Message__On__Earthquake__End = "The earth stops shaking.";
    @Comment("The horrible sound to play when the ground suddenly cracks.")
    public boolean Natural__Disasters_Earthquake_Flying__Blocks = true;
    public boolean Natural__Disasters_Earthquake_Screen__Shaking = true;

    @LimitInteger
    public int Natural__Disasters_Volcano_Chance__To__Start = 1;
    public int Natural__Disasters_Volcano_Volcano__Base__Interval = 72000;
    public String Natural__Disasters_Volcano_Message__On__Volcano__Start = "A volcano has burst out of the ground at %x, %y, %z!";
    public boolean Natural__Disasters_Volcano_Features_Erupting = true;
    public boolean Natural__Disasters_Volcano_Features_Survive__Restarts = true;

    public boolean Weathers__Enabled_Acid__Rain = true;
    public boolean Weathers__Enabled_Thunder__Storms = true;
    public boolean Weathers__Enabled_Blizzards = true;
    public boolean Weathers__Enabled_Natural__Disasters_Meteors = true;
    public boolean Weathers__Enabled_Natural__Disasters_Wildfires = true;
    public boolean Weathers__Enabled_Natural__Disasters_Volcanoes = true;
    public boolean Weathers__Enabled_Natural__Disasters_Earthquakes = true;

    public boolean Force__Weather__Textures = true;
    public String Textures_Acid__Rain__Texture__Path = "https://dl.dropboxusercontent.com/u/67341745/storm/acidrain.zip";
    public String Textures_Blizzard__Texture__Path = "https://dl.dropboxusercontent.com/u/67341745/storm/blizzard.zip";
    public String Textures_Default__Texture__Path = "https://dl.dropboxusercontent.com/u/67341745/storm/Default.zip";
}
