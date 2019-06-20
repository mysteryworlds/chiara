package de.felixklauke.chiara.bukkit.service;

import org.bukkit.entity.Player;

public interface PermissionsService {

    void registerPlayer(Player player);

    void unregisterPlayer(Player player);
}
