package com.vomlabs.lazytp.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public final class MessageUtils {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Pattern LEGACY_PATTERN = Pattern.compile("^[&§][0-9a-fk-or]");
    private static boolean papiAvailable = false;

    private MessageUtils() {
    }

    public static void setPapiAvailable(boolean available) {
        papiAvailable = available;
    }

    public static String applyPlaceholders(String message, Player player) {
        if (!papiAvailable || player == null || message == null || message.isEmpty()) {
            return message;
        }
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    public static Component parse(String message) {
        if (message == null || message.isEmpty()) {
            return Component.empty();
        }
        if (LEGACY_PATTERN.matcher(message).find()) {
            return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
        }
        return MINI_MESSAGE.deserialize(message);
    }

    public static Component parse(String message, String placeholder, String value) {
        if (message == null) {
            return Component.empty();
        }
        String resolved = message.replace("<" + placeholder + ">", value);
        return parse(resolved);
    }

    public static Component parseForPlayer(String message, Player player) {
        return parse(applyPlaceholders(message, player));
    }

    public static Component parseForPlayer(String message, String placeholder, String value, Player player) {
        if (message == null) {
            return Component.empty();
        }
        String resolved = message.replace("<" + placeholder + ">", value);
        return parseForPlayer(resolved, player);
    }

    public static void sendMessage(CommandSender sender, Component message) {
        sender.sendMessage(message);
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(parse(message));
    }

    public static void sendMessage(CommandSender sender, String message, String placeholder, String value) {
        sender.sendMessage(parse(message, placeholder, value));
    }

    public static void sendMessage(CommandSender sender, String message, Player player) {
        sender.sendMessage(parseForPlayer(message, player));
    }

    public static void sendMessage(CommandSender sender, String message, String placeholder, String value, Player player) {
        sender.sendMessage(parseForPlayer(message, placeholder, value, player));
    }

}
