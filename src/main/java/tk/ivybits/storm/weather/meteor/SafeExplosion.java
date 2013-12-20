package tk.ivybits.storm.weather.meteor;

import tk.ivybits.storm.utility.StormUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;

public class SafeExplosion implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent ex) {
        Entity en = ex.getEntity();
        if (en == null)
            return;

        if (Meteor.meteors.contains(en.getEntityId())) {

            ArrayList<Block> toProtect = new ArrayList<Block>(), toRemove = new ArrayList<Block>();

            for (Block block : ex.blockList())
                if (StormUtil.isBlockProtected(block))
                    toProtect.add(block);
                else
                    toRemove.add(block);

            for (Block block : toProtect)
                ex.blockList().remove(block);
            for (Block block : toRemove) {
                if (!block.getType().equals(Material.FIRE)) {
                    ex.blockList().remove(block);
                    block.setTypeId(0);
                }
            }

            if (en.isDead())
                Meteor.meteors.remove(en.getEntityId());
        }
    }
}
