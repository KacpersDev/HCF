package us.hcrealms.hcf;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import net.minecraft.util.com.google.common.base.Joiner;
import us.hcrealms.hcf.balance.EconomyCommand;
import us.hcrealms.hcf.balance.EconomyManager;
import us.hcrealms.hcf.balance.FlatFileEconomyManager;
import us.hcrealms.hcf.balance.PayCommand;
import us.hcrealms.hcf.balance.ShopSignListener;
import us.hcrealms.hcf.classes.PvpClassManager;
import us.hcrealms.hcf.classes.archer.ArcherClass;
import us.hcrealms.hcf.classes.bard.BardRestorer;
import us.hcrealms.hcf.classes.type.RogueClass;
import us.hcrealms.hcf.ConfigurationService;
import us.hcrealms.hcf.combatlog.CombatLogListener;
import us.hcrealms.hcf.combatlog.CustomEntityRegistration;
import us.hcrealms.hcf.commands.BroadcastCommand;
import us.hcrealms.hcf.commands.ClearCommand;
import us.hcrealms.hcf.commands.CobbleCommand;
import us.hcrealms.hcf.commands.CoordsCommand;
import us.hcrealms.hcf.commands.CrowbarCommand;
import us.hcrealms.hcf.commands.DiamondReviveCommand;
import us.hcrealms.hcf.commands.EnchantCommand;
import us.hcrealms.hcf.commands.EndPortalCommand;
import us.hcrealms.hcf.commands.FFACommand;
import us.hcrealms.hcf.commands.FeedCommand;
import us.hcrealms.hcf.commands.FixCommand;
import us.hcrealms.hcf.commands.FlyCommand;
import us.hcrealms.hcf.commands.FreezeCommand;
import us.hcrealms.hcf.commands.GMCCommand;
import us.hcrealms.hcf.commands.GMSCommand;
import us.hcrealms.hcf.commands.GameModeCommand;
import us.hcrealms.hcf.commands.GiveCommand;
import us.hcrealms.hcf.commands.GodCommand;
import us.hcrealms.hcf.commands.GoppleCommand;
import us.hcrealms.hcf.commands.HatCommand;
import us.hcrealms.hcf.commands.HealCommand;
import us.hcrealms.hcf.commands.HelpCommand;
import us.hcrealms.hcf.commands.InvSeeCommand;
import us.hcrealms.hcf.commands.ItemCommand;
import us.hcrealms.hcf.commands.KillCommand;
import us.hcrealms.hcf.commands.KingReviveCommand;
import us.hcrealms.hcf.commands.LFFCommand;
import us.hcrealms.hcf.commands.LagCommand;
import us.hcrealms.hcf.commands.ListCommand;
import us.hcrealms.hcf.commands.LockdownCommand;
import us.hcrealms.hcf.commands.LogoutCommand;
import us.hcrealms.hcf.commands.MapKitCommand;
import us.hcrealms.hcf.commands.MessageCommand;
import us.hcrealms.hcf.commands.MiscCommands;
import us.hcrealms.hcf.commands.MoreCommand;
import us.hcrealms.hcf.commands.OreStatsCommand;
import us.hcrealms.hcf.commands.PanicCommand;
import us.hcrealms.hcf.commands.PingCommand;
import us.hcrealms.hcf.commands.PlayTimeCommand;
import us.hcrealms.hcf.commands.PlayerVaultCommand;
import us.hcrealms.hcf.commands.PvpTimerCommand;
import us.hcrealms.hcf.commands.RandomCommand;
import us.hcrealms.hcf.commands.RefundCommand;
import us.hcrealms.hcf.commands.RenameCommand;
import us.hcrealms.hcf.commands.ReplyCommand;
import us.hcrealms.hcf.commands.ResetCommand;
import us.hcrealms.hcf.commands.SetBorderCommand;
import us.hcrealms.hcf.commands.SetCommand;
import us.hcrealms.hcf.commands.SetKBCommand;
import us.hcrealms.hcf.commands.SkullCommand;
import us.hcrealms.hcf.commands.SpawnCommand;
import us.hcrealms.hcf.commands.SpawnerCommand;
import us.hcrealms.hcf.commands.StaffModeCommand;
import us.hcrealms.hcf.commands.StatsCommand;
import us.hcrealms.hcf.commands.SudoCommand;
import us.hcrealms.hcf.commands.TLCommand;
import us.hcrealms.hcf.commands.TeamspeakCommand;
import us.hcrealms.hcf.commands.TeleportAllCommand;
import us.hcrealms.hcf.commands.TeleportCommand;
import us.hcrealms.hcf.commands.TeleportHereCommand;
import us.hcrealms.hcf.commands.ToggleMessageCommand;
import us.hcrealms.hcf.commands.TopCommand;
import us.hcrealms.hcf.commands.VanishCommand;
import us.hcrealms.hcf.commands.WhoisCommand;
import us.hcrealms.hcf.commands.WorldCommand;
import us.hcrealms.hcf.config.PlayerData;
import us.hcrealms.hcf.config.PotionLimiterData;
import us.hcrealms.hcf.config.WorldData;
import us.hcrealms.hcf.deathban.Deathban;
import us.hcrealms.hcf.deathban.DeathbanListener;
import us.hcrealms.hcf.deathban.DeathbanManager;
import us.hcrealms.hcf.deathban.FlatFileDeathbanManager;
import us.hcrealms.hcf.deathban.lives.LivesExecutor;
import us.hcrealms.hcf.deathban.lives.StaffReviveCommand;
import us.hcrealms.hcf.event.CaptureZone;
import us.hcrealms.hcf.event.EventExecutor;
import us.hcrealms.hcf.event.EventScheduler;
import us.hcrealms.hcf.event.conquest.ConquestExecutor;
import us.hcrealms.hcf.event.eotw.EOTWHandler;
import us.hcrealms.hcf.event.eotw.EotwCommand;
import us.hcrealms.hcf.event.eotw.EotwListener;
import us.hcrealms.hcf.event.faction.CapturableFaction;
import us.hcrealms.hcf.event.faction.ConquestFaction;
import us.hcrealms.hcf.event.faction.KothFaction;
import us.hcrealms.hcf.event.glmountain.GlowstoneMountain;
import us.hcrealms.hcf.event.koth.KothExecutor;
import us.hcrealms.hcf.faction.FactionExecutor;
import us.hcrealms.hcf.faction.FactionManager;
import us.hcrealms.hcf.faction.FactionMember;
import us.hcrealms.hcf.faction.FlatFileFactionManager;
import us.hcrealms.hcf.faction.claim.Claim;
import us.hcrealms.hcf.faction.claim.ClaimHandler;
import us.hcrealms.hcf.faction.claim.ClaimWandListener;
import us.hcrealms.hcf.faction.claim.Subclaim;
import us.hcrealms.hcf.faction.type.ClaimableFaction;
import us.hcrealms.hcf.faction.type.EndPortalFaction;
import us.hcrealms.hcf.faction.type.Faction;
import us.hcrealms.hcf.faction.type.GlowstoneFaction;
import us.hcrealms.hcf.faction.type.PlayerFaction;
import us.hcrealms.hcf.faction.type.RoadFaction;
import us.hcrealms.hcf.faction.type.SpawnFaction;
import us.hcrealms.hcf.inventory.implementation.ClaimSettingsInventory;
import us.hcrealms.hcf.listener.AutoSmeltOreListener;
import us.hcrealms.hcf.listener.BookDeenchantListener;
import us.hcrealms.hcf.listener.BorderListener;
import us.hcrealms.hcf.listener.BottledExpListener;
import us.hcrealms.hcf.listener.ChatListener;
import us.hcrealms.hcf.listener.CoreListener;
import us.hcrealms.hcf.listener.CrowbarListener;
import us.hcrealms.hcf.listener.DeathListener;
import us.hcrealms.hcf.listener.DeathMessageListener;
import us.hcrealms.hcf.listener.ElevatorListener;
import us.hcrealms.hcf.listener.EnderPearlFix;
import us.hcrealms.hcf.listener.EntityLimitListener;
import us.hcrealms.hcf.listener.ExpMultiplierListener;
import us.hcrealms.hcf.listener.FactionListener;
import us.hcrealms.hcf.listener.FactionsCoreListener;
import us.hcrealms.hcf.listener.FoundDiamondsListener;
import us.hcrealms.hcf.listener.FurnaceSmeltSpeederListener;
import us.hcrealms.hcf.listener.GodListener;
import us.hcrealms.hcf.listener.KitMapListener;
import us.hcrealms.hcf.listener.LoginEvent;
import us.hcrealms.hcf.listener.OreCountListener;
import us.hcrealms.hcf.listener.PearlGlitchListener;
import us.hcrealms.hcf.listener.PlayTimeManager;
import us.hcrealms.hcf.listener.PotionLimitListener;
import us.hcrealms.hcf.listener.SignSubclaimListener;
import us.hcrealms.hcf.listener.SkullListener;
import us.hcrealms.hcf.listener.StaffModeListener;
import us.hcrealms.hcf.listener.UnRepairableListener;
import us.hcrealms.hcf.listener.VanishListener;
import us.hcrealms.hcf.listener.WorldListener;
import us.hcrealms.hcf.listener.fixes.ArmorFixListener;
import us.hcrealms.hcf.listener.fixes.BeaconStrengthFixListener;
import us.hcrealms.hcf.listener.fixes.BlockHitFixListener;
import us.hcrealms.hcf.listener.fixes.BlockJumpGlitchFixListener;
import us.hcrealms.hcf.listener.fixes.BoatGlitchFixListener;
import us.hcrealms.hcf.listener.fixes.BookQuillFixListener;
import us.hcrealms.hcf.listener.fixes.CommandBlocker;
import us.hcrealms.hcf.listener.fixes.DupeGlitchFix;
import us.hcrealms.hcf.listener.fixes.EnchantLimitListener;
import us.hcrealms.hcf.listener.fixes.EnderChestRemovalListener;
import us.hcrealms.hcf.listener.fixes.HungerFixListener;
import us.hcrealms.hcf.listener.fixes.InfinityArrowFixListener;
import us.hcrealms.hcf.listener.fixes.NaturalMobSpawnFixListener;
import us.hcrealms.hcf.listener.fixes.PexCrashFixListener;
import us.hcrealms.hcf.listener.fixes.PortalListener;
import us.hcrealms.hcf.listener.fixes.StrengthListener;
import us.hcrealms.hcf.listener.fixes.SyntaxBlocker;
import us.hcrealms.hcf.listener.fixes.VoidGlitchFixListener;
import us.hcrealms.hcf.listener.fixes.WeatherFixListener;
import us.hcrealms.hcf.reboot.RebootCommand;
import us.hcrealms.hcf.reboot.RebootListener;
import us.hcrealms.hcf.sale.SaleCommand;
import us.hcrealms.hcf.sale.SaleListener;
import us.hcrealms.hcf.scoreboard.ScoreboardHandler;
import us.hcrealms.hcf.signs.EventSignListener;
import us.hcrealms.hcf.signs.KitSignListener;
import us.hcrealms.hcf.sotw.SotwCommand;
import us.hcrealms.hcf.sotw.SotwListener;
import us.hcrealms.hcf.sotw.SotwTimer;
import us.hcrealms.hcf.stattracker.OreTrackerListener;
import us.hcrealms.hcf.stattracker.StatTrackListener;
import us.hcrealms.hcf.timer.TimerExecutor;
import us.hcrealms.hcf.timer.TimerManager;
import us.hcrealms.hcf.user.ConsoleUser;
import us.hcrealms.hcf.user.FactionUser;
import us.hcrealms.hcf.user.UserManager;
import us.hcrealms.hcf.util.SignHandler;
import us.hcrealms.hcf.util.itemdb.ItemDb;
import us.hcrealms.hcf.util.itemdb.SimpleItemDb;
import us.hcrealms.hcf.visualise.ProtocolLibHook;
import us.hcrealms.hcf.visualise.VisualiseHandler;
import us.hcrealms.hcf.visualise.WallBorderListener;

