package de.felixklauke.chiara.bukkit.permission;

import java.util.Map;

public interface PermissionContainer {

  /**
   * Get the basic permissions of this specific container.
   *
   * @return The basic permissions.
   */
  Map<String, Boolean> getBasePermissions();

  /**
   * Get the overall world independent effective permissions.
   *
   * @return The effective permissions.
   */
  Map<String, Boolean> getEffectivePermissions();

  /**
   * Get the effective permissions including world overrides.
   *
   * @param world The worlds name.
   * @return The effective permissions.
   */
  Map<String, Boolean> getEffectivePermissions(String world);

  /**
   * Get the world base permissions this container has in a given world.
   *
   * @param world The worlds name.
   * @return The world base permissions.
   */
  Map<String, Boolean> getWorldBasePermissions(String world);

  void setPermission(String permission, boolean value);

  void setWorldPermission(String permission, String world, boolean value);

  void removePermission(String permission);

  void removeWorldPermission(String permission, String world);

  boolean hasWorldPermission(String world, String permission);

  boolean hasPermission(String permission);
}
