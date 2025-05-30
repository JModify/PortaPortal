package me.modify.portaportal.portal;

import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.portal.block.PortalBlockData;
import me.modify.portaportal.portal.block.PortalBlockRegistry;
import me.modify.portaportal.portal.schem.PortalSchematic;
import me.modify.portaportal.timer.cooldown.CooldownTask;
import me.modify.portaportal.timer.PortaTaskManager;
import me.modify.portaportal.util.Messenger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class PortalListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (!(projectile.getShooter() instanceof Player player)) return;

        if (projectile instanceof Snowball snowball) {
            if (!PortalItem.isPortalitem(snowball.getItem())) return;
        }

        if (projectile instanceof Egg egg) {
            if (!PortalItem.isPortalitem(egg.getItem())) return;
        }

        if (projectile instanceof EnderPearl pearl) {
            if (!PortalItem.isPortalitem(pearl.getItem())) return;

            NamespacedKey key = new NamespacedKey(PortaPortal.getInstance(), "block-teleport");
            player.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);
        }

        Block block = event.getHitBlock();
        if (block == null) return;

        PortalSchematic portalSchematic = new PortalSchematic();

        // Create paste location and offset Y by 1 block to be above ground.
        Location pasteLocation = block.getLocation();
        pasteLocation.setY(block.getY() + 2);

        // Check if portal is too close to another portal
        if (PortalBlockRegistry.getInstance().isCloseToPortal(pasteLocation)) {
            Messenger.sendMessage(player, Messenger.Type.ERROR, "portal-too-close");
            return;
        }

        portalSchematic.pasteFacingPlayer(player, pasteLocation);
        PortaTaskManager.getInstance().getCooldownController().add(new CooldownTask(player.getUniqueId()), 0L, 20L);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        PortaTaskManager manager = PortaTaskManager.getInstance();
        if (!manager.getCooldownController().isCooldown(player.getUniqueId())) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null) return;

        if (PortalItem.isPortalitem(item)) {
            Map<String, String> placeholders = Map.of("%TIME%",
                    String.valueOf(manager.getCooldownController().getCooldownTime(player.getUniqueId())));
            Messenger.sendMessage(player, Messenger.Type.ERROR, "portal-cooldown", placeholders);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) return;

        Player player = event.getPlayer();

        NamespacedKey key = new NamespacedKey(PortaPortal.getInstance(), "block-teleport");
        PersistentDataContainer persistentDataContainer = player.getPersistentDataContainer();

        if (persistentDataContainer.has(key, PersistentDataType.INTEGER)) {
            event.setCancelled(true);
            persistentDataContainer.remove(key);
        }
    }

    @EventHandler
    public void onWaterFlow(BlockFromToEvent event) {
        // Cancel portal blocks from flowing water / lava everywhere
        if (event.getBlock().getType() == Material.WATER || event.getBlock().getType() == Material.LAVA) {
            if (PortalBlockRegistry.getInstance().get(event.getBlock().getLocation()) != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        // Detection for when a player steps into a portal
        Player player = event.getPlayer();
        if (player.getLocation().getBlock().getType() != Material.WATER) return;

        PortalBlockData portalBlockData = PortalBlockRegistry.getInstance().get(player.getLocation());
        if (portalBlockData == null) return;


        player.teleport(portalBlockData.destination());
        Map<String, String> teleportPlaceholders = Map.of("%DESTINATION%", portalBlockData.destinationType().name());
        Messenger.sendMessage(player, Messenger.Type.GENERAL, "portal-teleport", teleportPlaceholders);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;

        Player player = event.getPlayer();
        if (player.hasPermission("portaportal.use")) return;
        if (PortalItem.isPortalitem(item)) {
            event.setCancelled(true);
            Messenger.sendMessage(player, Messenger.Type.ERROR, "insufficient-perms");
        }
    }

}
