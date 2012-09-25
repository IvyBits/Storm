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
	private boolean dbg = true;
	private int t = 10;
	
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
			
			int x = 40 - Math.abs(q.getEpicenter().LEFT - p.getLocation().getBlockX());
			int z = 40 - Math.abs(q.getEpicenter().RIGHT - p.getLocation().getBlockZ());
			
			float d = ((x + z) / 2);
			d = (d < 1) ? 1 : d;
			float step = 5 / d;
			step = (step < 1) ? step * 10 : step;
			int a = 6 - (int) Math.round(step);
			a = (a < 0) ? 0 : ((a > 5) ? 5 : a);
			
			
			if(dbg) {
				dbg = false;
				t = 10;
				s.getLogger().severe("========= DEBUG ========");
				s.getLogger().severe("====== Quake =====");
				s.getLogger().severe("Epicenter X: " + q.getEpicenter().LEFT);
				s.getLogger().severe("Epicenter Z: " + q.getEpicenter().RIGHT);
				s.getLogger().severe("====== Player =====");
				s.getLogger().severe("Location X: " + p.getLocation().getBlockX());
				s.getLogger().severe("Location Z: " + p.getLocation().getBlockZ());
				s.getLogger().severe("====== Calculations =====");
				s.getLogger().severe("x = q.X - p.X + 1 == " + x);
				s.getLogger().severe("z = q.Z - p.Z + 1 == " + z);
				s.getLogger().severe("d = ((x + z) / 2) == " + d);
				s.getLogger().severe("a = 5 / d == " + a);
			}else{
				--t;
				
				if(t == 0) {
					dbg = true;
				}
			}
			
			
			if(i == 0) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2, a), true);
			}else{
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2, a), true);
			}
		}
	}
}
