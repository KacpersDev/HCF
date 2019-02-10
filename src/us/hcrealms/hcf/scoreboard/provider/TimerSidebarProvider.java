package us.hcrealms.hcf.scoreboard.provider;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import us.hcrealms.hcf.Base;
import us.hcrealms.hcf.ConfigurationService;
import us.hcrealms.hcf.DateTimeFormats;
import us.hcrealms.hcf.classes.PvpClass;
import us.hcrealms.hcf.classes.archer.ArcherClass;
import us.hcrealms.hcf.classes.bard.BardClass;
import us.hcrealms.hcf.classes.type.MinerClass;
import us.hcrealms.hcf.classes.type.RogueClass;
import us.hcrealms.hcf.commands.StaffModeCommand;
import us.hcrealms.hcf.event.EventTimer;
import us.hcrealms.hcf.event.eotw.EOTWHandler;
import us.hcrealms.hcf.event.faction.ConquestFaction;
import us.hcrealms.hcf.event.faction.EventFaction;
import us.hcrealms.hcf.event.tracker.ConquestTracker;
import us.hcrealms.hcf.faction.type.PlayerFaction;
import us.hcrealms.hcf.listener.VanishListener;
import us.hcrealms.hcf.reboot.RebootTimer;
import us.hcrealms.hcf.scoreboard.SidebarEntry;
import us.hcrealms.hcf.scoreboard.SidebarProvider;
import us.hcrealms.hcf.sotw.SotwTimer;
import us.hcrealms.hcf.timer.GlobalTimer;
import us.hcrealms.hcf.timer.PlayerTimer;
import us.hcrealms.hcf.timer.Timer;
import us.hcrealms.hcf.timer.type.TeleportTimer;
import us.hcrealms.hcf.util.BukkitUtils;
import us.hcrealms.hcf.util.DurationFormatter;

public class TimerSidebarProvider implements SidebarProvider {

	protected static String STRAIGHT_LINE = BukkitUtils.STRAIGHT_LINE_DEFAULT.substring(0, 14);
	protected static final String NEW_LINE = ChatColor.STRIKETHROUGH + "----------";

	private Base plugin;

	public TimerSidebarProvider(Base plugin) {
		this.plugin = plugin;
	}

	private static String handleBardFormat(long millis, boolean trailingZero) {
		return ((DecimalFormat) (trailingZero ? DateTimeFormats.REMAINING_SECONDS_TRAILING
				: DateTimeFormats.REMAINING_SECONDS).get()).format(millis * 0.001D);
	}

	public SidebarEntry add(String s) {

		if (s.length() < 10) {
			return new SidebarEntry(s);
		}

		if (s.length() > 10 && s.length() < 20) {
			return new SidebarEntry(s.substring(0, 10), s.substring(10, s.length()), "");
		}

		if (s.length() > 20) {
			return new SidebarEntry(s.substring(0, 10), s.substring(10, 20), s.substring(20, s.length()));
		}

		return null;
	}

