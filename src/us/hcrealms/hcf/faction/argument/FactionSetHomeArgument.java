package us.hcrealms.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import us.hcrealms.hcf.Base;
import us.hcrealms.hcf.ConfigurationService;
import us.hcrealms.hcf.faction.FactionMember;
import us.hcrealms.hcf.faction.claim.Claim;
import us.hcrealms.hcf.faction.struct.Role;
import us.hcrealms.hcf.faction.type.PlayerFaction;
import us.hcrealms.hcf.util.command.CommandArgument;

public class FactionSetHomeArgument extends CommandArgument {
    private final Base plugin;

    public FactionSetHomeArgument(final Base plugin) {
        super("sethome", "Sets the faction home location.");
        this.plugin = plugin;
    }

    public String getUsage(final String label) {
        return '/' + label + ' ' + this.getName();
    }

    @SuppressWarnings("deprecation")
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }
        final Player player = (Player) sender;
        final PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player);
        if(playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return true;
        }
        final FactionMember factionMember = playerFaction.getMember(player);
        if(factionMember.getRole() == Role.MEMBER) {
            sender.sendMessage(ChatColor.RED + "You must be a faction officer to set the home.");
            return true;
        }
        final Location location = player.getLocation();
        boolean insideTerritory = false;
        for(final Claim claim : playerFaction.getClaims()) {
            if(claim.contains(location)) {
                insideTerritory = true;
                break;
            }
        }
        if(!insideTerritory) {
            player.sendMessage(ChatColor.RED + "You may only set your home in your territory.");
            return true;
        }
        playerFaction.setHome(location);
        playerFaction.broadcast(ConfigurationService.TEAMMATE_COLOUR + factionMember.getRole().getAstrix() + sender.getName() + ChatColor.YELLOW + " has set the faction home.");
        return true;
    }
}
