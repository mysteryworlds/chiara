package de.felixklauke.chiara.bukkit.permission;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;

public abstract class AbstractPermissionContainer implements PermissionContainer {

  private final Map<String, Boolean> permissions = Maps.newConcurrentMap();
  private final Map<String, Map<String, Boolean>> worldPermissions = Maps.newConcurrentMap();

  @Override
  public Map<String, Boolean> getBasePermissions() {
    return Maps.newHashMap(permissions);
  }

  @Override
  public void setPermission(String permission, boolean value) {
    Preconditions.checkNotNull(permission, "Permission should not be null");

    permissions.put(permission, value);
  }

  @Override
  public void setWorldPermission(String permission, String world, boolean value) {
    Preconditions.checkNotNull(permission, "Permission should not be null");
    Preconditions.checkNotNull(world, "World should not be null");

    Map<String, Boolean> currentWorldPermissions = worldPermissions
      .computeIfAbsent(world, key -> Maps.newConcurrentMap());
    currentWorldPermissions.put(permission, value);
  }

  @Override
  public void removePermission(String permission) {
    Preconditions.checkNotNull(permission, "Permission should not be null");

    permissions.remove(permission);
  }

  @Override
  public void removeWorldPermission(String permission, String world) {
    Preconditions.checkNotNull(permission, "Permission should not be null");
    Preconditions.checkNotNull(world, "World should not be null");

    Map<String, Boolean> currentWorldPermissions = worldPermissions
      .computeIfAbsent(world, key -> Maps.newConcurrentMap());

    currentWorldPermissions.remove(permission);
  }

  @Override
  public Map<String, Boolean> getWorldBasePermissions(String world) {

    Map<String, Boolean> permissions = worldPermissions.get(world);
    return permissions != null ? Maps.newHashMap(permissions) : Maps.newHashMap();
  }

  @Override
  public boolean hasWorldPermission(String world, String permission) {

    Map<String, Boolean> currentWorldPermissions = getEffectiveWorldPermissions(world);
    return currentWorldPermissions.getOrDefault(permission, false);
  }
}
