package de.felixklauke.chiara.bukkit.user;

import de.felixklauke.chiara.bukkit.group.PermissionGroup;
import de.felixklauke.chiara.bukkit.permission.PermissionContainer;
import java.util.Set;
import java.util.UUID;
import org.bukkit.World;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

public interface PermissionUser extends PermissionContainer {

  /**
   * Get the unique id of the permissions user.
   *
   * @return The unique id.
   */
  UUID getUniqueId();

  /**
   * Get the permission groups.
   *
   * @return The permissions groups.
   */
  Set<PermissionGroup> getPermissionsGroups();

  /**
   * Add the user to the given group.
   *
   * @param group The group.
   */
  void addGroup(PermissionGroup group);

  void calculatePermissionAttachment(Plugin plugin);

  void removePermissionAttachment(Plugin plugin);

  boolean isInGroup(PermissionGroup permissionGroup);

  void removeGroup(PermissionGroup permissionGroup);
}
