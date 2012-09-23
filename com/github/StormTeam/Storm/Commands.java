package com.github.StormTeam.Storm;

import com.github.StormTeam.Storm.Storm;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private CommandRunners commands;

    public Commands(Storm storm) {
	commands = new CommandRunners(storm);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
	    String[] args) {

	if (!(sender instanceof Player)) {
	    refuse(sender);
	    return false;

	}
	
	Player player = (Player) sender;
	Location toTarget = player.getTargetBlock(null, 200).getLocation();


	if (cmd.getName().equalsIgnoreCase("meteor")) {
	    System.out.println("Meteor!");
	    Location ploc = player.getLocation();
	    Location toSpawn = ploc.toVector()
		    .add(ploc.getDirection().normalize())
		    .toLocation(ploc.getWorld());
	    commands.meteor(toTarget, toSpawn);
	    return true;
	} else if (cmd.getName().equalsIgnoreCase("wildfire")) {
	    commands.wildfire(toTarget);
	    return true;

	} else if (cmd.getName().equalsIgnoreCase("acidrain")) {
	    commands.acidRain(player.getWorld());
	    return true;
	} else if (cmd.getName().equalsIgnoreCase("blizzard")) {
	    commands.blizzard(player.getWorld());
	    return true;
	}

	// do something
	return false;
    }

    public void refuse(CommandSender s) {
	s.sendMessage(ChatColor.RED
		+ "Sorry, you must be in game to use that command!");
    }

}