public class Base extends JavaPlugin implements CommandExecutor {

	private CombatLogListener combatLogListener;
	
	public CombatLogListener getCombatLogListener() {
		return this.combatLogListener;
	}
		
	@Getter
	private Chat chat;
	
	@Getter
	private ClaimSettingsInventory claimSettings;
	
	public void onEnable() {
		plugin = this;

		BasePlugins.getPlugin().init(this);
		config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();
		conf = new File(getDataFolder(), "config.yml");
		WorldData.getInstance().setup(this);
		PlayerData.getInstance().setup(this);
		PotionLimiterData.getInstance().setup(this);
		Bukkit.getConsoleSender()
				.sendMessage(ChatColor.GREEN.toString() + ChatColor.ITALIC + "HCRealms is now enabled.");
		ProtocolLibHook.hook(this);
		CustomEntityRegistration.registerCustomEntities();
		Plugin wep = Bukkit.getPluginManager().getPlugin("WorldEdit");
		this.craftBukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		this.worldEdit = (((wep instanceof WorldEditPlugin)) && (wep.isEnabled()) ? (WorldEditPlugin) wep : null);
		
		registerConfiguration();
		registerCommands();
		registerManagers();
		registerListeners();
		this.claimSettings = new ClaimSettingsInventory(this);
		
		Cooldowns.createCooldown("king_cooldown");
		Cooldowns.createCooldown("diamond_cooldown");
		Cooldowns.createCooldown("emerald_cooldown");
		Cooldowns.createCooldown("ruby_cooldown");
		Cooldowns.createCooldown("gold_cooldown");
		Cooldowns.createCooldown("archer_speed_cooldown");
		Cooldowns.createCooldown("archer_jump_cooldown");
		Cooldowns.createCooldown("rogue_speed_cooldown");
		Cooldowns.createCooldown("rogue_jump_cooldown");
		Cooldowns.createCooldown("rogue_cooldown");
		Cooldowns.createCooldown("lff_cooldown");
		
		new BukkitRunnable() {
			public void run() {
				Base.this.saveData();
				getServer().broadcastMessage(ChatColor.GREEN + "Saving data...");
			}
		}.runTaskTimerAsynchronously(this, TimeUnit.SECONDS.toMillis(20L), TimeUnit.SECONDS.toMillis(20L));
		
	    new BukkitRunnable() {

	        @Override
	        public void run() {
	          String players = Arrays.stream(Bukkit.getOnlinePlayers())
	              .filter(player -> player.hasPermission("rank.king")).map(Player::getName)
	              .collect(Collectors.joining(", "));

	          getServer().broadcastMessage(ChatColor.YELLOW + "King Users " + ChatColor.GOLD + "» " + ChatColor.WHITE + players);
	          getServer().broadcastMessage(ChatColor.GRAY + "Purchase the King rank at shop.hcrealms.us");	        }
	        
	      }.runTaskTimer(this, 60L, 20 * 60 * 5);
	}

