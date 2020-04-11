package com.felixklauke.chiara.bukkit.group;

import com.felixklauke.chiara.bukkit.permission.Permission;
import com.felixklauke.chiara.bukkit.permission.PermissionEntity;
import com.felixklauke.chiara.bukkit.permission.PermissionStatus;
import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.felixklauke.chiara.bukkit.permission.WorldPermissionTable;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.bukkit.plugin.PluginManager;

public final class PermissionGroup extends PermissionEntity {
  private final String name;
  private final PluginManager pluginManager;

  PermissionGroup(
    String name,
    PermissionTable permissions,
    GroupTable inheritedGroups,
    WorldPermissionTable worldPermissions,
    Metadata metadata,
    PluginManager pluginManager
  ) {
    super(permissions, inheritedGroups, worldPermissions, metadata);
    this.name = name;
    this.pluginManager = pluginManager;
  }

  public String name() {
    return name;
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

  private PermissionGroupChangeEvent callPermissionChangeEvent(
    Permission perm,
    PermissionStatus status
  ) {
    var permissionChange = PermissionGroupChangeEvent.of(
      this,
      perm,
      status
    );
    pluginManager.callEvent(permissionChange);
    return permissionChange;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("name", name)
      .add("pluginManager", pluginManager)
      .toString();
  }
}
