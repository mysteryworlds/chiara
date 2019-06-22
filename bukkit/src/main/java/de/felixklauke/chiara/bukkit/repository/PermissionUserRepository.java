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

    /**
     * Reload all users and invalidate cache.
     */
    void reloadUsers();

    /**
     * Save all user data.
     */
    void saveUsers();

    /**
     * Create a new user with the given unique id.
     *
     * @param uniqueId The unique id.
     * @return The user.
     */
    PermissionUser createUser(UUID uniqueId);

    /**
     * Save the data of the given permission user.
     *
     * @param permissionUser The permission user.
     */
    void saveUser(PermissionUser permissionUser);
}