	private void saveData() {
		this.combatLogListener.removeCombatLoggers();
		this.deathbanManager.saveDeathbanData();
		this.economyManager.saveEconomyData();
		this.factionManager.saveFactionData();
		this.playTimeManager.savePlaytimeData();
		this.userManager.saveUserData();
		this.signHandler.cancelTasks(null);

		PlayerData.getInstance().saveConfig();
	}

	public void onDisable() {
		this.pvpClassManager.onDisable();
		this.scoreboardHandler.clearBoards();
		this.deathbanManager.saveDeathbanData();
		this.economyManager.saveEconomyData();
		this.factionManager.saveFactionData();
		this.playTimeManager.savePlaytimeData();
		this.userManager.saveUserData();
		StaffModeCommand.onDisableMod();
		saveData();
		plugin = null;
	}

	private void registerConfiguration() {
		ConfigurationSerialization.registerClass(CaptureZone.class);
		ConfigurationSerialization.registerClass(Deathban.class);
		ConfigurationSerialization.registerClass(Claim.class);
		ConfigurationSerialization.registerClass(ConsoleUser.class);
		ConfigurationSerialization.registerClass(Subclaim.class);
		ConfigurationSerialization.registerClass(FactionUser.class);
		ConfigurationSerialization.registerClass(ClaimableFaction.class);
		ConfigurationSerialization.registerClass(ConquestFaction.class);
		ConfigurationSerialization.registerClass(CapturableFaction.class);
		ConfigurationSerialization.registerClass(KothFaction.class);
		ConfigurationSerialization.registerClass(GlowstoneFaction.class);
		ConfigurationSerialization.registerClass(EndPortalFaction.class);
		ConfigurationSerialization.registerClass(Faction.class);
		ConfigurationSerialization.registerClass(FactionMember.class);
		ConfigurationSerialization.registerClass(PlayerFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.class);
		ConfigurationSerialization.registerClass(SpawnFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.NorthRoadFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.EastRoadFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.SouthRoadFaction.class);
		ConfigurationSerialization.registerClass(RoadFaction.WestRoadFaction.class);
	}

