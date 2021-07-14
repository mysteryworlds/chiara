package com.mysteryworlds.chiara.user;

import com.mysteryworlds.chiara.permission.Permission;
import com.mysteryworlds.chiara.permission.PermissionChangeEvent;
import com.mysteryworlds.chiara.permission.PermissionStatus;
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

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public PermissionUser user() {
    return user;
  }

  @Override
  public HandlerList getHandlers() {
    return getHandlerList();
  }
}
