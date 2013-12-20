package tk.ivybits.storm.nms.v1_7_R1;

import net.minecraft.server.v1_7_R1.Block;
import net.minecraft.server.v1_7_R1.ChunkCoordIntPair;
import net.minecraft.server.v1_7_R1.EntityTNTPrimed;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_7_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import tk.ivybits.storm.nms.NMSHooks;

import java.lang.reflect.Method;

public class v1_7_R1Hooks implements NMSHooks {
    @Override
    public void setBlockFast(World world, int x, int y, int z, int typeId, byte data) {
        ((CraftChunk) world.getChunkAt(x, z)).getHandle().a(x & 15, y, z & 15, Block.e(typeId), data);
    }

    @Override
    public void updateChunkClient(Player player, int x, int z) {
        ((CraftPlayer) player).getHandle().chunkCoordIntPairQueue.add(new ChunkCoordIntPair(x, z));
    }

    @Override
    public Class getMeteorImplementation() {
        return v1_7_R1EntityMeteor.class;
    }

    @Override
    public void patchMeteor() throws ReflectiveOperationException {
        Method a = net.minecraft.server.v1_7_R1.EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
        a.setAccessible(true);
        a.invoke(a, getMeteorImplementation(), "StormMeteor", 12);
    }

    @Override
    public int createExplosion(Location where, float power) {
        net.minecraft.server.v1_7_R1.World nw = ((CraftWorld) where.getWorld()).getHandle();
        EntityTNTPrimed dummy = new EntityTNTPrimed(nw); //Entity is abstract, and really, we just need the id...
        nw.createExplosion(dummy, where.getX(), where.getY(), where.getZ(), power, true, true);
        return dummy.getId();
    }
}
