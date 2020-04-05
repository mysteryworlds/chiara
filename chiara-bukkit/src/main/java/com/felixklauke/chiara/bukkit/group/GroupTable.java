package com.felixklauke.chiara.bukkit.group;

import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public final class GroupTable {
  private final List<PermissionGroup> groups;

  private GroupTable(List<PermissionGroup> groups) {
    this.groups = groups;
  }

  public PermissionTable calculateEffectivePermissions() {
    var permissionTable = PermissionTable.empty();
    for (var group : groups) {
      PermissionTable groupPermissions = group.calculateEffectivePermissions();
      permissionTable = permissionTable.merge(groupPermissions);
    }
    return permissionTable;
  }

  public PermissionTable calculateEffectivePermissions(String world) {
    var permissionTable = PermissionTable.empty();
    for (var group : groups) {
      PermissionTable groupPermissions = group
        .calculateEffectivePermissions(world);
      permissionTable = permissionTable.merge(groupPermissions);
    }
    return permissionTable;
  }

  public Set<PermissionGroup> groups() {
    return Set.copyOf(groups);
  }

  public boolean add(PermissionGroup permissionGroup) {
    return groups.add(permissionGroup);
  }

  public boolean remove(PermissionGroup permissionGroup) {
    return groups.remove(permissionGroup);
  }

  public int count() {
    return groups.size();
  }

  public static GroupTable withGroups(List<PermissionGroup> groups) {
    Preconditions.checkNotNull(groups);
    return new GroupTable(Lists.newArrayList(groups));
  }

  public static GroupTable empty() {
    return withGroups(Lists.newArrayList());
  }

  public static GroupTable withGroups(PermissionGroup... defaultGroups) {
    Preconditions.checkNotNull(defaultGroups);
    return withGroups(Arrays.asList(defaultGroups));
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("groups", groups)
      .toString();
  }
}
