package me.modify.portaportal.timer.erasure;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.portal.block.PortalBlockRegistry;
import me.modify.portaportal.timer.PortaTask;
import me.modify.portaportal.timer.PortaTaskManager;
import me.modify.portaportal.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class ErasureTask extends PortaTask {

    private int time;
    private final int eraseTime;

    private final ClipboardHolder holder;
    private final EditSession session;
    private final World world;

    public ErasureTask(UUID playerId, EditSession session, ClipboardHolder holder, World world) {
        super(playerId);

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

            PortaTaskManager manager = PortaTaskManager.getInstance();
            manager.getErasureController().remove(playerId);
            manager.getErasureController().cancelTask(taskId);
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

        PortalBlockRegistry.getInstance().removeByPlayer(playerId);
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
