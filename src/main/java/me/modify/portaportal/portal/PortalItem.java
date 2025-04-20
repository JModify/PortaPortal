package me.modify.portaportal.portal;

import lombok.Setter;
import me.modify.portaportal.PortaPortal;
import me.modify.portaportal.data.flatfile.ConfigFile;
import me.modify.portaportal.util.ColorFormat;
import me.modify.portaportal.util.PortaLogger;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PortalItem extends ItemStack {

    private static final String tag = "portaportal";

    @Setter
    private String itemName;

    @Setter
    private List<String> itemLore;

    @Setter
    private Material material;

    public PortalItem() {
        super();
        readConfigurations();
        setMeta();
    }

    private void readConfigurations() {
        PortaPortal plugin = PortaPortal.getInstance();
        FileConfiguration config = plugin.getConfigFile().getYaml();


        setItemName(config.getString("portal.item.name", "&5&lPortal"));
        setItemLore(config.getStringList("portal.item.lore"));

        String materialRaw = config.getString("portal.item.material");
        if (materialRaw == null || materialRaw.isEmpty()) {
            PortaLogger.error("Portal material configuration is missing or empty!");
            setMaterial(Material.SNOWBALL);
            return;
        }

        if (!materialRaw.equalsIgnoreCase("SNOWBALL") && !materialRaw.equalsIgnoreCase("ENDER_PEARL")
                && !materialRaw.equalsIgnoreCase("EGG")) {
            PortaLogger.error("Invalid portal material: " + materialRaw);
            PortaLogger.error("Portal material Options - SNOWBALL/ENDER_PEARL/EGG");
            setMaterial(Material.SNOWBALL);
            return;
        }

        setMaterial(Material.valueOf(materialRaw.toUpperCase()));
    }

    private void setMeta() {
        setType(material);

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

    public static boolean isPortalitem(PersistentDataContainer container) {
        NamespacedKey key = new NamespacedKey(PortaPortal.getInstance(), tag);
        return container.has(key, PersistentDataType.INTEGER);
    }
}