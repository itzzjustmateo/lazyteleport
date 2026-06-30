package com.vomlabs.lazytp.util;

import com.vomlabs.lazytp.scheduler.SchedulerAdapter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public final class SoundUtils {

    private SoundUtils() {
    }

    public static void playSound(Player player, String soundName) {
        if (soundName == null || soundName.isEmpty() || soundName.equalsIgnoreCase("none")) {
            return;
        }
        try {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, SoundCategory.MASTER, 1.0f, 1.0f);
        } catch (IllegalArgumentException ignored) {
        }
    }

    public static void playSound(Location location, String soundName) {
        if (soundName == null || soundName.isEmpty() || soundName.equalsIgnoreCase("none")) {
            return;
        }
        try {
            Sound sound = Sound.valueOf(soundName);
            location.getWorld().playSound(location, sound, SoundCategory.MASTER, 1.0f, 1.0f);
        } catch (IllegalArgumentException ignored) {
        }
    }

    public static void playSoundAsync(Player player, String soundName, SchedulerAdapter scheduler) {
        scheduler.runAtEntity(player, () -> playSound(player, soundName));
    }

}
