package com.felixklauke.chiara.bukkit.user;

import com.felixklauke.chiara.bukkit.group.GroupTable;
import com.felixklauke.chiara.bukkit.group.PermissionGroup;
import com.felixklauke.chiara.bukkit.permission.Permission;
import com.felixklauke.chiara.bukkit.permission.PermissionStatus;
import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.felixklauke.chiara.bukkit.permission.WorldPermissionTable;
import com.google.common.base.Preconditions;
import java.util.Set;
import java.util.UUID;

public final class PermissionUser {
  private final UUID id;
  private final PermissionTable permissions;
  private final GroupTable groups;
  private final WorldPermissionTable worldPermissions;

  private PermissionUser(
    UUID id,
    PermissionTable permissions,
    GroupTable groups,
    WorldPermissionTable worldPermissions
  ) {
    this.id = id;
    this.permissions = permissions;
    this.groups = groups;
    this.worldPermissions = worldPermissions;
  }

  public UUID id() {
    return id;
  }

  public PermissionTable calculateEffectivePermissions() {
    var groupPermissions = groups.calculateEffectivePermissions();
    return groupPermissions.merge(permissions);
  }

  public PermissionTable calculateEffectivePermissions(String world) {
    Preconditions.checkNotNull(world);
    var groupPermissions = groups.calculateEffectivePermissions(world);
    var worldPermissions = this.worldPermissions.calculateWorldPermissions(world);
    return groupPermissions.merge(permissions)
      .merge(worldPermissions);
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

  public boolean hasPermissions(String permission) {
    Preconditions.checkNotNull(permission);
    var perm = Permission.of(permission);
    return calculateEffectivePermissions().statusOf(perm).booleanValue();
  }

  public boolean hasPermissions(String permission, String world) {
    Preconditions.checkNotNull(permission);
    Preconditions.checkNotNull(world);
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
    worldPermissions.setStatus(perm, status, world);
    return true;
  }
}
