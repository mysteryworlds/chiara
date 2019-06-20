package de.felixklauke.chiara.bukkit.repository;

import de.felixklauke.chiara.bukkit.model.PermissionGroup;

public interface PermissionGroupRepository {

    /**
     * Find the group with the given name.
     *
     * @param groupName The name of the group.
     * @return The group.
     */
    PermissionGroup findGroup(String groupName);
}
