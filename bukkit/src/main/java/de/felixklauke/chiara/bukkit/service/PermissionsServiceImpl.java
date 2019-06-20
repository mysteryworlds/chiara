package de.felixklauke.chiara.bukkit.service;

import com.google.common.collect.Maps;
import de.felixklauke.chiara.bukkit.model.PermissionGroup;
import de.felixklauke.chiara.bukkit.model.PermissionUser;
import de.felixklauke.chiara.bukkit.util.ReflectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PermissionsServiceImpl implements PermissionsService {

    /**
     * All currently managed permissions attachments.
     */
    private final Map<UUID, PermissionAttachment> permissionAttachments = Maps.newConcurrentMap();
    private final Plugin plugin;

    public PermissionsServiceImpl(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerPlayer(Player player) {

        UUID uniqueId = player.getUniqueId();

        if (!permissionAttachments.containsKey(uniqueId)) {
            unregisterPlayer(player);
        }

        // Add permission attachment
        PermissionAttachment attachment = player.addAttachment(plugin);
        permissionAttachments.put(uniqueId, attachment);

        // Calculate permission attachment
        calculatePermissionAttachment(player);
    }

    @Override
    public void unregisterPlayer(Player player) {

        UUID uniqueId = player.getUniqueId();

        if (permissionAttachments.containsKey(uniqueId)) {
            return;
        }

        // Delete permission attachment
        PermissionAttachment attachment = permissionAttachments.get(uniqueId);
        player.removeAttachment(attachment);
        permissionAttachments.remove(uniqueId);
    }

    /**
     * Calculate the permission attachment for the given player.
     *
     * @param player The player.
     */
    private void calculatePermissionAttachment(Player player) {

        UUID uniqueId = player.getUniqueId();

        PermissionAttachment attachment = permissionAttachments.get(uniqueId);

        // Calculate our own permissions
        Map<String, Boolean> permissions = calculatePermissions(player);

        Map<String, Boolean> attachmentPermissions;

        try {
            attachmentPermissions = (Map<String, Boolean>) ReflectionUtils.getFieldValue(attachment, "permissions");
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalStateException("Couldn't reflect permissions map", e);
        }

        // Set our permissions into the attachment
        attachmentPermissions.clear();
        attachmentPermissions.putAll(permissions);

        // Recalculate all permissions
        player.recalculatePermissions();
    }

    /**
     * Calculate the permissions for the given player.
     *
     * @param player The player.
     * @return The permissions.
     */
    private Map<String, Boolean> calculatePermissions(Player player) {

        // Load user
        String worldName = player.getWorld().getName();
        UUID uniqueId = player.getUniqueId();
        PermissionUser permissionUser = getUser(uniqueId);

        // The real permissions
        Map<String, Boolean> permissions = new LinkedHashMap<>();

        // Calculate group permissions
        List<String> groups = permissionUser.getGroups();
        for (String group : groups) {
            Map<String, Boolean> groupPermissions = calculateGroupPermissions(group, worldName);
            permissions.putAll(groupPermissions);
        }

        // User permissions
        Map<String, Boolean> userPermissions = permissionUser.getPermissions();
        permissions.putAll(userPermissions);

        // User world permissions
        Map<String, Boolean> userWorldPermissions = permissionUser.getWorldPermissions(worldName);
        permissions.putAll(userWorldPermissions);

        return permissions;
    }

    /**
     * Calculate the permissions of the given group in the given world.
     *
     * @param groupName The name of the group.
     * @param worldName The name of the world.
     * @return The permissions of the group.
     */
    private Map<String, Boolean> calculateGroupPermissions(String groupName, String worldName) {

        return new HashMap<>();
    }

    private PermissionUser getUser(UUID uniqueId) {

        return new PermissionUser(null, null, null, null);
    }

    private PermissionGroup getGroup(String groupName) {

        return new PermissionGroup(null, null, null, null);
    }
}
