package me.modify.portaportal.hook;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;

public class FastAsyncWorldEditHook extends PortaPortalHook{

    public FastAsyncWorldEditHook() {
        super("FastAsyncWorldEdit", true);
    }

    public WorldEditPlugin getAPI() {
        return (WorldEditPlugin) Bukkit.getPluginManager().getPlugin(name);
    }
}
