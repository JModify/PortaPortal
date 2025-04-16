package me.modify.portaportal.hook;

import org.bukkit.Bukkit;

public class EssentialsHook extends PortaPortalHook {
    public EssentialsHook() {
        super("Essentials", false);
    }

    public Object getAPI() {
        return Bukkit.getPluginManager().getPlugin(name);
    }
}
