package de.felixklauke.chiara.bukkit.listener;

import de.felixklauke.chiara.bukkit.user.PermissionUser;
import de.felixklauke.chiara.bukkit.user.PermissionUserRepository;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PermissionListener implements Listener {
  private final Plugin plugin;
  private final PermissionUserRepository permissionUserRepository;

  @Inject
  public PermissionListener(
    Plugin plugin,
    PermissionUserRepository permissionUserRepository
  ) {
    this.plugin = plugin;
    this.permissionUserRepository = permissionUserRepository;
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerLogin(PlayerLoginEvent event) {
    var player = event.getPlayer();
    var uniqueId = player.getUniqueId();
    Optional<PermissionUser> userOptional = permissionUserRepository.find(uniqueId);
    userOptional.ifPresent(permissionUser -> permissionUser.calculatePermissionAttachment(plugin));
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerQuit(PlayerQuitEvent event) {
    var player = event.getPlayer();
    var uniqueId = player.getUniqueId();
    Optional<PermissionUser> userOptional = permissionUserRepository.find(uniqueId);
    userOptional.ifPresent(permissionUser -> permissionUser.removePermissionAttachment(plugin));
  }

  @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
  public void onPlayerKick(PlayerKickEvent event) {
    var player = event.getPlayer();
    var uniqueId = player.getUniqueId();
    Optional<PermissionUser> userOptional = permissionUserRepository.find(uniqueId);
    userOptional.ifPresent(permissionUser -> permissionUser.removePermissionAttachment(plugin));
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onWorldChange(PlayerChangedWorldEvent event) {
    var player = event.getPlayer();
    var uniqueId = player.getUniqueId();
    Optional<PermissionUser> userOptional = permissionUserRepository.find(uniqueId);
    userOptional.ifPresent(permissionUser -> permissionUser.calculatePermissionAttachment(plugin));
  }
}
