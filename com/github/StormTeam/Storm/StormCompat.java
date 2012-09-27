package com.github.StormTeam.Storm;

import java.lang.reflect.Method;

/**
 *
 * @author Tudor
 */
public class StormCompat {
    //A class to try to maintain backwards compatibility. Kinda limited, but ah well. 

    public boolean contains(Object ob, Object key) {
        //Tries to use reflection to remedy inconsistencies between LongHashMap/HashSet

        try {

            //If this works without crashing, its a LongHashMap
            Method cont = ob.getClass().getMethod("contains", Object.class);
            return (Boolean) cont.invoke(ob, key);

        } catch (Exception e) {
        }

        try {
            //If this works without crashing, its a LongHashSet
            Method cont = ob.getClass().getMethod("containsKey", Object.class);
            return (Boolean) cont.invoke(ob, key);

        } catch (Exception ex) {
        }

        return false;

    }
}
