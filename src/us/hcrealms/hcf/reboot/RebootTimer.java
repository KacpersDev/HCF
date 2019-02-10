package us.hcrealms.hcf.reboot;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import net.md_5.bungee.api.ChatColor;
import us.hcrealms.hcf.Base;
import us.hcrealms.hcf.timer.GlobalTimer;
import us.hcrealms.hcf.timer.event.TimerExpireEvent;

public class RebootTimer extends GlobalTimer implements Listener {

	private final Base plugin;
	
	public RebootTimer(Base plugin) {
		super("Reboot",  TimeUnit.SECONDS.toMillis(1L));
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onExpire(TimerExpireEvent event) {
		if (event.getTimer() == this) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
		}
	}
	
	@Override
	public String getScoreboardPrefix() {
		return ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString();
	}
	
}