	private void registerListeners() {
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(new OreStatsCommand(), this);
		manager.registerEvents(new GodListener(), this);
		manager.registerEvents(new VanishListener(), this);
		manager.registerEvents(new ArcherClass(this), this);
		manager.registerEvents(new RogueClass(this), this);
		manager.registerEvents(new PotionLimitListener(this), this);
		manager.registerEvents(new LoginEvent(), this);
		manager.registerEvents(new DupeGlitchFix(), this);
		manager.registerEvents(new PortalListener(this), this);
		manager.registerEvents(new WeatherFixListener(), this);
		manager.registerEvents(this.combatLogListener = new CombatLogListener(this), this);
		manager.registerEvents(new NaturalMobSpawnFixListener(), this);
		manager.registerEvents(new AutoSmeltOreListener(), this);
		manager.registerEvents(new BlockHitFixListener(), this);
		manager.registerEvents(new BlockJumpGlitchFixListener(), this);
		manager.registerEvents(new CommandBlocker(), this);
		manager.registerEvents(new BoatGlitchFixListener(), this);
		manager.registerEvents(new BookDeenchantListener(), this);
		manager.registerEvents(new PexCrashFixListener(this), this);
		manager.registerEvents(new BookQuillFixListener(this), this);
		manager.registerEvents(new BorderListener(), this);
		manager.registerEvents(new ChatListener(this), this);
		manager.registerEvents(new ClaimWandListener(this), this);
		manager.registerEvents(new BottledExpListener(), this);
		manager.registerEvents(new CoreListener(this), this);
		manager.registerEvents(new CrowbarListener(this), this);
		manager.registerEvents(new DeathListener(this), this);
		manager.registerEvents(new ElevatorListener(this), this);
		manager.registerEvents(new DeathMessageListener(this), this);
	//	if (ConfigurationService.KIT_MAP == false) {
		
	//  manager.registerEvents(new DeathbanListener(this), this);
	//	
	//	}
		manager.registerEvents(new EnchantLimitListener(), this);
		manager.registerEvents(new EnderChestRemovalListener(), this);
		manager.registerEvents(new FlatFileFactionManager(this), this);
		manager.registerEvents(new StrengthListener(), this);
		manager.registerEvents(new ArmorFixListener(), this);
		manager.registerEvents(new EotwListener(this), this);
		manager.registerEvents(new EventSignListener(), this);
		manager.registerEvents(new ExpMultiplierListener(), this);
		manager.registerEvents(new FactionListener(this), this);
		manager.registerEvents(new FoundDiamondsListener(this), this);
		manager.registerEvents(new FurnaceSmeltSpeederListener(), this);
		manager.registerEvents(new KitMapListener(this), this);
		manager.registerEvents(new InfinityArrowFixListener(), this);
		manager.registerEvents(new HungerFixListener(), this);
		manager.registerEvents(new PearlGlitchListener(this), this);
		manager.registerEvents(new FactionsCoreListener(this), this);
		manager.registerEvents(new PearlGlitchListener(this), this);
		manager.registerEvents(new EnderPearlFix(this), this);
		manager.registerEvents(new SignSubclaimListener(this), this);
		manager.registerEvents(new EndPortalCommand(getPlugin()), this);
		manager.registerEvents(new ShopSignListener(this), this);
		manager.registerEvents(new SkullListener(), this);
		manager.registerEvents(new BeaconStrengthFixListener(this), this);
		manager.registerEvents(new VoidGlitchFixListener(), this);
		manager.registerEvents(new WallBorderListener(this), this);
		manager.registerEvents(this.playTimeManager, this);
		manager.registerEvents(new WorldListener(this), this);
		manager.registerEvents(new UnRepairableListener(), this);
		manager.registerEvents(new StaffModeListener(), this);
		manager.registerEvents(new SyntaxBlocker(), this);
		manager.registerEvents(new OreTrackerListener(), this);
		manager.registerEvents(new OreCountListener(this), this);
		manager.registerEvents(new SotwListener(this), this);
		manager.registerEvents(new KitSignListener(), this);
		manager.registerEvents(new RebootListener(), this);
		manager.registerEvents(new SaleListener(), this);
		manager.registerEvents(new EntityLimitListener(this), this);
		manager.registerEvents(new StatTrackListener(), this);
	}

