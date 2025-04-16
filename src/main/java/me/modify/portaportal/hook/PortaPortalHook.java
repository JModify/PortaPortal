package me.modify.portaportal.hook;

import lombok.Getter;
import me.modify.portaportal.PortaPortal;
import org.bukkit.Bukkit;

public abstract class PortaPortalHook {

    protected final String name;

    @Getter
    private boolean hooked;

    private final boolean depends;

    /**
     * Initializes a plugin hook.
     * @param name name of plugin to hook.
     */
    public PortaPortalHook(String name, boolean depends) {
        this.hooked = false;
        this.name = name;
        this.depends = depends;
    }

    public void hook() {
        PortaPortal plugin = PortaPortal.getInstance();
        if (Bukkit.getServer().getPluginManager().getPlugin(name) != null) {
            hooked = true;
            plugin.getLogger().info(name + " detected. Plugin successfully hooked.");
        } else {
            if (depends) {
                plugin.getLogger().severe("Failed to hook into dependant plugin " + name + ". Plugin shutting down.");
                Bukkit.getServer().getPluginManager().disablePlugin(plugin);
            }
        }
    }

}