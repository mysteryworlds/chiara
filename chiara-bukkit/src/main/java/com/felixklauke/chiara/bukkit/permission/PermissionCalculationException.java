package com.felixklauke.chiara.bukkit.permission;

import com.google.common.base.Preconditions;

public final class PermissionCalculationException extends RuntimeException {
  private PermissionCalculationException(String message) {
    super(message);
  }

  public static PermissionCalculationException withMessage(String message) {
    Preconditions.checkNotNull(message);
    return new PermissionCalculationException(message);
  }
}
