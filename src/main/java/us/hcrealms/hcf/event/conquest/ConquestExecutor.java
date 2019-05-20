package us.hcrealms.hcf.event.conquest;

import us.hcrealms.hcf.Base;
import us.hcrealms.hcf.util.command.ArgumentExecutor;

public class ConquestExecutor
  extends ArgumentExecutor
{
  public ConquestExecutor(Base plugin)
  {
    super("conquest");
    addArgument(new ConquestSetpointsArgument(plugin));
  }
}
