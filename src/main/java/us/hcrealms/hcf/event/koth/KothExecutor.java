package us.hcrealms.hcf.event.koth;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import us.hcrealms.hcf.Base;
import us.hcrealms.hcf.event.koth.argument.KothNextArgument;
import us.hcrealms.hcf.event.koth.argument.KothScheduleArgument;
import us.hcrealms.hcf.event.koth.argument.KothSetCapDelayArgument;
import us.hcrealms.hcf.event.koth.argument.KothShowArgument;
import us.hcrealms.hcf.util.command.ArgumentExecutor;

public class KothExecutor
  extends ArgumentExecutor
{
  private final KothScheduleArgument kothScheduleArgument;
  
  public KothExecutor(Base plugin)
  {
    super("koth");
    addArgument(new KothNextArgument(plugin));
    addArgument(new KothShowArgument());
    addArgument(this.kothScheduleArgument = new KothScheduleArgument(plugin));
    addArgument(new KothSetCapDelayArgument(plugin));
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    if (args.length < 1)
    {
      this.kothScheduleArgument.onCommand(sender, command, label, args);
      return true;
    }
    return super.onCommand(sender, command, label, args);
  }
}
