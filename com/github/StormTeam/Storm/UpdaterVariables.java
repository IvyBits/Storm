package com.github.StormTeam.Storm;

/**
 *
 * @author Tudor
 */
public class UpdaterVariables extends ReflectConfiguration {
    
    public UpdaterVariables(Storm storm, String name) {
        super(storm, name);
    }
    
    public boolean Updater_Check__For__Updates = true;
    public boolean Updater_Automagically__Update = false;
    
}
