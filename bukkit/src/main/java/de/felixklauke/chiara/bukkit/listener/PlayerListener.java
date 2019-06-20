package de.felixklauke.chiara.bukkit.listener;

import de.felixklauke.chiara.bukkit.service.PermissionsService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final PermissionsService permissionsService;

    public PlayerListener(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent event) {

        Player player = event.getPlayer();
        permissionsService.registerPlayer(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();
        permissionsService.unregisterPlayer(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {

        Player player = event.getPlayer();
        permissionsService.unregisterPlayer(player);
    }
}
