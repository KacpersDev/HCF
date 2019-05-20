package us.hcrealms.hcf.faction.type;

import org.bukkit.command.CommandSender;

import us.hcrealms.hcf.ConfigurationService;

import java.util.Map;

public class WildernessFaction extends Faction {
	public WildernessFaction() {
		super("Wilderness");
	}

	public WildernessFaction(final Map<String, Object> map) {
		super(map);
	}

	@Override
	public String getDisplayName(final CommandSender sender) {
		return ConfigurationService.WILDERNESS_COLOUR + this.getName();
	}
}
