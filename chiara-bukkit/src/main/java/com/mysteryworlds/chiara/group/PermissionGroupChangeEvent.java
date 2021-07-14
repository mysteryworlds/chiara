package com.mysteryworlds.chiara.group;

import com.mysteryworlds.chiara.permission.Permission;
import com.mysteryworlds.chiara.permission.PermissionChangeEvent;
import com.mysteryworlds.chiara.permission.PermissionStatus;
import com.google.common.base.Preconditions;
import org.bukkit.event.HandlerList;

public final class PermissionGroupChangeEvent extends PermissionChangeEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final PermissionGroup group;

  private PermissionGroupChangeEvent(
    PermissionGroup group,
    Permission permission,
    PermissionStatus permissionStatus
  ) {
    super(permission, permissionStatus);
    this.group = group;
  }

  public static PermissionGroupChangeEvent of(
    PermissionGroup group,
    Permission permission,
    PermissionStatus permissionStatus
  ) {
    Preconditions.checkNotNull(group);
    Preconditions.checkNotNull(permission);
    Preconditions.checkNotNull(permissionStatus);
    return new PermissionGroupChangeEvent(group, permission, permissionStatus);
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public PermissionGroup group() {
    return group;
  }

  @Override
  public HandlerList getHandlers() {
    return getHandlerList();
  }
}
