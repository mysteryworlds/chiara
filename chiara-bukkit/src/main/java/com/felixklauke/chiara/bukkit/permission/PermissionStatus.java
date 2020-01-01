package com.felixklauke.chiara.bukkit.permission;

public enum PermissionStatus {
  ALLOWED(true),
  DECLINED(false),
  NOT_SET(false);

  private final boolean booleanValue;

  PermissionStatus(boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  public boolean booleanValue() {
    return booleanValue;
  }

  public static PermissionStatus of(boolean booleanValue) {
    return booleanValue ? ALLOWED : DECLINED;
  }
}
