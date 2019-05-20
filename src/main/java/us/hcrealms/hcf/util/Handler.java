package us.hcrealms.hcf.util;

import us.hcrealms.hcf.Base;

public class Handler
{
  private Base plugin;
  
  public Handler(Base paramNotorious) {
    this.plugin = paramNotorious;
  }
  
  public void enable() {}
  
  public void disable() {}
  
  public Base getInstance() {
    return this.plugin;
  }
}
