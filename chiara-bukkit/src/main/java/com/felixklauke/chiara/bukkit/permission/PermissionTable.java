package com.felixklauke.chiara.bukkit.permission;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.permissions.PermissionAttachment;

public final class PermissionTable {
  private final Map<Permission, PermissionStatus> permissions;

  private PermissionTable(Map<Permission, PermissionStatus> permissions) {
    this.permissions = permissions;
  }

  public static PermissionTable withPermissions(List<Permission> permissions) {
    Preconditions.checkNotNull(permissions);
    var permissionMap = permissions.parallelStream()
      .collect(Collectors.toMap(key -> key, value -> PermissionStatus.ALLOWED));
    return PermissionTable.withPermissions(permissionMap);
  }

  public static PermissionTable withPermissions(
    Map<Permission, PermissionStatus> permissions
  ) {
    Preconditions.checkNotNull(permissions);
    return new PermissionTable(Maps.newHashMap(permissions));
  }

  public static PermissionTable empty() {
    return withPermissions(Maps.newHashMap());
  }

  /**
   * Get the status of the given permission.
   *
   * @param permission Permission.
   * @return Permission status.
   */
  public PermissionStatus statusOf(Permission permission) {
    Preconditions.checkNotNull(permission);
    return permissions.getOrDefault(permission, PermissionStatus.NOT_SET);
  }

  /**
   * Merge the source table into this table.
   *
   * @param source Source table.
   * @return Target table.
   */
  public PermissionTable merge(PermissionTable source) {
    Preconditions.checkNotNull(source);
    var permissionTable = PermissionTable.withPermissions(permissions);
    source.permissions.forEach(permissionTable::setStatus);
    return permissionTable;
  }

  /**
   * Update the status of a permission.
   *
   * @param permission Permission.
   * @param status     Permission status.
   */
  public void setStatus(Permission permission, PermissionStatus status) {
    Preconditions.checkNotNull(permission);
    Preconditions.checkNotNull(status);
    if (status == PermissionStatus.NOT_SET) {
      unsetPermission(permission);
    } else {
      permissions.put(permission, status);
    }
  }

  private void unsetPermission(Permission permission) {
    permissions.remove(permission);
  }

  public void apply(PermissionAttachment permissionAttachment) {
    Preconditions.checkNotNull(permissionAttachment);
    Map<String, Boolean> permissionsMap = tryExtractPermissionsMap(
      permissionAttachment);
    clonePermissions(permissionsMap);
  }

  private void clonePermissions(Map<String, Boolean> permissionsMap) {
    permissionsMap.clear();
    permissions.forEach((permission, status) ->
      permissionsMap.put(permission.name(), status.booleanValue())
    );
  }

  private Map<String, Boolean> tryExtractPermissionsMap(
    PermissionAttachment permissionAttachment
  ) {
    try {
      var permissionsField = permissionAttachment.getClass()
        .getDeclaredField("permissions");
      permissionsField.setAccessible(true);
      return (Map<String, Boolean>) permissionsField.get(permissionAttachment);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
      return Maps.newHashMap();
    }
  }

  public boolean isEmpty() {
    return permissions.isEmpty();
  }
}
