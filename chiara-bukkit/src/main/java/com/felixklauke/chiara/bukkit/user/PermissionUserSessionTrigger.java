package com.felixklauke.chiara.bukkit.user;

import com.felixklauke.chiara.bukkit.group.PermissionGroupChangeEvent;
import javax.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PermissionUserSessionTrigger implements Listener {
  private final PermissionUserRepository userRepository;
  private final PermissionUserSessionFactory sessionFactory;
  private final PermissionUserSessionRegistry sessionRegistry;

  @Inject
  private PermissionUserSessionTrigger(
    PermissionUserRepository userRepository,
    PermissionUserSessionFactory sessionFactory,
    PermissionUserSessionRegistry sessionRegistry
  ) {
    this.userRepository = userRepository;
    this.sessionFactory = sessionFactory;
    this.sessionRegistry = sessionRegistry;
  }

  @EventHandler
  public void permissionChange(PermissionUserChangeEvent permissionChange) {
    var userId = permissionChange.user().id();
    var sessionOptional = sessionRegistry.findSession(userId);
    sessionOptional.ifPresent(PermissionUserSession::recalculatePermissions);
  }

  @EventHandler
  public void groupPermissionChange(
    PermissionGroupChangeEvent permissionChange
  ) {
    var group = permissionChange.group();
    var sessions = sessionRegistry.findSessionsByGroup(group.name());
    sessions.forEach(PermissionUserSession::recalculatePermissions);
  }

  @EventHandler
  public void beginSession(PlayerJoinEvent playerJoin) {
    var uniqueId = playerJoin.getPlayer().getUniqueId();
    var user = userRepository.findOrCreateUser(uniqueId);
    var session = sessionFactory.createSession(playerJoin.getPlayer(), user);
    sessionRegistry.register(session);
  }

  @EventHandler
  public void closeSession(PlayerQuitEvent playerQuit) {
    var uniqueId = playerQuit.getPlayer().getUniqueId();
    var sessionOptional = sessionRegistry.findSession(uniqueId);
    sessionOptional.ifPresent(session -> {
      session.close();
      sessionRegistry.removeByUser(uniqueId);
    });
  }
}
