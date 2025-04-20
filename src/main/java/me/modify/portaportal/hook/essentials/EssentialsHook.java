package me.modify.portaportal.hook.essentials;

import me.modify.portaportal.hook.PortaPortalHook;
import org.bukkit.Bukkit;

public class EssentialsHook extends PortaPortalHook {
    public EssentialsHook() {
        super("Essentials", false);
    }

    public Object getAPI() {
        return Bukkit.getPluginManager().getPlugin(name);
    }
}
