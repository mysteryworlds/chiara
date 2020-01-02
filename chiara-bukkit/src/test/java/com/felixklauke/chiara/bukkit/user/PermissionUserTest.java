package com.felixklauke.chiara.bukkit.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.felixklauke.chiara.bukkit.group.GroupTable;
import com.felixklauke.chiara.bukkit.permission.Permission;
import com.felixklauke.chiara.bukkit.permission.PermissionStatus;
import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.felixklauke.chiara.bukkit.permission.WorldPermissionTable;
import java.util.UUID;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class PermissionUserTest {

  private static final Permission TEST_PERMISSION = Permission
    .of("test-permission");
  private static final String TEST_WORLD = "TestWorld";

  @Mock
  private PluginManager pluginManager;

  private PermissionTable permissionTable;
  private PermissionUser permissionUser;
  private GroupTable groups;
  private WorldPermissionTable worldPermissions;

  @BeforeEach
  void setUp() {
    permissionTable = PermissionTable.empty();
    groups = GroupTable.empty();
    worldPermissions = WorldPermissionTable.empty();
    permissionUser = new PermissionUser(
      UUID.randomUUID(),
      permissionTable,
      groups,
      worldPermissions,
      pluginManager
    );
  }

  @Test
  void testCalculateEffectivePermissionsEmpty() {
    var permissionTable = permissionUser.calculateEffectivePermissions();
    assertTrue(permissionTable.isEmpty());
  }

  @Test
  void testCalculateEffectivePermissions() {
    permissionTable.setStatus(TEST_PERMISSION, PermissionStatus.ALLOWED);

    var permissionTable = permissionUser.calculateEffectivePermissions();
    var testPermissionStatus = permissionTable.statusOf(TEST_PERMISSION);

    assertEquals(PermissionStatus.ALLOWED, testPermissionStatus);
  }

  @Test
  void testCalculateEffectiveWorldPermissions() {
  }

  @Test
  void testHasPermissions() {
    var hasPermission = permissionUser.hasPermission(TEST_PERMISSION.name());
    assertFalse(hasPermission);

    permissionUser
      .setPermissionStatus(TEST_PERMISSION, PermissionStatus.ALLOWED);
    hasPermission = permissionUser.hasPermission(TEST_PERMISSION.name());
    assertTrue(hasPermission);
  }

  @Test
  void testHasWorldPermission() {
    var hasPermission = permissionUser
      .hasPermission(TEST_PERMISSION.name(), TEST_WORLD);
    assertFalse(hasPermission);

    permissionUser.setWorldPermissionStatus(TEST_PERMISSION,
      PermissionStatus.ALLOWED, TEST_WORLD);
    hasPermission = permissionUser
      .hasPermission(TEST_PERMISSION.name(), TEST_WORLD);
    assertTrue(hasPermission);
  }
}