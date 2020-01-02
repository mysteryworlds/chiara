package com.felixklauke.chiara.bukkit.permission;

import com.felixklauke.chiara.bukkit.group.GroupTable;
import com.felixklauke.chiara.bukkit.group.PermissionGroup;
import com.google.common.base.Preconditions;
import java.util.Set;

public abstract class PermissionEntity {
  private final PermissionTable permissions;
  private final GroupTable groups;
  private final WorldPermissionTable worldPermissions;

  protected PermissionEntity(
    PermissionTable permissions,
    GroupTable groups,
    WorldPermissionTable worldPermissions
  ) {
    this.permissions = permissions;
    this.groups = groups;
    this.worldPermissions = worldPermissions;
  }

  public Set<PermissionGroup> groups() {
    return groups.groups();
  }

  public boolean addGroup(PermissionGroup permissionGroup) {
    Preconditions.checkNotNull(permissionGroup);
    return groups.add(permissionGroup);
  }

  public boolean removeGroup(PermissionGroup permissionGroup) {
    Preconditions.checkNotNull(permissionGroup);
    return groups.remove(permissionGroup);
  }

  public PermissionTable calculateEffectivePermissions() {
    var groupPermissions = groups.calculateEffectivePermissions();
    return groupPermissions.merge(permissions);
  }

  public PermissionTable calculateEffectivePermissions(String world) {
    Preconditions.checkNotNull(world);
    var groupPermissions = groups.calculateEffectivePermissions(world);
    var worldPermissions = this.worldPermissions
      .calculateWorldPermissions(world);
    return groupPermissions.merge(permissions)
      .merge(worldPermissions);
  }

  public boolean hasPermission(String permission) {
    var perm = Permission.of(permission);
    return calculateEffectivePermissions().statusOf(perm).booleanValue();
  }

  public boolean hasPermission(String permission, String world) {
    var perm = Permission.of(permission);
    return calculateEffectivePermissions(world).statusOf(perm).booleanValue();
  }

  protected boolean setPermissionStatus(
    Permission permission,
    PermissionStatus status
  ) {
    Preconditions.checkNotNull(permission);
    Preconditions.checkNotNull(status);
    permissions.setStatus(permission, status);
    return true;
  }

  protected boolean setWorldPermissionStatus(
    Permission permission,
    PermissionStatus status,
    String world
  ) {
    Preconditions.checkNotNull(permission);
    Preconditions.checkNotNull(status);
    Preconditions.checkNotNull(world);
    worldPermissions.setStatus(permission, status, world);
    return true;
  }
}
