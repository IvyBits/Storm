package com.github.Icyene.Storm.Earthquake;

import org.bukkit.block.Block;

/**
 *
 * @author Giant
 */
public class QuakeUtil {
	
	public static boolean isBounceable(Block b) {
		switch(b.getType()) {
			case BED_BLOCK:
			
			case DIODE_BLOCK_OFF:
			case DIODE_BLOCK_ON:
			
			case FIRE:
			
			case GRASS:
			
			case IRON_DOOR_BLOCK:
			
			case LADDER:
			case LAVA:
			case LEVER:
			
			case PAINTING:
			case PISTON_BASE:
			case PISTON_STICKY_BASE:
			case POWERED_RAIL:
			
			case SAPLING:
			case SIGN_POST:
			case STATIONARY_WATER:
			case STATIONARY_LAVA:
			case STONE_BUTTON:
			case STONE_PLATE:
				
			case TORCH:
			case TRAP_DOOR:
			case TRIPWIRE:
			case TRIPWIRE_HOOK:
			
			case RAILS:
			case REDSTONE_WIRE:
			case REDSTONE_TORCH_ON:
			
			case WALL_SIGN:
			case WATER:
			case WOOD_PLATE:
			case WOODEN_DOOR:
				return false;
			default:
				break;
		}
		
		return true;
	}
	
	
}
