package com.felixklauke.chiara.bukkit.permission;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

final class PermissionStatusTest {

  @Test
  void testBooleanValues() {
    assertTrue(PermissionStatus.ALLOWED.booleanValue());
    assertFalse(PermissionStatus.DECLINED.booleanValue());
    assertFalse(PermissionStatus.NOT_SET.booleanValue());
  }

  @Test
  void testOfAllowed() {
    var permissionStatus = PermissionStatus.of(true);
    assertEquals(PermissionStatus.ALLOWED, permissionStatus);
  }

  @Test
  void testOfDeclined() {
    var permissionStatus = PermissionStatus.of(false);
    assertEquals(PermissionStatus.DECLINED, permissionStatus);
  }
}