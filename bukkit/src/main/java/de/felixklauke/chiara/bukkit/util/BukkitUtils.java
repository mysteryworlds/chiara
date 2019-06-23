package de.felixklauke.chiara.bukkit.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class BukkitUtils {

    public static UUID getUniqueIdByPlayerName(String playerName) {

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        return offlinePlayer.getUniqueId();
    }
}
