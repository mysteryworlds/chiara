package com.felixklauke.chiara.bukkit.group;

import com.felixklauke.chiara.bukkit.permission.Permission;
import com.felixklauke.chiara.bukkit.permission.PermissionStatus;
import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.felixklauke.chiara.bukkit.permission.WorldPermissionTable;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;

public final class PermissionGroup {
  private final String name;
  private final PermissionTable permissions;
  private final GroupTable inheritedGroups;
  private final WorldPermissionTable worldPermissions;

  private PermissionGroup(
    String name,
    PermissionTable permissions,
    GroupTable inheritedGroups,
    WorldPermissionTable worldPermissions
  ) {
    this.name = name;
    this.permissions = permissions;
    this.inheritedGroups = inheritedGroups;
    this.worldPermissions = worldPermissions;
  }

  public String name() {
    return name;
  }

  public PermissionTable calculateEffectivePermissions() {
    var groupPermissions = inheritedGroups.calculateEffectivePermissions();
    return groupPermissions.merge(permissions);
  }

  public PermissionTable calculateEffectivePermissions(String world) {
    Preconditions.checkNotNull(world);
    var groupPermissions = inheritedGroups.calculateEffectivePermissions(world);
    var worldPermissions = this.worldPermissions.calculateWorldPermissions(world);
    return groupPermissions.merge(permissions)
      .merge(worldPermissions);
  }

  public boolean hasPermissions(String permission) {
    var perm = Permission.of(permission);
    return calculateEffectivePermissions().statusOf(perm).booleanValue();
  }

  public boolean hasPermissions(String permission, String world) {
    var perm = Permission.of(permission);
    return calculateEffectivePermissions(world).statusOf(perm).booleanValue();
  }

  public boolean setPermissionStatus(
    String permission,
    PermissionStatus status
  ) {
    Preconditions.checkNotNull(permission);
    Preconditions.checkNotNull(status);
    var perm = Permission.of(permission);
    var permissionChange = callPermissionChangeEvent(perm, status);
    if (permissionChange.isCancelled()) {
      return false;
    }
    permissions.setStatus(perm, status);
    return true;
  }

  public boolean setWorldPermissionStatus(
    String permission,
    PermissionStatus status,
    String world
  ) {
    Preconditions.checkNotNull(permission);
    Preconditions.checkNotNull(status);
    Preconditions.checkNotNull(world);
    var perm = Permission.of(permission);
    var permissionChange = callPermissionChangeEvent(perm, status);
    if (permissionChange.isCancelled()) {
      return false;
    }
    worldPermissions.setStatus(perm, status, world);
    return true;
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
    Bukkit.getPluginManager().callEvent(permissionChange);
    return permissionChange;
  }
}
