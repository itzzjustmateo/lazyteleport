package com.vomlabs.lazytp.gui;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.model.Warp;
import com.vomlabs.lazytp.util.MessageUtils;
import com.vomlabs.lazytp.util.SoundUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class WarpEditGUI extends BaseGUI {

    private static final Component TITLE = Component.text("Edit Warp", ORANGE);

    private final Warp warp;

    private static final int SLOT_TELEPORT = 0;
    private static final int SLOT_MOVE = 1;
    private static final int SLOT_RENAME = 2;
    private static final int SLOT_INFO = 4;
    private static final int SLOT_BACK = 8;

    public WarpEditGUI(Player player, LazyTeleportPlugin plugin, Warp warp) {
        super(player, plugin, 9, TITLE);
        this.warp = warp;
    }

    @Override
    public void populate() {
        for (int i = 0; i < 9; i++) inventory.setItem(i, null);
        fillRow(0, 0, 8);

        inventory.setItem(SLOT_TELEPORT, item(Material.ENDER_PEARL,
                text("Teleport Here", AQUA),
                text("Teleport to this warp location", GRAY)));

        inventory.setItem(SLOT_MOVE, item(Material.COMPASS,
                text("Move Here", GOLD),
                text("Move warp to your current location", GRAY)));

        inventory.setItem(SLOT_RENAME, item(Material.NAME_TAG,
                text("Rename", GREEN),
                text("Click then type a new name in chat", GRAY)));

        inventory.setItem(SLOT_INFO, item(Material.PAPER,
                text(warp.getName(), WHITE),
                List.of(
                        text("World: " + warp.getWorldName(), GRAY),
                        text("X: " + format(warp.getX()), GRAY),
                        text("Y: " + format(warp.getY()), GRAY),
                        text("Z: " + format(warp.getZ()), GRAY)
                )));

        inventory.setItem(SLOT_BACK, item(Material.BARRIER,
                text("Back", RED),
                text("Return to warp list", GRAY)));
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        int slot = event.getSlot();

        if (slot == SLOT_TELEPORT) {
            var loc = warp.toLocation();
            if (loc != null) {
                player.teleportAsync(loc);
                MessageUtils.sendMessage(player, "<green>Teleported to warp '" + warp.getName() + "'</green>");
            }
            player.closeInventory();
            return;
        }

        if (slot == SLOT_MOVE) {
            plugin.getWarpManager().delete(warp.getName());
            plugin.getWarpManager().create(warp.getName(), player.getLocation());
            MessageUtils.sendMessage(player, "<green>Warp '" + warp.getName() + "' moved to your location!</green>");
            SoundUtils.playSound(player, plugin.getPluginConfig().getSoundWarpCreated());
            player.closeInventory();
            return;
        }

        if (slot == SLOT_RENAME) {
            player.closeInventory();
            plugin.getChatPrompt().await(player,
                    "<gold>Enter a new name for warp '" + warp.getName() + "' (or 'cancel'):</gold>",
                    (p, input) -> {
                        if (input.equalsIgnoreCase("cancel")) {
                            MessageUtils.sendMessage(p, "<gray>Cancelled.</gray>");
                            return;
                        }
                        String oldName = warp.getName();
                        var loc = warp.toLocation();
                        if (loc == null) {
                            MessageUtils.sendMessage(p, "<red>Invalid warp location!</red>");
                            return;
                        }
                        try {
                            plugin.getWarpManager().delete(oldName);
                            plugin.getWarpManager().create(input, loc);
                            MessageUtils.sendMessage(p, "<green>Warp renamed to '" + input + "'</green>");
                            SoundUtils.playSound(p, plugin.getPluginConfig().getSoundWarpCreated());
                        } catch (Exception e) {
                            plugin.getWarpManager().create(oldName, loc);
                            MessageUtils.sendMessage(p, "<red>" + e.getMessage() + "</red>");
                            SoundUtils.playSound(p, plugin.getPluginConfig().getSoundError());
                        }
                    });
            return;
        }

        if (slot == SLOT_BACK) {
            open(new WarpListGUI(player, plugin));
        }
    }

    private String format(double d) {
        return String.format("%.1f", d);
    }

}