	@Override
	public String getTitle() {
		return ConfigurationService.SCOREBOARD_TITLE;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public List<SidebarEntry> getLines(Player player) {

		List<SidebarEntry> lines = new ArrayList<SidebarEntry>();
		EOTWHandler.EotwRunnable eotwRunnable = this.plugin.getEotwHandler().getRunnable();
		PvpClass pvpClass = this.plugin.getPvpClassManager().getEquippedClass(player);
		EventTimer eventTimer = this.plugin.getTimerManager().eventTimer;
		List<SidebarEntry> conquestLines = null;
		Collection<Timer> timers = this.plugin.getTimerManager().getTimers();
		EventFaction eventFaction = eventTimer.getEventFaction();

		if ((StaffModeCommand.getInstance().isMod(player))) {
			lines.add(new SidebarEntry(ChatColor.GOLD + ChatColor.BOLD.toString() + "Staff Mode:"));
			lines.add(new SidebarEntry(ChatColor.GOLD + " » ", ChatColor.YELLOW + "Vanish" + ChatColor.GRAY + ": ",
					VanishListener.getInstance().isVanished(player) ? ChatColor.GREEN + "On" : ChatColor.RED + "Off"));
			}

		if ((ConfigurationService.KIT_MAP) == true) {
			lines.add(new SidebarEntry("",
					ChatColor.DARK_RED.toString() + "Kills" + ChatColor.GRAY + ": " + ChatColor.RED,
					player.getStatistic(Statistic.PLAYER_KILLS)));
			lines.add(new SidebarEntry("",
					ChatColor.DARK_RED.toString() + "Deaths" + ChatColor.GRAY + ": " + ChatColor.RED,
					player.getStatistic(Statistic.DEATHS)));
		}

		if ((pvpClass != null) && ((pvpClass instanceof BardClass))) {
			BardClass bardClass = (BardClass) pvpClass;
			lines.add(new SidebarEntry(ChatColor.AQUA + ChatColor.BOLD.toString() + "Bard ",
					ChatColor.AQUA + ChatColor.BOLD.toString() + "Energy", ChatColor.GRAY + ": " + ChatColor.RED
							+ handleBardFormat(bardClass.getEnergyMillis(player), true)));
			long remaining2 = bardClass.getRemainingBuffDelay(player);
			if (remaining2 > 0L) {
				lines.add(new SidebarEntry(ChatColor.GREEN + ChatColor.BOLD.toString() + "Bard ",
						ChatColor.GREEN + ChatColor.BOLD.toString() + "Effect",
						ChatColor.GRAY + ": " + ChatColor.RED + Base.getRemaining(remaining2, true)));
			}
		}
		final SotwTimer.SotwRunnable sotwRunnable = this.plugin.getSotwTimer().getSotwRunnable();
		if (sotwRunnable != null) {
			lines.add(new SidebarEntry(String.valueOf(ChatColor.GREEN.toString()) + ChatColor.BOLD, "SOTW Timer",
					ChatColor.GRAY + ": " + String.valueOf(ChatColor.RED.toString())
							+ Base.getRemaining(sotwRunnable.getRemaining(), true)));
		}

/*		if ((pvpClass instanceof MinerClass)) {
			lines.add(new SidebarEntry(ChatColor.GREEN.toString(), "Active Class",
					ChatColor.GRAY + ": " + ChatColor.RED + "Miner"));
		}

		if ((pvpClass instanceof ArcherClass)) {
			lines.add(new SidebarEntry(ChatColor.GREEN.toString(), "Active Class",
					ChatColor.GRAY + ": " + ChatColor.RED + "Archer"));
		}

		if ((pvpClass instanceof BardClass)) {
			lines.add(new SidebarEntry(ChatColor.GREEN.toString(), "Active Class",
					ChatColor.GRAY + ": " + ChatColor.RED + "Bard"));
		}

		if ((pvpClass instanceof RogueClass)) {
			lines.add(new SidebarEntry(ChatColor.GREEN.toString(), "Active Class",
					ChatColor.GRAY + ": " + ChatColor.RED + "Rogue"));
		}*/

		for (Timer timer : timers) {
			if (((timer instanceof PlayerTimer)) && (!(timer instanceof TeleportTimer))) {
				PlayerTimer playerTimer = (PlayerTimer) timer;
				long remaining3 = playerTimer.getRemaining(player);
				if (remaining3 > 0L) {
					String timerName1 = playerTimer.getName();
					if (timerName1.length() > 14) {
						timerName1 = timerName1.substring(0, timerName1.length());
					}
					lines.add(new SidebarEntry(playerTimer.getScoreboardPrefix(), timerName1 + ChatColor.GRAY,
							": " + ChatColor.RED + Base.getRemaining(remaining3, true)));
				}
			} else if ((timer instanceof GlobalTimer)) {
				GlobalTimer playerTimer2 = (GlobalTimer) timer;
				long remaining3 = playerTimer2.getRemaining();
				if (remaining3 > 0L) {
					String timerName = playerTimer2.getName();
					if (timerName.length() > 14) {
						timerName = timerName.substring(0, timerName.length());
					}
					if (!timerName.equalsIgnoreCase("Conquest")) {
						lines.add(new SidebarEntry(playerTimer2.getScoreboardPrefix(), timerName + ChatColor.GRAY,
								": " + ChatColor.RED + Base.getRemaining(remaining3, true)));
					}
				}
			}
		}

		if (eotwRunnable != null) {
			long remaining4 = eotwRunnable.getTimeUntilStarting();
			if (remaining4 > 0L) {
				lines.add(new SidebarEntry(ChatColor.DARK_RED.toString() + ChatColor.BOLD, "EOTW" + ChatColor.GRAY + "",
						"" + ChatColor.GRAY + ": " + ChatColor.RED + Base.getRemaining(remaining4, true)));
			} else if ((remaining4 = eotwRunnable.getTimeUntilCappable()) > 0L) {
				lines.add(new SidebarEntry(ChatColor.DARK_RED.toString() + ChatColor.BOLD, "EOTW" + ChatColor.GRAY + "",
						"" + ChatColor.GRAY + ": " + ChatColor.RED + Base.getRemaining(remaining4, true)));
			}
		}

		if ((eventFaction instanceof ConquestFaction)) {
			ConquestFaction conquestFaction = (ConquestFaction) eventFaction;
			CONQUEST_FORMATTER.get();
			conquestLines = new ArrayList<SidebarEntry>();
			ConquestTracker conquestTracker = (ConquestTracker) conquestFaction.getEventType().getEventTracker();
			int count = 0;
			for (Iterator<?> localIterator = conquestTracker.getFactionPointsMap().entrySet().iterator(); localIterator
					.hasNext(); count = 3) {
				Map.Entry<PlayerFaction, Integer> entry = (Map.Entry) localIterator.next();
				String factionName = ((PlayerFaction) entry.getKey()).getDisplayName(player);
				if (factionName.length() > 14) {
					factionName = factionName.substring(0, 14);
				}
				lines.add(new SidebarEntry(ChatColor.WHITE + " * " + ChatColor.RED, factionName,
						ChatColor.GRAY + ": " + ChatColor.RED + entry.getValue()));
				if (++count == 3) {
					break;
				}
			}
		}

		if ((conquestLines != null) && (!conquestLines.isEmpty())) {
			if (player.hasPermission("command.mod")) {
				conquestLines.add(new SidebarEntry("§7§m------", "------", "--------"));
			}
			conquestLines.addAll(lines);
			lines = conquestLines;
		}
		if (!lines.isEmpty()) {
			lines.add(0, new SidebarEntry(ChatColor.GRAY, ChatColor.STRIKETHROUGH + "-----------", "---------"));
			lines.add(lines.size(), new SidebarEntry(ChatColor.GRAY, NEW_LINE, "----------"));
		}
		return lines;

	}

	public static final ThreadLocal<DecimalFormat> CONQUEST_FORMATTER = new ThreadLocal<DecimalFormat>() {
		protected DecimalFormat initialValue() {
			return new DecimalFormat("00.0");
		}
	};
}
