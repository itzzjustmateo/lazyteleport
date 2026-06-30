package com.vomlabs.lazytp.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

import java.util.regex.Pattern;

public final class MessageUtils {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Pattern LEGACY_PATTERN = Pattern.compile("^[&§][0-9a-fk-or]");

    private MessageUtils() {
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

    public static void sendMessage(CommandSender sender, Component message) {
        sender.sendMessage(message);
    }

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(parse(message));
    }

    public static void sendMessage(CommandSender sender, String message, String placeholder, String value) {
        sender.sendMessage(parse(message, placeholder, value));
    }

}
