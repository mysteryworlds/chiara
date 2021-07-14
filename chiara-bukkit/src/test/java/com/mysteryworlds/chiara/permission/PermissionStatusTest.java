package com.mysteryworlds.chiara.permission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
