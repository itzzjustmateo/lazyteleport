package com.vomlabs.lazytp.util;

import com.vomlabs.lazytp.scheduler.SchedulerAdapter;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public final class SoundUtils {

    private SoundUtils() {
    }

    public static void playSound(Player player, String soundName) {
        Sound sound = resolveSound(soundName);
        if (sound != null) {
            player.playSound(player.getLocation(), sound, SoundCategory.MASTER, 1.0f, 1.0f);
        }
    }

    public static void playSound(Location location, String soundName) {
        Sound sound = resolveSound(soundName);
        if (sound != null && location.getWorld() != null) {
            location.getWorld().playSound(location, sound, SoundCategory.MASTER, 1.0f, 1.0f);
        }
    }

    public static void playSoundAsync(Player player, String soundName, SchedulerAdapter scheduler) {
        scheduler.runAtEntity(player, () -> playSound(player, soundName));
    }

    private static Sound resolveSound(String name) {
        if (name == null || name.isEmpty() || name.equalsIgnoreCase("none")) {
            return null;
        }
        NamespacedKey key = NamespacedKey.fromString(name);
        if (key != null) {
            Sound sound = Registry.SOUND_EVENT.get(key);
            if (sound != null) {
                return sound;
            }
        }
        String converted = name.toLowerCase().replace('_', '.');
        key = NamespacedKey.fromString(converted);
        if (key != null) {
            Sound sound = Registry.SOUND_EVENT.get(key);
            if (sound != null) {
                return sound;
            }
        }
        return null;
    }

}
