package me.modify.portaportal.hook;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class WorldEditHook extends PortaPortalHook{

    public WorldEditHook() {
        super("WorldEdit", true);
    }

    public WorldEditPlugin getAPI() {
        return (WorldEditPlugin) Bukkit.getPluginManager().getPlugin(name);
    }
}
