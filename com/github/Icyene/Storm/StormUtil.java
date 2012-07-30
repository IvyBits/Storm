package com.github.Icyene.Storm;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StormUtil
{
    public static final Logger log = Logger.getLogger("Minecraft");
    static final String prefix = "[Storm] ";
    
    public static void log(String logM)
    {
	log.log(Level.INFO, prefix + logM);
    }

    public static void log(Level level, String logM)
    {
	log.log(level, prefix + logM);
    }

      
}
