package com.github.Icyene.Storm.Snow.RealisticSnow;

import java.lang.reflect.Method;

import com.github.Icyene.Storm.Snow.RealisticSnow.Blocks.Snow;

import net.minecraft.server.Block;
import net.minecraft.server.StepSound;

public class ModSnow {

    public void mod(boolean doMod) {

	try {
	    Method v = Block.class.getDeclaredMethod("v");
	    Method p = Block.class.getDeclaredMethod("p");
	    Method c = Block.class.getDeclaredMethod("c", float.class);
	    Method a = Block.class.getDeclaredMethod("a", StepSound.class);
	    Method h = Block.class.getDeclaredMethod("h", int.class);
	    v.setAccessible(true);
	    p.setAccessible(true);
	    c.setAccessible(true);
	    a.setAccessible(true);
	    h.setAccessible(true);
	    if (doMod) {
		Block.byId[Block.SNOW.id] = null;
		Block SNOW = ((Snow) (new Snow(78, 66)).b("snow"));

		SNOW = (Block) v.invoke(SNOW);
		SNOW = (Block) p.invoke(SNOW);
		SNOW = (Block) c.invoke(SNOW, 0.1F);
		SNOW = (Block) a.invoke(SNOW, Block.k);
		SNOW = (Block) h.invoke(SNOW, 0);
		Block.byId[Block.SNOW.id] = SNOW;
	    } else {

		Block.byId[Block.SNOW.id] = null;
		Block.byId[Block.SNOW.id] = Block.SNOW;

	    }

	} catch (Exception e) {
	};

    }

}
