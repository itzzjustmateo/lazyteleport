package com.vomlabs.lazytp.gui;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.manager.HomeManager;
import com.vomlabs.lazytp.manager.TeleportManager;
import com.vomlabs.lazytp.model.Home;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.util.MessageUtils;
import com.vomlabs.lazytp.util.SoundUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HomesListGUI extends BaseGUI {

    private static final Component TITLE = Component.text("All Homes", ORANGE);
    private static final int ENTRIES_PER_PAGE = 36;

    private final HomeManager homeManager;
    private final TeleportManager teleportManager;
    private final AtomicBoolean deleteMode = new AtomicBoolean(false);

    private static final int SLOT_DELETE = 1;
    private static final int SLOT_BACK = 8;
    private static final int SLOT_PREV = 45;
    private static final int SLOT_PAGE = 49;
    private static final int SLOT_NEXT = 53;

    public HomesListGUI(Player player, LazyTeleportPlugin plugin) {
        super(player, plugin, 54, TITLE);
        this.homeManager = plugin.getHomeManager();
        this.teleportManager = plugin.getTeleportManager();
    }

    @Override
    public void populate() {
        List<Home> homes = new ArrayList<>(homeManager != null ? homeManager.getAll() : List.of());
        maxPage = Math.max(0, (homes.size() - 1) / ENTRIES_PER_PAGE);
        if (currentPage > maxPage) currentPage = maxPage;

        fillRow(4, 0, 8);
        fillRow(5, 0, 8);

        if (deleteMode.get()) {
            inventory.setItem(SLOT_DELETE, item(Material.RED_CONCRETE,
                    text("Delete Mode: ON", RED),
                    text("Click a home to delete it", GRAY)));
        } else {
            inventory.setItem(SLOT_DELETE, item(Material.RED_DYE,
                    text("Delete Mode: OFF", GRAY),
                    text("Click to enable delete mode", GRAY)));
        }

        inventory.setItem(SLOT_BACK, item(Material.BARRIER,
                text("Back", RED),
                text("Return to main menu", GRAY)));

        int start = currentPage * ENTRIES_PER_PAGE;
        int end = Math.min(start + ENTRIES_PER_PAGE, homes.size());
        int slot = 9;
        for (int i = start; i < end; i++) {
            Home home = homes.get(i);
            OfflinePlayer owner = Bukkit.getOfflinePlayer(home.getOwnerId());
            String ownerName = owner.getName() != null ? owner.getName() : home.getOwnerId().toString().substring(0, 8);
            String world = home.getWorldName();
            List<Component> lore = new ArrayList<>();
            lore.add(text("Owner: " + ownerName, GRAY));
            lore.add(text("World: " + world, GRAY));
            lore.add(text("Click to teleport", GRAY));

            Material mat = deleteMode.get() ? Material.REDSTONE_BLOCK : Material.OAK_DOOR;
            Component name = deleteMode.get()
                    ? text("Delete: " + home.getName() + " (" + ownerName + ")", RED)
                    : text(home.getName() + " (" + ownerName + ")", WHITE);
            inventory.setItem(slot, item(mat, name, lore));
            slot++;
        }

        if (currentPage > 0) {
            inventory.setItem(SLOT_PREV, item(Material.ARROW, text("Previous Page", WHITE)));
        }
        inventory.setItem(SLOT_PAGE, item(Material.PAPER, text("Page " + (currentPage + 1) + "/" + (maxPage + 1), WHITE)));
        if (currentPage < maxPage) {
            inventory.setItem(SLOT_NEXT, item(Material.ARROW, text("Next Page", WHITE)));
        }
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        int slot = event.getSlot();

        if (slot == SLOT_DELETE) {
            deleteMode.set(!deleteMode.get());
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
        int homeIndex = currentPage * ENTRIES_PER_PAGE + index;
        List<Home> homes = new ArrayList<>(homeManager != null ? homeManager.getAll() : List.of());
        if (homeIndex >= homes.size()) return;
        Home home = homes.get(homeIndex);

        if (deleteMode.get()) {
            if (!player.hasPermission(Permissions.ADMIN)) {
                MessageUtils.sendMessage(player, "<red>No permission!</red>");
                SoundUtils.playSound(player, plugin.getPluginConfig().getSoundError());
                return;
            }
            homeManager.delete(home.getOwnerId(), home.getName());
            MessageUtils.sendMessage(player, "<green>Deleted home '" + home.getName() + "'</green>");
            SoundUtils.playSound(player, plugin.getPluginConfig().getSoundHomeDeleted());
            deleteMode.set(false);
            populate();
            return;
        }

        if (player.hasPermission(Permissions.HOME_USE) || player.hasPermission(Permissions.ADMIN)) {
            teleportManager.teleport(player, home.toLocation(), () -> {});
            player.closeInventory();
        }
    }

}
