package us.hcrealms.hcf.sotw;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import us.hcrealms.hcf.Base;
import us.hcrealms.hcf.ConfigurationService;

public class SotwListener implements Listener {

    private final Base plugin;

    public SotwListener(Base plugin) {
        this.plugin = plugin;
    }

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent e)
	{
	  if (e.isCancelled()) {
		  return;
	  }
	  Entity entity = e.getEntity();
      if ((entity instanceof Player)) {
      if (plugin.getSotwTimer().getRemaining() > 0) {
	      e.setCancelled(true);
	  }
	 }
    }
	
/*    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event){
        if(plugin.getSotwTimer().getRemaining() > 0){
        	if (Bukkit.getWorld("world_nether").getEnvironment() == World.Environment.NETHER)
        	{
        		return;
        	}
        	if (Bukkit.getWorld("world_the_end").getEnvironment() == World.Environment.THE_END)
        	{
        		return;
        	}
        	
            event.getItemDrop().remove();
        }
       
    }*/
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		if (plugin.getSotwTimer().getRemaining() > 0) {
			if (player.getWorld().getEnvironment() == Environment.THE_END) {
				if (player.getLocation().getBlockY() <= -30)
					player.teleport(plugin.getServer().getWorld("world").getSpawnLocation().clone().add(0.5, 0.5, 0.5));
			}
		}

	}
}
