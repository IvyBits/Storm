package tk.ivybits.storm.nms;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public interface NMSHooks {
    void setBlockFast(World world, int x, int y, int z, int typeId, byte data);

    void updateChunkClient(Player player, int x, int y);

    Class getMeteorImplementation();

    void patchMeteor() throws ReflectiveOperationException;
}
