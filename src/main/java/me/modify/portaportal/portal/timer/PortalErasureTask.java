package me.modify.portaportal.portal.timer;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import lombok.Getter;
import lombok.Setter;
import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.portal.PortalBlockRegistry;
import me.modify.portaportal.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PortalErasureTask implements Runnable {

    @Getter @Setter
    int taskId;

    private int time;
    private final int eraseTime;

    private final UUID playerId;

    private final ClipboardHolder holder;
    private final EditSession session;
    private final World world;

    public PortalErasureTask(UUID playerId, EditSession session, ClipboardHolder holder, World world) {
        this.playerId = playerId;
        this.session = session;
        this.holder = holder;
        this.world = world;

        this.time = 0;
        this.eraseTime = PortaPortal.getInstance().getConfigFile().getYaml().getInt("portal.duration", 5);
        sendCountdown(0);
    }

    @Override
    public void run() {

        sendCountdown(1);

        time += 1;
        if (time >= eraseTime) {
            erase();

            PortalTaskManager manager = PortalTaskManager.getInstance();
            manager.removeErasureTask(this);
            manager.cancelTask(taskId);
        }
    }

    public void erase() {
        WorldEdit worldEdit = PortaPortal.getInstance().getWorldEditHook().getAPI().getWorldEdit();

        EditSession undoSession = worldEdit.newEditSession(world);
        session.undo(undoSession);

        try {
            Operations.complete(holder.createPaste(undoSession).build());
            undoSession.close();
        } catch (WorldEditException e) {
            throw new RuntimeException(e);
        }

        PortalBlockRegistry.getInstance().removePortal(playerId);
    }

    private void sendCountdown(int offset) {
        FileConfiguration fileConfiguration = PortaPortal.getInstance().getConfigFile().getYaml();
        boolean countdown = fileConfiguration.getBoolean("portal.countdown");
        if (!countdown) return;

        Player player = Bukkit.getPlayer(playerId);
        if (player == null) return;

        int remainingTime = (eraseTime - offset) - time;
        if (remainingTime == 0) return;

        Map<String, String> timePlaceholder = Map.of("%TIME%", String.valueOf(remainingTime));
        Messenger.sendMessage(player, Messenger.Type.GENERAL, "portal-countdown", timePlaceholder);
    }
}