	private void registerCommands() {

		getCommand("top").setExecutor(new TopCommand());
		getCommand("list").setExecutor(new ListCommand());
		getCommand("setborder").setExecutor(new SetBorderCommand());
		getCommand("hat").setExecutor(new HatCommand());
		getCommand("world").setExecutor(new WorldCommand());
		getCommand("endportal").setExecutor(new EndPortalCommand(getPlugin()));
		getCommand("fix").setExecutor(new FixCommand());
		getCommand("setkb").setExecutor(new SetKBCommand());
		getCommand("enchant").setExecutor(new EnchantCommand());
		getCommand("freeze").setExecutor(new FreezeCommand(this));
		getCommand("staffrevive").setExecutor(new StaffReviveCommand(this));
		getCommand("lag").setExecutor(new LagCommand());
		getCommand("broadcast").setExecutor(new BroadcastCommand());
		getCommand("togglemessage").setExecutor(new ToggleMessageCommand());
		getCommand("king").setExecutor(new KingReviveCommand(this));
		getCommand("reply").setExecutor(new ReplyCommand());
		getCommand("message").setExecutor(new MessageCommand());
		getCommand("feed").setExecutor(new FeedCommand());
		getCommand("pv").setExecutor(new PlayerVaultCommand(this));
		getCommand("setspawn").setExecutor(new SpawnCommand());
		getCommand("ping").setExecutor(new PingCommand());
		getCommand("togglemessage").setExecutor(new ToggleMessageCommand());
		getCommand("teleportall").setExecutor(new TeleportAllCommand());
		getCommand("teleporthere").setExecutor(new TeleportHereCommand());
		getCommand("give").setExecutor(new GiveCommand());
		getCommand("gamemode").setExecutor(new GameModeCommand());
		getCommand("item").setExecutor(new ItemCommand());
		getCommand("lockdown").setExecutor(new LockdownCommand(this));
		getCommand("fly").setExecutor(new FlyCommand());
		getCommand("invsee").setExecutor(new InvSeeCommand(this));
		getCommand("god").setExecutor(new GodCommand());
		getCommand("gms").setExecutor(new GMSCommand());
		getCommand("gmc").setExecutor(new GMCCommand());
		getCommand("vanish").setExecutor(new VanishCommand());
		getCommand("sotw").setExecutor(new SotwCommand(this));
		getCommand("random").setExecutor(new RandomCommand(this));
		getCommand("conquest").setExecutor(new ConquestExecutor(this));
		getCommand("crowbar").setExecutor(new CrowbarCommand());
		getCommand("economy").setExecutor(new EconomyCommand(this));
		getCommand("eotw").setExecutor(new EotwCommand(this));
		getCommand("event").setExecutor(new EventExecutor(this));
		getCommand("faction").setExecutor(new FactionExecutor(this));
		getCommand("playtime").setExecutor(new PlayTimeCommand(this));
		getCommand("gopple").setExecutor(new GoppleCommand(this));
		getCommand("cobble").setExecutor(new CobbleCommand());
		getCommand("koth").setExecutor(new KothExecutor(this));
		getCommand("lives").setExecutor(new LivesExecutor(this));
		getCommand("logout").setExecutor(new LogoutCommand(this));
		getCommand("more").setExecutor(new MoreCommand());
		getCommand("panic").setExecutor(new PanicCommand());
		getCommand("heal").setExecutor(new HealCommand());
		getCommand("pay").setExecutor(new PayCommand(this));
		getCommand("pvptimer").setExecutor(new PvpTimerCommand(this));
		getCommand("LFF").setExecutor(new LFFCommand(this));
		getCommand("refund").setExecutor(new RefundCommand());
		getCommand("spawn").setExecutor(new SpawnCommand());
		getCommand("FFA").setExecutor(new FFACommand());
		getCommand("timer").setExecutor(new TimerExecutor(this));
		getCommand("kill").setExecutor(new KillCommand());
		getCommand("ores").setExecutor(new OreStatsCommand());
		getCommand("help").setExecutor(new HelpCommand());
		getCommand("rename").setExecutor(new RenameCommand());
		getCommand("teamspeak").setExecutor(new TeamspeakCommand());
		getCommand("coords").setExecutor(new CoordsCommand());
		getCommand("fsay").setExecutor(new MiscCommands());
		getCommand("mapkit").setExecutor(new MapKitCommand(this));
		getCommand("diamond").setExecutor(new DiamondReviveCommand(this));
		getCommand("emerald").setExecutor(new DiamondReviveCommand(this));
		getCommand("gold").setExecutor(new DiamondReviveCommand(this));
		getCommand("ruby").setExecutor(new DiamondReviveCommand(this));
		getCommand("staffmode").setExecutor(new StaffModeCommand());
		getCommand("spawner").setExecutor(new SpawnerCommand());
		getCommand("set").setExecutor(new SetCommand(this));
		getCommand("ci").setExecutor(new ClearCommand());
		getCommand("drop").setExecutor(new MiscCommands());
		getCommand("copyinv").setExecutor(new MiscCommands());
		getCommand("teleport").setExecutor(new TeleportCommand());
		getCommand("skull").setExecutor(new SkullCommand());
		getCommand("reset").setExecutor(new ResetCommand());
		getCommand("sudo").setExecutor(new SudoCommand());
		getCommand("whois").setExecutor(new WhoisCommand(this));
		getCommand("tl").setExecutor(new TLCommand());
		getCommand("reboot").setExecutor(new RebootCommand(plugin));
		getCommand("sale").setExecutor(new SaleCommand(plugin));
		getCommand("stats").setExecutor(new StatsCommand());
		getCommand("glowstone").setExecutor(new GlowstoneMountain(this));

		Map<String, Map<String, Object>> map = getDescription().getCommands();
		for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
			PluginCommand command = getCommand((String) entry.getKey());
			command.setPermission("command." + (String) entry.getKey());
			command.setPermissionMessage(ChatColor.RED.toString() + "You do not have permission to use this command.");
		}
	}

	private void registerManagers() {
		this.claimHandler = new ClaimHandler(this);
		this.deathbanManager = new FlatFileDeathbanManager(this);
		this.economyManager = new FlatFileEconomyManager(this);
		this.eotwHandler = new EOTWHandler(this);
		this.eventScheduler = new EventScheduler(this);
		this.factionManager = new FlatFileFactionManager(this);
		this.itemDb = new SimpleItemDb(this);
		this.playTimeManager = new PlayTimeManager(this);
		this.pvpClassManager = new PvpClassManager(this);
		this.timerManager = new TimerManager(this);
		this.scoreboardHandler = new ScoreboardHandler(this);
		this.userManager = new UserManager(this);
		this.visualiseHandler = new VisualiseHandler();
		this.sotwTimer = new SotwTimer();
		this.message = new Message(this);
		this.signHandler = new SignHandler(this);
		new BardRestorer(this);
	}

	public Message getMessage() {
		return this.message;
	}

	public ItemDb getItemDb() {
		return this.itemDb;
	}

	public Random getRandom() {
		return this.random;
	}

	public PlayTimeManager getPlayTimeManager() {
		return this.playTimeManager;
	}

	public WorldEditPlugin getWorldEdit() {
		return this.worldEdit;
	}

	public ClaimHandler getClaimHandler() {
		return this.claimHandler;
	}

	public SotwTimer getSotwTimer() {
		return this.sotwTimer;
	}

	public SignHandler getSignHandler() {
		return this.signHandler;
	}

	public ConfigurationService getConfiguration() {
		return this.configuration;
	}

	public DeathbanManager getDeathbanManager() {
		return this.deathbanManager;
	}

	public VanishListener getVanish() {
		return this.vanish;
	}

	public EconomyManager getEconomyManager() {
		return this.economyManager;
	}

	public EOTWHandler getEotwHandler() {
		return this.eotwHandler;
	}

	public FactionManager getFactionManager() {
		return this.factionManager;
	}

	public PvpClassManager getPvpClassManager() {
		return this.pvpClassManager;
	}

	public ScoreboardHandler getScoreboardHandler() {
		return this.scoreboardHandler;
	}

	public TimerManager getTimerManager() {
		return this.timerManager;
	}

	public UserManager getUserManager() {
		return this.userManager;
	}

	public VisualiseHandler getVisualiseHandler() {
		return this.visualiseHandler;
	}

	public Base() {
		this.random = new Random();
	}

	public ServerHandler getServerHandler() {
		return this.serverHandler;
	}

	public static Base getPlugin() {
		return plugin;
	}

	public static Base getInstance() {
		return instance;
	}

	public static String getReaming(long millis) {
		return getRemaining(millis, true, true);
	}

	public String getCraftBukkitVersion() {
		return this.craftBukkitVersion;
	}

	public static String getRemaining(long millis, boolean milliseconds) {
		return getRemaining(millis, milliseconds, true);
	}

	public static String getRemaining(long duration, boolean milliseconds, boolean trail) {
		if ((milliseconds) && (duration < MINUTE)) {
			return ((DecimalFormat) (trail ? DateTimeFormats.REMAINING_SECONDS_TRAILING
					: DateTimeFormats.REMAINING_SECONDS).get()).format(duration * 0.001D) + 's';
		}
		return DurationFormatUtils.formatDuration(duration, (duration >= HOUR ? "HH:" : "") + "mm:ss");
	}

	public static File conf;
	public static FileConfiguration config;
	private String craftBukkitVersion;
	public static Base instance;
	private ConfigurationService configuration;
	private static final long MINUTE = TimeUnit.MINUTES.toMillis(1L);
	private static final long HOUR = TimeUnit.HOURS.toMillis(1L);
	private static Base plugin;
	public static Plugin pl;
	private ServerHandler serverHandler;
	public BukkitRunnable clearEntityHandler;
	public BukkitRunnable announcementTask;
	private Message message;

	public EventScheduler eventScheduler;
	public static final Joiner SPACE_JOINER = Joiner.on(' ');
	public static final Joiner COMMA_JOINER = Joiner.on(", ");
	private Random random;
	private PlayTimeManager playTimeManager;
	private WorldEditPlugin worldEdit;
	private ClaimHandler claimHandler;
	private ItemDb itemDb;

	private DeathbanManager deathbanManager;
	private EconomyManager economyManager;
	private EOTWHandler eotwHandler;
	private FactionManager factionManager;
	private PvpClassManager pvpClassManager;
	private VanishListener vanish;
	private ScoreboardHandler scoreboardHandler;
	private SotwTimer sotwTimer;
	private TimerManager timerManager;
	private UserManager userManager;
	private VisualiseHandler visualiseHandler;
	private SignHandler signHandler;

}
