package com.mysteryworlds.chiara.permission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class PermissionTableTest {
  private Permission testPermissionAllowed;
  private Permission testPermissionDeclined;
  private Permission testPermissionNotSet;

  @BeforeEach
  void setUp() {
    testPermissionAllowed = Permission.of("test.permission");
    testPermissionDeclined = Permission.of("test.permission.declined");
    testPermissionNotSet = Permission.of("test.permission.notset");
  }

  @Test
  void testWithPermissionsAllAllowed() {
    var permissionTable = PermissionTable
      .withPermissions(List.of(testPermissionAllowed));

    assertEquals(
      PermissionStatus.ALLOWED,
      permissionTable.statusOf(testPermissionAllowed)
    );
  }

  @Test
  void testStatusOf() {
    var permissionTable = PermissionTable.withPermissions(Map.of(
      testPermissionAllowed, PermissionStatus.ALLOWED,
      testPermissionDeclined, PermissionStatus.DECLINED
    ));

    assertEquals(PermissionStatus.ALLOWED, permissionTable.statusOf(
      testPermissionAllowed
    ));
    assertEquals(PermissionStatus.DECLINED, permissionTable.statusOf(
      testPermissionDeclined
    ));
    assertEquals(PermissionStatus.NOT_SET, permissionTable.statusOf(
      testPermissionNotSet
    ));
  }

  @Test
  void testSetStatus() {
    var permissionTable = PermissionTable.withPermissions(List.of(
      testPermissionAllowed
    ));

    permissionTable.setStatus(testPermissionAllowed, PermissionStatus.DECLINED);
    assertEquals(PermissionStatus.DECLINED, permissionTable.statusOf(
      testPermissionAllowed
    ));
  }

  @Test
  void testUnsetPermission() {
    var permissionTable = PermissionTable.withPermissions(Map.of(
      testPermissionAllowed, PermissionStatus.ALLOWED
    ));

    permissionTable.setStatus(testPermissionAllowed, PermissionStatus.NOT_SET);
    assertEquals(PermissionStatus.NOT_SET, permissionTable.statusOf(
      testPermissionAllowed
    ));
  }

  @Test
  void testMergeEmpty() {
    var emptyTable = PermissionTable.empty();
    var permissionTable = PermissionTable.withPermissions(Map.of(
      testPermissionAllowed, PermissionStatus.ALLOWED,
      testPermissionDeclined, PermissionStatus.DECLINED
    ));

    var mergedTable = emptyTable.merge(permissionTable);
    assertEquals(PermissionStatus.ALLOWED, mergedTable.statusOf(
      testPermissionAllowed
    ));
    assertEquals(PermissionStatus.DECLINED, mergedTable.statusOf(
      testPermissionDeclined
    ));
    assertEquals(PermissionStatus.NOT_SET, mergedTable.statusOf(
      testPermissionNotSet
    ));
  }

  @Test
  void testMergeOverrideAllowed() {
    var defensiveTable = PermissionTable.withPermissions(Map.of(
      testPermissionAllowed, PermissionStatus.ALLOWED
    ));
    var offensiveTable = PermissionTable.withPermissions(Map.of(
      testPermissionAllowed, PermissionStatus.DECLINED
    ));

    var mergedTable = defensiveTable.merge(offensiveTable);
    assertEquals(PermissionStatus.DECLINED, mergedTable.statusOf(
      testPermissionAllowed
    ));
  }

  @Test
  void testMergeOverrideDeclined() {
    var defensiveTable = PermissionTable.withPermissions(Map.of(
      testPermissionAllowed, PermissionStatus.DECLINED
    ));
    var offensiveTable = PermissionTable.withPermissions(Map.of(
      testPermissionAllowed, PermissionStatus.ALLOWED
    ));

    var mergedTable = defensiveTable.merge(offensiveTable);
    assertEquals(PermissionStatus.ALLOWED, mergedTable.statusOf(
      testPermissionAllowed
    ));
  }

  @Test
  void testApply() {
    var permissionTable = PermissionTable.withPermissions(List.of(
      testPermissionAllowed
    ));
    var plugin = mock(Plugin.class);
    when(plugin.isEnabled()).thenReturn(true);
    var permissible = mock(Permissible.class);
    PermissionAttachment attachment = new PermissionAttachment(
      plugin,
      permissible
    );
    permissionTable.apply(attachment);
  }

  @Test
  void statusOf() {
  }

  @Test
  void withPermissions() {
  }
}
