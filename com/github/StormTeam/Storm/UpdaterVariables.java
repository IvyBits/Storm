package github.StormTeam.Storm;

import com.github.StormTeam.Storm.ReflectConfiguration;
import com.github.StormTeam.Storm.Storm;

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
