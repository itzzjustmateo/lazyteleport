package com.vomlabs.lazytp.command;

import com.vomlabs.lazytp.LazyTeleportPlugin;
import com.vomlabs.lazytp.config.MessageConfig;
import com.vomlabs.lazytp.config.PluginConfig;
import com.vomlabs.lazytp.util.MessageUtils;
import com.vomlabs.lazytp.util.ParticleUtils;
import com.vomlabs.lazytp.util.SoundUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCommand {

    protected final LazyTeleportPlugin lazyTeleport;
    protected final MessageConfig messages;
    protected final PluginConfig config;

    protected BaseCommand(LazyTeleportPlugin lazyTeleport) {
        this.lazyTeleport = lazyTeleport;
        this.messages = lazyTeleport.getMessageConfig();
        this.config = lazyTeleport.getPluginConfig();
    }

    protected Player requirePlayer(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            MessageUtils.sendMessage(sender, messages.getPlayerOnly());
            return null;
        }
        return player;
    }

    protected boolean hasPermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            MessageUtils.sendMessage(sender, messages.getNoPermission());
            if (sender instanceof Player player) {
                SoundUtils.playSound(player, config.getSoundError());
            }
            return false;
        }
        return true;
    }

    protected void playSuccess(Player player, String soundKey, String particleKey) {
        playSuccess(player, soundKey, particleKey, 20);
    }

    protected void playSuccess(Player player, String soundKey, String particleKey, int particleCount) {
        SoundUtils.playSound(player, soundKey);
        ParticleUtils.spawnParticle(player, particleKey, particleCount, 0.1);
    }

    protected void playError(Player player) {
        SoundUtils.playSound(player, config.getSoundError());
    }

    protected boolean validateName(String name) {
        if (name == null || name.isEmpty() || !name.matches("^[a-zA-Z0-9_-]{1,32}$")) {
            return false;
        }
        return true;
    }

    protected boolean charge(Player player, double cost) {
        if (!config.isEconomyEnabled() || cost <= 0) {
            return true;
        }
        return lazyTeleport.hasEconomy() && lazyTeleport.getVaultHook().charge(player, cost);
    }

}
