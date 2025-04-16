package me.modify.portaportal.portal;

import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.util.ColorFormat;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class PortalItem extends ItemStack {

    private static final String tag = "portaportal";

    private final String itemName = "&a&lHome Portal";
    private final List<String> itemLore = List.of("&7Right click to throw portal.", "&7Portal location set to: HOME");

    public PortalItem() {
        super(Material.SNOWBALL);
        setMeta();
    }

    private void setMeta() {
        ItemMeta meta = getItemMeta();
        if (meta == null) return;

        meta.setLore(ColorFormat.formatList(itemLore));
        meta.setDisplayName(ColorFormat.format(itemName));
        setAmount(1);

        NamespacedKey key = new NamespacedKey(PortaPortal.getInstance(), tag);
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, 1);

        setItemMeta(meta);
    }

    public static boolean isPortalitem(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        NamespacedKey key = new NamespacedKey(PortaPortal.getInstance(), tag);
        PersistentDataContainer container = meta.getPersistentDataContainer();

        return container.has(key, PersistentDataType.INTEGER);
    }
}