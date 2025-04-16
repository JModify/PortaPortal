package me.modify.portaportal.hook;

import com.earth2me.essentials.Essentials;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;

public class EssentialsHook extends PortaPortalHook {
    public EssentialsHook() {
        super("Essentials", false);
    }

    public Essentials getAPI() {
        return (Essentials) Bukkit.getPluginManager().getPlugin(name);
    }
}
