package me.modify.portaportal.hook;

import com.fastasyncworldedit.core.Fawe;
import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.WorldEdit;
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
