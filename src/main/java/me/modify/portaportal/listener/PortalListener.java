package me.modify.portaportal.listener;

import me.modify.portaportal.registry.PortalBlockRegistry;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PortalListener implements Listener {

    @EventHandler
    public void onWaterFlow(BlockFromToEvent event) {
        // Cancel portal blocks from flowing water / lava everywhere
        if (event.getBlock().getType() == Material.WATER || event.getBlock().getType() == Material.LAVA) {
            if (PortalBlockRegistry.getInstance().isPortal(event.getBlock().getLocation())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        // Detection for when a player steps into a portal
        Player player = event.getPlayer();
        if (player.getLocation().getBlock().getType() != Material.WATER) return;
        if (!PortalBlockRegistry.getInstance().isPortal(player.getLocation())) return;

        player.sendMessage("Teleported to Home!");
    }

}
