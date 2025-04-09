package me.modify.portaportal.item;

import me.modify.portaportal.util.ColorFormat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class HomePortal extends ItemStack {

    private final String itemName = "&a&lHome Portal";
    private final List<String> itemLore = List.of("&7Right click to throw portal.", "&7Portal location set to: HOME");

    public HomePortal() {
        super(Material.SNOWBALL);

        ItemMeta meta = getItemMeta();
        meta.setLore(ColorFormat.formatList(itemLore));
        meta.setDisplayName(ColorFormat.format(itemName));
        setItemMeta(meta);

        setAmount(1);
    }
}