package com.github.StormTeam.Storm.Blizzard;

import java.lang.reflect.Method;

import com.github.StormTeam.Storm.Blizzard.Blocks.SnowLayer;

import net.minecraft.server.Block;
import net.minecraft.server.StepSound;

public class ModSnow {

    public static void mod(boolean doMod) {

        try {
            if (doMod) {

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

                Block.byId[Block.SNOW.id] = (Block) h.invoke(a.invoke(c.invoke(p.invoke(v.invoke(((SnowLayer) 
                        (new SnowLayer(78, 66)).b("snow")))), 0.1F), Block.k), 0);
            } else {                
                Block.byId[Block.SNOW.id] = Block.SNOW;
            }

        } catch (Exception e) {};
    }
}
