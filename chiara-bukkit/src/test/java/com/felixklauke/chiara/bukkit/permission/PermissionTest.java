package com.felixklauke.chiara.bukkit.permission;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

final class PermissionTest {
  private static final String TEST_PERMISSION_NAME = "test.permission";
  private Permission permission;

  @BeforeEach
  void setUp() {
    permission = Permission.of(TEST_PERMISSION_NAME);
  }

  @Test
  void testName() {
    var name = permission.name();
    assertEquals(TEST_PERMISSION_NAME, name);
  }

  @Test
  void testEquality() {
    var clonedPermissions = Permission.of(TEST_PERMISSION_NAME);
    assertEquals(permission, clonedPermissions);
    assertEquals(permission.hashCode(), clonedPermissions.hashCode());
  }
}