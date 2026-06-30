package com.vomlabs.lazytp.hook;

import com.vomlabs.lazytp.util.MessageUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    private Economy economy;
    private boolean enabled = false;

    public boolean setup() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> provider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (provider == null) {
            return false;
        }
        economy = provider.getProvider();
        enabled = economy != null;
        return enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean charge(Player player, double amount) {
        if (!enabled || amount <= 0) {
            return true;
        }
        if (!economy.has(player, amount)) {
            String currency = economy.currencyNamePlural();
            if (currency == null || currency.isEmpty()) {
                currency = amount == 1 ? economy.currencyNameSingular() : economy.currencyNamePlural();
            }
            MessageUtils.sendMessage(player, "<red>You need <gold>" + format(amount) + " " + currency + "</gold> to do that!</red>");
            return false;
        }
        EconomyResponse result = economy.withdrawPlayer(player, amount);
        if (result.transactionSuccess()) {
            String currency = amount == 1 ? economy.currencyNameSingular() : economy.currencyNamePlural();
            MessageUtils.sendMessage(player, "<gray>Charged <gold>" + format(amount) + " " + currency + "</gold></gray>");
            return true;
        }
        return false;
    }

    public double getBalance(Player player) {
        if (!enabled) return 0;
        return economy.getBalance(player);
    }

    private String format(double amount) {
        if (amount == (long) amount) {
            return String.valueOf((long) amount);
        }
        return String.format("%.2f", amount);
    }

}
