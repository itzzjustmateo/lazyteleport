package com.vomlabs.lazytp.gui;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseGUI implements InventoryHolder {

    protected static final TextColor ORANGE = TextColor.color(0xFFAA00);
    protected static final TextColor GOLD = TextColor.color(0xFFFF55);
    protected static final TextColor RED = TextColor.color(0xFF5555);
    protected static final TextColor GREEN = TextColor.color(0x55FF55);
    protected static final TextColor AQUA = TextColor.color(0x55FFFF);
    protected static final TextColor GRAY = TextColor.color(0xAAAAAA);
    protected static final TextColor WHITE = TextColor.color(0xFFFFFF);

    protected final Player player;
    protected final LazyTeleportPlugin plugin;
    protected final Inventory inventory;
    protected int currentPage = 0;
    protected int maxPage = 0;

    public BaseGUI(Player player, LazyTeleportPlugin plugin, int size, Component title) {
        this.player = player;
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(this, size, title);
    }

    public abstract void populate();

    public abstract void handleClick(InventoryClickEvent event);

    public void handleDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    protected ItemStack item(Material material, Component name, Component... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.itemName(name);
        if (lore.length > 0) {
            List<Component> loreList = new ArrayList<>();
            for (Component l : lore) {
                if (l != null) loreList.add(l);
            }
            meta.lore(loreList);
        }
        item.setItemMeta(meta);
        return item;
    }

    protected ItemStack item(Material material, Component name, List<Component> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.itemName(name);
        if (lore != null && !lore.isEmpty()) {
            meta.lore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    protected Component text(String content, TextColor color) {
        return Component.text(content, color).decoration(TextDecoration.ITALIC, false);
    }

    protected void open(BaseGUI gui) {
        player.closeInventory();
        gui.populate();
        player.openInventory(gui.getInventory());
    }

    protected void fillEmpty(int... slots) {
        for (int slot : slots) {
            if (slot >= 0 && slot < inventory.getSize() && inventory.getItem(slot) == null) {
                inventory.setItem(slot, EMPTY);
            }
        }
    }

    protected void fillRow(int row, int startCol, int endCol) {
        for (int col = startCol; col <= endCol; col++) {
            int slot = row * 9 + col;
            if (slot >= 0 && slot < inventory.getSize()) {
                inventory.setItem(slot, EMPTY);
            }
        }
    }

    protected void fillColumn(int col, int startRow, int endRow) {
        for (int row = startRow; row <= endRow; row++) {
            int slot = row * 9 + col;
            if (slot >= 0 && slot < inventory.getSize()) {
                inventory.setItem(slot, EMPTY);
            }
        }
    }

    private static final ItemStack EMPTY;

    static {
        ItemStack empty = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = empty.getItemMeta();
        meta.itemName(Component.text(""));
        empty.setItemMeta(meta);
        EMPTY = empty;
    }

}
