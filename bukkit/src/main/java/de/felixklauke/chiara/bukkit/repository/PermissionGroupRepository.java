package de.felixklauke.chiara.bukkit.repository;

import de.felixklauke.chiara.bukkit.model.PermissionGroup;
import java.util.List;

public interface PermissionGroupRepository {

  /**
   * Find the group with the given name.
   *
   * @param groupName The name of the group.
   * @return The group.
   */
  PermissionGroup findGroup(String groupName);

  void writeGroups();

  /**
   * Reload all groups and invalidate cache.
   */
  void reloadGroups();

  /**
   * Find all groups.
   *
   * @return The groups.
   */
  List<PermissionGroup> findGroups();

  /**
   * Create a new group with the given name.
   *
   * @param group The group name.
   * @return The group.
   */
  PermissionGroup createGroup(String group);

  /**
   * Save the given group.
   *
   * @param permissionGroup The group.
   */
  void saveGroup(PermissionGroup permissionGroup);
}
