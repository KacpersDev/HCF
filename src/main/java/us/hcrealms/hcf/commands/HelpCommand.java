package us.hcrealms.hcf.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import us.hcrealms.hcf.Base;

public class HelpCommand implements CommandExecutor {
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if (command.getName().equalsIgnoreCase("help")) {
			for (String msg : Base.config.getStringList("Help")) {
				commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			}
			return true;
		}
		return false;
	}
}
