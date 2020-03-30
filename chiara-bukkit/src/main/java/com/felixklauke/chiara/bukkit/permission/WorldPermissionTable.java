package com.felixklauke.chiara.bukkit.permission;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

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

  public static WorldPermissionTable withMapWorldPermissions(
    Map<String, Map<Permission, PermissionStatus>> worldPermissions
  ) {
    Preconditions.checkNotNull(worldPermissions);
    var permissionTables = worldPermissions.entrySet().stream()
      .collect(Collectors.toMap(
        Entry::getKey,
        value -> PermissionTable.withPermissions(value.getValue())
      ));
    return new WorldPermissionTable(permissionTables);
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("worldPermissions", worldPermissions)
      .toString();
  }
}
