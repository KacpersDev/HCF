package us.hcrealms.hcf.combatlog;

import java.util.UUID;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

import us.hcrealms.hcf.Base;

public abstract interface LoggerEntity
{
    public abstract void postSpawn(Base paramHCF);

    public abstract CraftPlayer getBukkitEntity();

    public abstract UUID getUniqueID();

    public abstract void destroy();
}
