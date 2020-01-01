package com.felixklauke.chiara.bukkit.user;

import com.google.common.base.Preconditions;

public final class PermissionUserNotFoundException extends RuntimeException {
  private PermissionUserNotFoundException(String message) {
    super(message);
  }

  public static PermissionUserNotFoundException withMessage(String message) {
    Preconditions.checkNotNull(message);
    return new PermissionUserNotFoundException(message);
  }
}
