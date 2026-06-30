package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.config.PluginConfig;
import com.vomlabs.lazytp.permission.Permissions;
import com.vomlabs.lazytp.util.MessageUtils;
import com.vomlabs.lazytp.util.ParticleUtils;
import com.vomlabs.lazytp.util.SoundUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LazytpCommand extends BaseCommand implements CommandExecutor, TabCompleter {

    private static final TextColor PRIMARY = TextColor.color(0xFFAA00);
    private static final TextColor SECONDARY = TextColor.color(0xFFFF55);
    private static final TextColor HIGHLIGHT = TextColor.color(0x55FFFF);
    private static final TextColor DIM = TextColor.color(0xAAAAAA);

    private final PluginConfig config;

    public LazytpCommand(LazyTeleportPlugin lazyTeleport) {
        super(lazyTeleport);
        this.config = lazyTeleport.getPluginConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!hasPermission(sender, Permissions.ADMIN)) {
                return true;
            }
            lazyTeleport.reload();
            MessageUtils.sendMessage(sender, lazyTeleport.getMessageConfig().getPrefix() + " <green>Configuration reloaded!</green>");
            return true;
        }

        if (args[0].equalsIgnoreCase("version")) {
            MessageUtils.sendMessage(sender, lazyTeleport.getMessageConfig().getPrefix()
                    + " <gold>Version <yellow>" + lazyTeleport.getPlugin().getPluginMeta().getVersion() + "</yellow></gold>");
            return true;
        }

        sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        if (sender instanceof Player player) {
            SoundUtils.playSound(player, config.getSoundHelpOpen());
            ParticleUtils.spawnParticle(player, "VILLAGER_HAPPY", 10, 0.1);
        }

        Component header = Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("╔══════════════════════════════════════╗", PRIMARY)
        );
        Component title = Component.text("  LazyTeleport Commands", SECONDARY);
        Component separator = Component.text("╠══════════════════════════════════════╣", DIM);

        sender.sendMessage(header);
        sender.sendMessage(title);

        if (sender.hasPermission(Permissions.WARP_USE)) {
            sendCommand(sender, "/warp <name>", "Teleport to a warp", PRIMARY);
        }
        if (sender.hasPermission(Permissions.WARP_SET)) {
            sendCommand(sender, "/setwarp <name>", "Create a warp", PRIMARY);
        }
        if (sender.hasPermission(Permissions.WARP_DEL)) {
            sendCommand(sender, "/delwarp <name>", "Delete a warp", PRIMARY);
        }
        if (sender.hasPermission(Permissions.WARP_LIST)) {
            sendCommand(sender, "/listwarp", "List all warps", PRIMARY);
        }

        sender.sendMessage(separator);

        if (sender.hasPermission(Permissions.HOME_USE)) {
            sendCommand(sender, "/home [name]", "Teleport to your home", HIGHLIGHT);
        }
        if (sender.hasPermission(Permissions.HOME_SET)) {
            sendCommand(sender, "/sethome [name]", "Set your home", HIGHLIGHT);
        }
        if (sender.hasPermission(Permissions.HOME_DEL)) {
            sendCommand(sender, "/delhome <name>", "Delete a home", HIGHLIGHT);
        }
        if (sender.hasPermission(Permissions.HOME_LIST)) {
            sendCommand(sender, "/listhome", "List your homes", HIGHLIGHT);
        }

        sender.sendMessage(separator);

        if (sender.hasPermission(Permissions.SPAWN_USE)) {
            sendCommand(sender, "/spawn", "Teleport to spawn", SECONDARY);
        }
        if (sender.hasPermission(Permissions.SPAWN_SET)) {
            sendCommand(sender, "/setspawn", "Set the spawn", SECONDARY);
        }
        if (sender.hasPermission(Permissions.SPAWN_DEL)) {
            sendCommand(sender, "/delspawn", "Delete spawn", SECONDARY);
        }

        sender.sendMessage(separator);

        if (sender.hasPermission(Permissions.LOBBY_USE)) {
            sendCommand(sender, "/lobby", "Teleport to lobby", SECONDARY);
        }
        if (sender.hasPermission(Permissions.LOBBY_SET)) {
            sendCommand(sender, "/setlobby", "Set the lobby", SECONDARY);
        }
        if (sender.hasPermission(Permissions.LOBBY_DEL)) {
            sendCommand(sender, "/dellobby", "Delete lobby", SECONDARY);
        }

        sender.sendMessage(separator);

        if (sender.hasPermission(Permissions.ADMIN)) {
            sendCommand(sender, "/lazytp reload", "Reload configuration", PRIMARY);
        }
        sendCommand(sender, "/lazytp version", "Show plugin version", DIM);
        sendCommand(sender, "/lazytp help", "Show this help", DIM);

        Component footer = Component.text("╚══════════════════════════════════════╝", PRIMARY);
        sender.sendMessage(footer);
    }

    private void sendCommand(CommandSender sender, String usage, String description, TextColor color) {
        Component msg = Component.join(
                JoinConfiguration.noSeparators(),
                Component.text("  ", DIM),
                Component.text(usage, color),
                Component.text("  -  ", DIM),
                Component.text(description, TextColor.color(0xFFFFFF))
        );
        sender.sendMessage(msg);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> completions = new java.util.ArrayList<>();
            completions.add("help");
            completions.add("version");
            if (sender.hasPermission(Permissions.ADMIN)) {
                completions.add("reload");
            }
            return completions.stream()
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return List.of();
    }

}
