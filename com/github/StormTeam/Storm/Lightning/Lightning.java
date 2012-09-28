package com.github.StormTeam.Storm.Lightning;

import com.github.StormTeam.Storm.Storm;
import com.github.StormTeam.Storm.Lightning.Listeners.StrikeListener;

/**
 * @author hammale
 */
public class Lightning {

    public static void load(Storm storm) {
        Storm.pm.registerEvents(new StrikeListener(storm), storm);
    }
}
