package de.felixklauke.chiara.bukkit.service;

import com.google.common.collect.Maps;
import com.google.common.reflect.Reflection;
import de.felixklauke.chiara.bukkit.util.ReflectionUtils;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

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

        return Maps.newHashMap();
    }
}
