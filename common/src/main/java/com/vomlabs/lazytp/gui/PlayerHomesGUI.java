package com.vomlabs.lazytp.gui;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.exception.InvalidNameException;
import com.vomlabs.lazytp.manager.HomeManager;
import com.vomlabs.lazytp.manager.TeleportManager;
import com.vomlabs.lazytp.model.Home;
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

public class PlayerHomesGUI extends BaseGUI {

    private static final Component TITLE = Component.text("My Homes", GREEN);
    private static final int ENTRIES_PER_PAGE = 36;

    private final HomeManager homeManager;
    private final TeleportManager teleportManager;
    private final AtomicBoolean deleteMode = new AtomicBoolean(false);

    private static final int SLOT_CREATE = 0;
    private static final int SLOT_DELETE = 1;
    private static final int SLOT_BACK = 8;
    private static final int SLOT_PREV = 45;
    private static final int SLOT_PAGE = 49;
    private static final int SLOT_NEXT = 53;

    public PlayerHomesGUI(Player player, LazyTeleportPlugin plugin) {
        super(player, plugin, 54, TITLE);
        this.homeManager = plugin.getHomeManager();
        this.teleportManager = plugin.getTeleportManager();
    }

    @Override
    public void populate() {
        List<Home> homes = new ArrayList<>(homeManager != null
                ? homeManager.getByOwner(player.getUniqueId()) : List.of());
        maxPage = Math.max(0, (homes.size() - 1) / ENTRIES_PER_PAGE);
        if (currentPage > maxPage) currentPage = maxPage;

        fillRow(4, 0, 8);
        fillRow(5, 0, 8);

        inventory.setItem(SLOT_CREATE, item(Material.LIME_DYE,
                text("Set Home", GREEN),
                text("Click then type a name in chat", GRAY)));

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
            String world = home.getWorldName();
            List<Component> lore = new ArrayList<>();
            lore.add(text("World: " + world, GRAY));
            lore.add(text("Click to teleport", GRAY));

            Material mat = deleteMode.get() ? Material.REDSTONE_BLOCK : Material.RED_BED;
            Component name = deleteMode.get()
                    ? text("Delete: " + home.getName(), RED)
                    : text(home.getName(), WHITE);
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

        if (slot == SLOT_CREATE) {
            if (!player.hasPermission(Permissions.HOME_SET)) {
                MessageUtils.sendMessage(player, "<red>No permission to set homes!</red>");
                SoundUtils.playSound(player, plugin.getPluginConfig().getSoundError());
                return;
            }
            player.closeInventory();
            plugin.getChatPrompt().await(player,
                    "<gold>Enter a name for the new home (or 'cancel'):</gold>",
                    (p, input) -> {
                        if (input.equalsIgnoreCase("cancel")) {
                            MessageUtils.sendMessage(p, "<gray>Cancelled.</gray>");
                            return;
                        }
                        String homeName = input.isEmpty() ? "home" : input;
                        try {
                            homeManager.create(p, homeName, p.getLocation());
                            MessageUtils.sendMessage(p, "<green>Home '" + homeName + "' created!</green>");
                            SoundUtils.playSound(p, plugin.getPluginConfig().getSoundHomeCreated());
                        } catch (InvalidNameException e) {
                            if (e.getMessage().contains("already exists")) {
                                MessageUtils.sendMessage(p, "<red>Home '" + homeName + "' already exists!</red>");
                            } else {
                                MessageUtils.sendMessage(p, "<red>Invalid name! Use letters, numbers, underscores, hyphens (1-32 chars).</red>");
                            }
                            SoundUtils.playSound(p, plugin.getPluginConfig().getSoundError());
                        } catch (IllegalStateException e) {
                            MessageUtils.sendMessage(p, "<red>Home limit reached!</red>");
                            SoundUtils.playSound(p, plugin.getPluginConfig().getSoundError());
                        }
                    });
            return;
        }

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
        List<Home> homes = new ArrayList<>(homeManager != null
                ? homeManager.getByOwner(player.getUniqueId()) : List.of());
        if (homeIndex >= homes.size()) return;
        Home home = homes.get(homeIndex);

        if (deleteMode.get()) {
            if (!player.hasPermission(Permissions.HOME_DEL)) {
                MessageUtils.sendMessage(player, "<red>No permission!</red>");
                SoundUtils.playSound(player, plugin.getPluginConfig().getSoundError());
                return;
            }
            homeManager.delete(player.getUniqueId(), home.getName());
            MessageUtils.sendMessage(player, "<green>Deleted home '" + home.getName() + "'</green>");
            SoundUtils.playSound(player, plugin.getPluginConfig().getSoundHomeDeleted());
            deleteMode.set(false);
            populate();
            return;
        }

        if (player.hasPermission(Permissions.HOME_USE)) {
            teleportManager.teleport(player, home.toLocation(), () -> {});
            player.closeInventory();
        }
    }

}
