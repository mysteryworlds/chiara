package com.felixklauke.chiara.bukkit.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.felixklauke.chiara.bukkit.permission.Permission;
import com.felixklauke.chiara.bukkit.permission.PermissionStatus;
import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.felixklauke.chiara.bukkit.permission.WorldPermissionTable;
import java.util.List;
import java.util.Map;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class PermissionGroupTest {
  private static final Permission TEST_PERMISSION = Permission
    .of("test-permission");
  private static final String TEST_GROUP_NAME = "TestGroup";
  private static final String TEST_WORLD = "TestWorld";

  @Mock
  private PluginManager pluginManager;

  private PermissionTable permissionTable;
  private PermissionGroup permissionGroup;
  private GroupTable groups;
  private WorldPermissionTable worldPermissions;

  @BeforeEach
  void setUp() {
    permissionTable = PermissionTable.empty();
    groups = GroupTable.empty();
    worldPermissions = WorldPermissionTable.empty();
    permissionGroup = new PermissionGroup(
      "TestGroup",
      permissionTable,
      groups,
      worldPermissions,
      pluginManager
    );
  }

  @Test
  void testGetName() {
    var name = permissionGroup.name();
    assertEquals(TEST_GROUP_NAME, name);
  }

  @Test
  void testCalculateEffectivePermissionsEmpty() {
    var permissionTable = permissionGroup.calculateEffectivePermissions();
    assertTrue(permissionTable.isEmpty());
  }

  @Test
  void testCalculateEffectivePermissions() {
    permissionTable.setStatus(TEST_PERMISSION, PermissionStatus.ALLOWED);

    var permissionTable = permissionGroup.calculateEffectivePermissions();
    var testPermissionStatus = permissionTable.statusOf(TEST_PERMISSION);

    assertEquals(PermissionStatus.ALLOWED, testPermissionStatus);
  }

  @Test
  void testCalculateEffectiveWorldPermissions() {
  }

  @Test
  void testHasPermissions() {
    var hasPermission = permissionGroup.hasPermission(TEST_PERMISSION.name());
    assertFalse(hasPermission);

    permissionGroup
      .setPermissionStatus(TEST_PERMISSION, PermissionStatus.ALLOWED);
    hasPermission = permissionGroup.hasPermission(TEST_PERMISSION.name());
    assertTrue(hasPermission);
  }

  @Test
  void testHasWorldPermission() {
    var hasPermission = permissionGroup
      .hasPermission(TEST_PERMISSION.name(), TEST_WORLD);
    assertFalse(hasPermission);

    permissionGroup.setWorldPermissionStatus(TEST_PERMISSION,
      PermissionStatus.ALLOWED, TEST_WORLD);
    hasPermission = permissionGroup
      .hasPermission(TEST_PERMISSION.name(), TEST_WORLD);
    assertTrue(hasPermission);
  }

  @Test
  void testPermissionGroupOverride() {
    permissionGroup
      .setPermissionStatus(TEST_PERMISSION, PermissionStatus.DECLINED);

    var overriddenGroup = new PermissionGroup(
      "TestGroup2",
      PermissionTable
        .withPermissions(Map.of(TEST_PERMISSION, PermissionStatus.ALLOWED)),
      GroupTable.withGroups(List.of(permissionGroup)),
      WorldPermissionTable.empty(),
      pluginManager
    );
    var hasPermission = overriddenGroup.hasPermission(TEST_PERMISSION.name());
    assertTrue(hasPermission);
  }

  @Test
  void testSetPermissionStatus() {
  }

  @Test
  void setWorldPermissionStatus() {
  }
}