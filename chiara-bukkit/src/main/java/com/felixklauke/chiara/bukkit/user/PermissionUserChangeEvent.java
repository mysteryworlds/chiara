package com.felixklauke.chiara.bukkit.user;

import com.felixklauke.chiara.bukkit.permission.Permission;
import com.felixklauke.chiara.bukkit.permission.PermissionChangeEvent;
import com.felixklauke.chiara.bukkit.permission.PermissionStatus;
import com.google.common.base.Preconditions;
import org.bukkit.event.HandlerList;

public final class PermissionUserChangeEvent extends
  PermissionChangeEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final PermissionUser user;

  private PermissionUserChangeEvent(
    PermissionUser user,
    Permission permission,
    PermissionStatus permissionStatus
  ) {
    super(permission, permissionStatus);
    this.user = user;
  }

  public static PermissionUserChangeEvent of(
    PermissionUser user,
    Permission permission,
    PermissionStatus permissionStatus
  ) {
    Preconditions.checkNotNull(user);
    Preconditions.checkNotNull(permission);
    Preconditions.checkNotNull(permissionStatus);
    return new PermissionUserChangeEvent(user, permission, permissionStatus);
  }

  public PermissionUser user() {
    return user;
  }

  @Override
  public HandlerList getHandlers() {
    return getHandlerList();
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
