package me.modify.portaportal;

import lombok.Getter;
import lombok.Setter;
import me.modify.portaportal.command.AdminCommand;
import me.modify.portaportal.command.PortaPortalCommand;
import me.modify.portaportal.data.flatfile.ConfigFile;
import me.modify.portaportal.data.flatfile.MessageFile;
import me.modify.portaportal.hook.FastAsyncWorldEditHook;
import me.modify.portaportal.portal.PortalListener;
import me.modify.portaportal.listener.ProjectileListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PortaPortal extends JavaPlugin {

    @Getter @Setter
    private static PortaPortal instance;

    @Getter
    private ConfigFile configFile;

    @Getter
    private MessageFile messageFile;

    @Getter
    private final FastAsyncWorldEditHook worldEditHook = new FastAsyncWorldEditHook();

    @Override
    public void onEnable() {
        setInstance(this);

        configFile = new ConfigFile(this);
        messageFile = new MessageFile(this);

        registerCommands();
        registerListeners();

        worldEditHook.hook();
    }

    @Override
    public void onDisable() {

    }
    
    private void registerCommands() {
        PortaPortalCommand.registerCommand(new AdminCommand());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ProjectileListener(), this);
        getServer().getPluginManager().registerEvents(new PortalListener(), this);
    }
}
