package com.felixklauke.chiara.bukkit.permission;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;

public final class WorldPermissionTable {
  private final Map<String, PermissionTable> worldPermissions;

  private WorldPermissionTable(
    Map<String, PermissionTable> worldPermissions
  ) {
    this.worldPermissions = worldPermissions;
  }

  public static WorldPermissionTable withWorldPermissions(
    Map<String, PermissionTable> worldPermissions
  ) {
    Preconditions.checkNotNull(worldPermissions);
    return new WorldPermissionTable(Maps.newHashMap(worldPermissions));
  }

  public static WorldPermissionTable empty() {
    return withWorldPermissions(Maps.newHashMap());
  }

  public PermissionTable calculateWorldPermissions(String worldName) {
    Preconditions.checkNotNull(worldName);
    return worldPermissions.getOrDefault(worldName, PermissionTable.empty());
  }

  public void setStatus(
    Permission perm,
    PermissionStatus status,
    String world
  ) {
    Preconditions.checkNotNull(perm);
    Preconditions.checkNotNull(status);
    Preconditions.checkNotNull(world);
    var permissionTable = worldPermissions.get(world);
    if (permissionTable == null) {
      permissionTable = PermissionTable.empty();
      worldPermissions.put(world, permissionTable);
    }
    permissionTable.setStatus(perm, status);
  }
}
