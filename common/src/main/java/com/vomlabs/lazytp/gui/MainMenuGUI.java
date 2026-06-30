package com.vomlabs.lazytp.gui;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.util.SoundUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class MainMenuGUI extends BaseGUI {

    private static final Component TITLE = Component.text("LazyTeleport Management", ORANGE);

    public MainMenuGUI(Player player, LazyTeleportPlugin plugin) {
        super(player, plugin, 27, TITLE);
    }

    @Override
    public void populate() {
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, null);
        }
        fillRow(0, 0, 8);
        fillRow(1, 0, 8);
        fillRow(2, 0, 8);

        int slot = 0;
        if (plugin.getWarpManager() != null) {
            inventory.setItem(slot++, item(Material.ENDER_CHEST,
                    text("Warps", GOLD),
                    text("Manage server warps", GRAY),
                    text("Create, edit, delete warps", GRAY)));
        }
        if (plugin.getHomeManager() != null) {
            inventory.setItem(slot++, item(Material.BOOK,
                    text("All Homes", AQUA),
                    text("Browse all player homes", GRAY),
                    text("View and manage homes", GRAY)));
            inventory.setItem(slot++, item(Material.PLAYER_HEAD,
                    text("My Homes", GREEN),
                    text("Manage your own homes", GRAY),
                    text("Create, delete, teleport", GRAY)));
        }
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        if (slot < 0 || slot >= 27) return;

        SoundUtils.playSound(player, plugin.getPluginConfig().getSoundHelpOpen());

        int warpSlot = 0;
        if (plugin.getWarpManager() != null) {
            if (slot == warpSlot) {
                if (player.hasPermission(Permissions.ADMIN)) {
                    open(new WarpListGUI(player, plugin));
                }
                return;
            }
            warpSlot++;
        }
        if (plugin.getHomeManager() != null) {
            if (slot == warpSlot) {
                open(new HomesListGUI(player, plugin));
                return;
            }
            warpSlot++;
            if (slot == warpSlot) {
                open(new PlayerHomesGUI(player, plugin));
                return;
            }
        }
    }

}
