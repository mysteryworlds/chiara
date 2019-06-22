package de.felixklauke.chiara.bukkit.service;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface PermissionService {

    /**
     * Register the given player and calculate his permissions.
     *
     * @param player The player.
     */
    void registerPlayer(Player player);

    /**
     * Unregister the given player and delete his permissions.
     *
     * @param player The player.
     */
    void unregisterPlayer(Player player);

    /**
     * Refresh the permissions of the given player.
     *
     * @param player The player.
     */
    void refreshPlayer(Player player);

    /**
     * Reload all permissions.
     */
    void reloadPermissions();

    /**
     * Get the names of all groups.
     *
     * @return The names of the groups.
     */
    String[] getGroups();

    /**
     * Get a list of groups of the player with the given unique id.
     *
     * @param uniqueId The unique id of the players.
     * @return The groups.
     */
    List<String> getGroups(UUID uniqueId);

    /**
     * Set the given permission of the given user to the given value.
     *
     * @param uniqueId The unique id of the user.
     * @param permission The permission.
     * @param value The value.
     */
    void setUserPermission(UUID uniqueId, String permission, boolean value);

    /**
     * Set the given permission of the given player to the given value in the given world.
     *
     * @param uniqueId The unique id of the user.
     * @param world The name of the world.
     * @param permission The permission.
     * @param value The value.
     */
    void setUserPermission(UUID uniqueId, String world, String permission, boolean value);

    /**
     * Unset the given permission for the given player.
     *
     * @param uniqueId The unique id of the player.
     * @param permission The permission.
     */
    void unsetUserPermission(UUID uniqueId, String permission);

    /**
     * Unset the given permission for the given user in the given world.
     *
     * @param uniqueId The unique id.
     * @param world The world.
     * @param permission The permission.
     */
    void unsetUserPermission(UUID uniqueId, String world, String permission);

    /**
     * Check if the given group has the permission with the given value.
     *
     * @param group The group.
     * @param permission The permission.
     * @param value The value.
     * @return If the group has the permission.
     */
    boolean hasGroupPermission(String group, String permission, boolean value);

    /**
     * Check if the given group has the permission with the given value in the given world.
     *
     * @param group The group.
     * @param world The world.
     * @param permission The permission.
     * @param value The value.
     * @return If the group has the permission.
     */
    boolean hasGroupPermission(String group, String world, String permission, boolean value);

    /**
     * Set the value of the given permission for the given group.
     *
     * @param group The group.
     * @param permission The permission.
     * @param value The value.
     */
    void setGroupPermission(String group, String permission, boolean value);

    /**
     * Set the value for the given permission for the given group in the given world.
     *
     * @param group The group.
     * @param world The world.
     * @param permission The permission.
     * @param value The value.
     */
    void setGroupPermission(String group, String world, String permission, boolean value);

    /**
     * Unset the given permission for the given group.
     *
     * @param group The group.
     * @param permission The permission.
     */
    void unsetGroupPermission(String group, String permission);

    /**
     * Unset the given permission for the given group in the given world.
     *
     * @param group The group.
     * @param world The world.
     * @param permission The permission.
     */
    void unsetGroupPermission(String group, String world, String permission);

    /**
     * Add the given player to the given group.
     *
     * @param uniqueId The unique id of the user.
     * @param group The name of the group.
     */
    void addUserGroup(UUID uniqueId, String group);

    /**
     * Remove the given user from the given group.
     *
     * @param uniqueId The unique id.
     * @param group The group.
     */
    void removeUserGroup(UUID uniqueId, String group);
}
