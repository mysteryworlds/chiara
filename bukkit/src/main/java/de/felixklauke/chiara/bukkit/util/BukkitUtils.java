package de.felixklauke.chiara.bukkit.util;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class BukkitUtils {

  public static UUID getUniqueIdByPlayerName(String playerName) {

    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
    return offlinePlayer.getUniqueId();
  }
}
