package com.felixklauke.chiara.bukkit.user;

import com.felixklauke.chiara.bukkit.group.GroupTable;
import com.felixklauke.chiara.bukkit.permission.Permission;
import com.felixklauke.chiara.bukkit.permission.PermissionEntity;
import com.felixklauke.chiara.bukkit.permission.PermissionStatus;
import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.felixklauke.chiara.bukkit.permission.WorldPermissionTable;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.UUID;
import org.bukkit.plugin.PluginManager;

public final class PermissionUser extends PermissionEntity {
  private final UUID id;
  private final PluginManager pluginManager;

  PermissionUser(
    UUID id,
    PermissionTable permissions,
    GroupTable groups,
    WorldPermissionTable worldPermissions,
    Metadata metadata,
    PluginManager pluginManager
  ) {
    super(permissions, groups, worldPermissions, metadata);
    this.id = id;
    this.pluginManager = pluginManager;
  }

  public UUID id() {
    return id;
  }

  @Override
  public boolean setPermissionStatus(
    Permission permission,
    PermissionStatus status
  ) {
    Preconditions.checkNotNull(permission);
    Preconditions.checkNotNull(status);
    var permissionChange = callPermissionChangeEvent(permission, status);
    if (permissionChange.isCancelled()) {
      return false;
    }
    return super.setPermissionStatus(permission, status);
  }

  @Override
  public boolean setWorldPermissionStatus(
    Permission permission,
    PermissionStatus status,
    String world
  ) {
    Preconditions.checkNotNull(permission);
    Preconditions.checkNotNull(status);
    Preconditions.checkNotNull(world);
    var permissionChange = callPermissionChangeEvent(permission, status);
    if (permissionChange.isCancelled()) {
      return false;
    }
    return super.setWorldPermissionStatus(permission, status, world);
  }

  private PermissionUserChangeEvent callPermissionChangeEvent(
    Permission perm,
    PermissionStatus status
  ) {
    var permissionChange = PermissionUserChangeEvent.of(
      this,
      perm,
      status
    );
    pluginManager.callEvent(permissionChange);
    return permissionChange;
  }
}
