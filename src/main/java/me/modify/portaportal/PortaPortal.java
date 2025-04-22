package me.modify.portaportal;

import lombok.Getter;
import lombok.Setter;
import me.modify.portaportal.command.AdminCommand;
import me.modify.portaportal.command.PortaPortalCommand;
import me.modify.portaportal.data.flatfile.ConfigFile;
import me.modify.portaportal.data.flatfile.MessageFile;
import me.modify.portaportal.hook.essentials.EssentialsHook;
import me.modify.portaportal.hook.WorldEditHook;
import me.modify.portaportal.portal.PortalListener;
import me.modify.portaportal.portal.timer.PortalTaskManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class PortaPortal extends JavaPlugin {

    @Getter @Setter
    private static PortaPortal instance;

    @Getter
    private ConfigFile configFile;

    @Getter
    private MessageFile messageFile;

    @Getter
    private final WorldEditHook worldEditHook = new WorldEditHook();

    @Getter
    private final EssentialsHook essentialsHook = new EssentialsHook();

    @Override
    public void onEnable() {
        setInstance(this);

        configFile = new ConfigFile(this);
        messageFile = new MessageFile(this);

        registerCommands();
        registerListeners();

        worldEditHook.hook();
        essentialsHook.hook();
    }

    @Override
    public void onDisable() {
        PortalTaskManager.getInstance().shutdown();
    }
    
    private void registerCommands() {
        PortaPortalCommand.registerCommand(new AdminCommand());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PortalListener(), this);
    }
}
