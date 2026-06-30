package com.vomlabs.lazytp.gui;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.manager.TeleportManager;
import com.vomlabs.lazytp.manager.WarpManager;
import com.vomlabs.lazytp.model.Warp;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.util.MessageUtils;
import com.vomlabs.lazytp.util.SoundUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class WarpListGUI extends BaseGUI {

    private static final Component TITLE = Component.text("Warp Management", ORANGE);
    private static final int ENTRIES_PER_PAGE = 36;

    private final WarpManager warpManager;
    private final TeleportManager teleportManager;
    private final AtomicBoolean deleteMode = new AtomicBoolean(false);
    private final AtomicBoolean editMode = new AtomicBoolean(false);

    private static final int SLOT_CREATE = 0;
    private static final int SLOT_DELETE = 1;
    private static final int SLOT_EDIT = 2;
    private static final int SLOT_BACK = 8;
    private static final int SLOT_PREV = 45;
    private static final int SLOT_PAGE = 49;
    private static final int SLOT_NEXT = 53;

    public WarpListGUI(Player player, LazyTeleportPlugin plugin) {
        super(player, plugin, 54, TITLE);
        this.warpManager = plugin.getWarpManager();
        this.teleportManager = plugin.getTeleportManager();
    }

    @Override
    public void populate() {
        List<Warp> warps = new ArrayList<>(warpManager != null ? warpManager.getAll() : List.of());
        maxPage = Math.max(0, (warps.size() - 1) / ENTRIES_PER_PAGE);
        if (currentPage > maxPage) currentPage = maxPage;

        fillRow(4, 0, 8);
        fillRow(5, 0, 8);

        inventory.setItem(SLOT_CREATE, item(Material.LIME_DYE,
                text("Create Warp", GREEN),
                text("Click then type a name in chat", GRAY)));

        if (deleteMode.get()) {
            inventory.setItem(SLOT_DELETE, item(Material.RED_CONCRETE,
                    text("Delete Mode: ON", RED),
                    text("Click a warp to delete it", GRAY)));
        } else {
            inventory.setItem(SLOT_DELETE, item(Material.RED_DYE,
                    text("Delete Mode: OFF", GRAY),
                    text("Click to enable delete mode", GRAY)));
        }

        if (editMode.get()) {
            inventory.setItem(SLOT_EDIT, item(Material.GOLD_INGOT,
                    text("Edit Mode: ON", GOLD),
                    text("Click a warp to edit it", GRAY)));
        } else {
            inventory.setItem(SLOT_EDIT, item(Material.ORANGE_DYE,
                    text("Edit Mode: OFF", GRAY),
                    text("Click to enable edit mode", GRAY)));
        }

        inventory.setItem(SLOT_BACK, item(Material.BARRIER,
                text("Back", RED),
                text("Return to main menu", GRAY)));

        int start = currentPage * ENTRIES_PER_PAGE;
        int end = Math.min(start + ENTRIES_PER_PAGE, warps.size());
        int slot = 9;
        for (int i = start; i < end; i++) {
            Warp warp = warps.get(i);
            String world = warp.getWorldName();
            List<Component> lore = new ArrayList<>();
            lore.add(text("World: " + world, GRAY));
            lore.add(text("Click to teleport", GRAY));
            slot = fillEntry(slot, warp, lore);
        }

        if (currentPage > 0) {
            inventory.setItem(SLOT_PREV, item(Material.ARROW,
                    text("Previous Page", WHITE)));
        }
        inventory.setItem(SLOT_PAGE, item(Material.PAPER,
                text("Page " + (currentPage + 1) + "/" + (maxPage + 1), WHITE)));
        if (currentPage < maxPage) {
            inventory.setItem(SLOT_NEXT, item(Material.ARROW,
                    text("Next Page", WHITE)));
        }
    }

    private int fillEntry(int slot, Warp warp, List<Component> lore) {
        if (deleteMode.get()) {
            inventory.setItem(slot, item(Material.REDSTONE_BLOCK,
                    text("Delete: " + warp.getName(), RED), lore));
        } else if (editMode.get()) {
            inventory.setItem(slot, item(Material.ENDER_CHEST,
                    text("Edit: " + warp.getName(), GOLD), lore));
        } else {
            inventory.setItem(slot, item(Material.ENDER_PEARL,
                    text(warp.getName(), WHITE), lore));
        }
        return slot + 1;
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        int slot = event.getSlot();

        if (slot == SLOT_CREATE) {
            if (!player.hasPermission(Permissions.WARP_SET)) {
                MessageUtils.sendMessage(player, "<red>No permission to create warps!</red>");
                SoundUtils.playSound(player, plugin.getPluginConfig().getSoundError());
                return;
            }
            player.closeInventory();
            plugin.getChatPrompt().await(player,
                    "<gold>Enter a name for the new warp (or 'cancel'):</gold>",
                    (p, input) -> {
                        if (input.equalsIgnoreCase("cancel")) {
                            MessageUtils.sendMessage(p, "<gray>Cancelled.</gray>");
                            return;
                        }
                        try {
                            warpManager.create(input, p.getLocation());
                            MessageUtils.sendMessage(p, "<green>Warp '" + input + "' created!</green>");
                        } catch (Exception e) {
                            MessageUtils.sendMessage(p, "<red>" + e.getMessage() + "</red>");
                        }
                    });
            return;
        }

        if (slot == SLOT_DELETE) {
            deleteMode.set(!deleteMode.get());
            editMode.set(false);
            populate();
            return;
        }

        if (slot == SLOT_EDIT) {
            editMode.set(!editMode.get());
            deleteMode.set(false);
            populate();
            return;
        }

        if (slot == SLOT_BACK) {
            open(new MainMenuGUI(player, plugin));
            return;
        }

        if (slot == SLOT_PREV && currentPage > 0) {
            currentPage--;
            populate();
            return;
        }

        if (slot == SLOT_NEXT && currentPage < maxPage) {
            currentPage++;
            populate();
            return;
        }

        int index = slot - 9;
        if (index < 0) return;
        int warpIndex = currentPage * ENTRIES_PER_PAGE + index;
        List<Warp> warps = new ArrayList<>(warpManager != null ? warpManager.getAll() : List.of());
        if (warpIndex >= warps.size()) return;
        Warp warp = warps.get(warpIndex);

        if (deleteMode.get()) {
            if (!player.hasPermission(Permissions.WARP_DEL)) {
                MessageUtils.sendMessage(player, "<red>No permission to delete warps!</red>");
                SoundUtils.playSound(player, plugin.getPluginConfig().getSoundError());
                return;
            }
            warpManager.delete(warp.getName());
            MessageUtils.sendMessage(player, "<green>Warp '" + warp.getName() + "' deleted!</green>");
            SoundUtils.playSound(player, plugin.getPluginConfig().getSoundWarpDeleted());
            deleteMode.set(false);
            populate();
            return;
        }

        if (editMode.get()) {
            if (!player.hasPermission(Permissions.ADMIN)) {
                MessageUtils.sendMessage(player, "<red>No permission!</red>");
                SoundUtils.playSound(player, plugin.getPluginConfig().getSoundError());
                return;
            }
            open(new WarpEditGUI(player, plugin, warp));
            return;
        }

        if (player.hasPermission(Permissions.WARP_USE)) {
            teleportManager.teleport(player, warp.toLocation(), () -> {});
            player.closeInventory();
        }
    }

}
