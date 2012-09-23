package com.github.StormTeam.Storm.Earthquake.Tasks;

import com.github.StormTeam.Storm.Earthquake.Quake;
import com.github.StormTeam.Storm.Storm;
import java.util.List;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
 * @author Giant
 */
public class QuakeTask implements Runnable {
	
	private Storm s;
	private Quake q;
	private int i = 0;
	
	public QuakeTask(Quake q, Storm s) {
		this.q = q;
		this.s = s;
	}
	
	

	@Override
	public void run() {
		List<Player> players = q.getWorld().getPlayers();
		for(Player p : players) {
			// Don't bother creative players
			if(p.getGameMode() == GameMode.CREATIVE)
				continue;

			// Player isn't quaking...
			if(!q.isQuaking(p.getLocation()))
				continue;
			
			int x = q.getEpicenter().LEFT - p.getLocation().getBlockX() + 1;
			int z = q.getEpicenter().RIGHT - p.getLocation().getBlockZ() + 1;
			
			int a = 5 / ((x + z) / 2);
			
			if(i == 0) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2, a), true);
			}else{
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, a), true);
			}
		}
	}
}
