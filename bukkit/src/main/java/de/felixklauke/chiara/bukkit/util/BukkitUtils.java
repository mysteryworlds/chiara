package de.felixklauke.chiara.bukkit.util;

import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class BukkitUtils {

  public static UUID getUniqueIdByPlayerName(String playerName) {

    Objects.requireNonNull(playerName, "Player name cannot be null.");

    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
    return offlinePlayer.getUniqueId();
  }
}
