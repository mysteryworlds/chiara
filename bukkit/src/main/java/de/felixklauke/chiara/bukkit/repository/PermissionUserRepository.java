package de.felixklauke.chiara.bukkit.repository;

import de.felixklauke.chiara.bukkit.model.PermissionUser;

import java.util.UUID;

public interface PermissionUserRepository {

    /**
     * Find the user with the given unique id.
     *
     * @param uniqueId The unique id.
     * @return The user.
     */
    PermissionUser findUser(UUID uniqueId);
}
