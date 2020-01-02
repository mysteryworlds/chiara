package com.felixklauke.chiara.bukkit.permission;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class PermissionChangeEvent extends Event implements
  Cancellable {
  private boolean cancelled;
  private final Permission permission;
  private final PermissionStatus permissionStatus;

  protected PermissionChangeEvent(
    Permission permission,
    PermissionStatus permissionStatus
  ) {
    this.permission = permission;
    this.permissionStatus = permissionStatus;
  }

  public Permission permission() {
    return permission;
  }

  public PermissionStatus permissionStatus() {
    return permissionStatus;
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
