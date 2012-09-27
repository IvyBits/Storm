package com.github.StormTeam.Storm.Blizzard;

import java.lang.reflect.Method;

import com.github.StormTeam.Storm.Blizzard.Blocks.SnowLayer;
import com.github.StormTeam.Storm.Storm;

import net.minecraft.server.Block;
import net.minecraft.server.StepSound;

public class ModSnow {

    public static void mod(boolean doMod) {

        try {
            if (doMod) {
                
                Method v, p, c, a, h, a_st;

                Class<?> bc = Block.class;

                if (Storm.version == 1.3) {

                    System.out.println("Modded snow for 1.3x");
                    v = Block.class.getDeclaredMethod("v");
                    p = bc.getDeclaredMethod("p");
                    c = bc.getDeclaredMethod("c", float.class);
                    a = bc.getDeclaredMethod("a", StepSound.class);
                    h = bc.getDeclaredMethod("h", int.class);
                    v.setAccessible(true);
                    p.setAccessible(true);
                    c.setAccessible(true);
                    a.setAccessible(true);
                    h.setAccessible(true);
                     Block.byId[Block.SNOW.id] = null;
                    Block.byId[Block.SNOW.id] = (Block) h.invoke(a.invoke(c.invoke(p.invoke(v.invoke(((SnowLayer) (new SnowLayer(78, 66)).b("snow")))), 0.1F), Block.k), 0);

                } else {

                    if (Storm.version == 1.2) {

                        System.out.println("Modded snow for 1.2x");
                        p = bc.getDeclaredMethod("p");
                        c = bc.getDeclaredMethod("c", float.class);
                        a = bc.getDeclaredMethod("a", StepSound.class);
                        h = bc.getDeclaredMethod("f", int.class);
                        a_st = bc.getDeclaredMethod("a", String.class);
                        a_st.setAccessible(true);

                        p.setAccessible(true);
                        c.setAccessible(true);
                        a.setAccessible(true);
                        h.setAccessible(true);
                        Block.byId[Block.SNOW.id] = null;
                        Block.byId[Block.SNOW.id] = (Block) h.invoke(a.invoke(c.invoke(a_st.invoke((SnowLayer) (new SnowLayer(78, 66)), "snow"), 0.1F), Block.k), 0);

                    }
                }

            } else {
                 Block.byId[Block.SNOW.id] = null;
                Block.byId[Block.SNOW.id] = Block.SNOW;
            }

        } catch (Exception e) {
            e.printStackTrace();
        };
    }
}
