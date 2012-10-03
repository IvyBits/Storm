package com.github.StormTeam.Storm;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private CommandExecutors commands;

    public Commands(Storm storm) {
        commands = new CommandExecutors(storm);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
            String[] args) {

        if (!(sender instanceof Player)) { //I know this isn't the right way to do it, but I'm tired...
            refuse(sender);
            return false;
        }

        Player player = (Player) sender;
        Location toTarget = player.getTargetBlock(null, 0).getLocation();


        if (cmd.getName().equalsIgnoreCase("meteor")) {
            System.out.println("Meteor!");
            try {
            Location ploc = player.getLocation();
            Location toSpawn = ploc.toVector()
                    .add(ploc.getDirection().normalize())
                    .toLocation(ploc.getWorld());
            commands.meteor(toTarget, toSpawn);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        } else if (cmd.getName().equalsIgnoreCase("wildfire")) {
            commands.wildfire(toTarget);
            return true;

        } else if (cmd.getName().equalsIgnoreCase("acidrain")) {
            commands.acidRain(player.getWorld());
            return true;
        } else if (cmd.getName().equalsIgnoreCase("blizzard")) { //Too bad Java doesn't have elif... :-(
            commands.blizzard(player.getWorld());
            return true;
        } else if (cmd.getName().equalsIgnoreCase("thunderstorm")) { 
            commands.thunderstorm(player.getWorld());
            return true;
        }
        
        return false;
    }

    public void refuse(CommandSender s) {
        s.sendMessage(ChatColor.RED
                + "Sorry, you must be in game to use that command!");
    }
}
