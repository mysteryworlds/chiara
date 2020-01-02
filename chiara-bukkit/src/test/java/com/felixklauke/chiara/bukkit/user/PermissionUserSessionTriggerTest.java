package com.felixklauke.chiara.bukkit.user;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.felixklauke.chiara.bukkit.group.GroupTable;
import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.felixklauke.chiara.bukkit.permission.WorldPermissionTable;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermissionUserSessionTriggerTest {
  @Mock
  private PermissionUserRepository userRepository;
  @Mock
  private Plugin plugin;

  private PermissionUserSessionRegistry sessionRegistry = PermissionUserSessionRegistry.empty();
  private PermissionUserSessionTrigger sessionTrigger;

  @BeforeEach
  void setUp() {
    sessionTrigger = new PermissionUserSessionTrigger(
      userRepository,
      new PermissionUserSessionFactory(plugin),
      sessionRegistry
    );
  }

  @Test
  void testPermissionChange() {
  }

  @Test
  void testGroupPermissionChange() {
  }

  @Test
  void testBeginSession() {
    var playerId = UUID.randomUUID();
    var player = mock(Player.class);
    var plugin = mock(Plugin.class);

    // Needed for permission attachment initialization
    when(plugin.isEnabled()).thenReturn(true);
    var attachment = new PermissionAttachment(plugin, player);
    var permissionUser = new PermissionUser(
      playerId,
      PermissionTable.empty(),
      GroupTable.empty(),
      WorldPermissionTable.empty(),
      mock(PluginManager.class)
    );

    when(player.getUniqueId()).thenReturn(playerId);
    when(userRepository.findOrCreateUser(playerId)).thenReturn(permissionUser);
    when(player.addAttachment(Mockito.any(Plugin.class))).thenReturn(attachment);

    var playerJoin = new PlayerJoinEvent(player, null);
    sessionTrigger.beginSession(playerJoin);

    var session = sessionRegistry.findSession(playerId);
    assertTrue(session.isPresent());
  }

  @Test
  void testCloseSession() {
  }
}