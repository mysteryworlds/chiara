package de.felixklauke.chiara.bukkit.service;

import com.google.common.collect.Maps;
import de.felixklauke.chiara.bukkit.model.PermissionGroup;
import de.felixklauke.chiara.bukkit.model.PermissionUser;
import de.felixklauke.chiara.bukkit.repository.PermissionGroupRepository;
import de.felixklauke.chiara.bukkit.repository.PermissionUserRepository;
import de.felixklauke.chiara.bukkit.util.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.IntFunction;

public class PermissionsServiceImpl implements PermissionsService {

    /**
     * All currently managed permissions attachments.
     */
    private final Map<UUID, PermissionAttachment> permissionAttachments = Maps.newConcurrentMap();
    private final Plugin plugin;
    private final PermissionGroupRepository permissionGroupRepository;
    private final PermissionUserRepository permissionUserRepository;

    public PermissionsServiceImpl(Plugin plugin, PermissionGroupRepository permissionGroupRepository, PermissionUserRepository permissionUserRepository) {
        this.plugin = plugin;
        this.permissionGroupRepository = permissionGroupRepository;
        this.permissionUserRepository = permissionUserRepository;
    }

    @Override
    public void registerPlayer(Player player) {

        UUID uniqueId = player.getUniqueId();

        if (permissionAttachments.containsKey(uniqueId)) {
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

        if (!permissionAttachments.containsKey(uniqueId)) {
            return;
        }

        // Delete permission attachment
        PermissionAttachment attachment = permissionAttachments.get(uniqueId);
        player.removeAttachment(attachment);
        permissionAttachments.remove(uniqueId);
    }

    @Override
    public void refreshPlayer(Player player) {

        UUID uniqueId = player.getUniqueId();
        if (permissionAttachments.containsKey(uniqueId)) {
            calculatePermissionAttachment(player);
            return;
        }

        registerPlayer(player);
    }

    @Override
    public void reloadPermissions() {

        permissionGroupRepository.reloadGroups();
        permissionUserRepository.reloadUsers();

        Bukkit.getOnlinePlayers().forEach(this::refreshPlayer);
    }

    @Override
    public String[] getGroups() {

        return permissionGroupRepository.findGroups()
                .stream()
                .map(PermissionGroup::getName)
                .toArray(String[]::new);
    }

    /**
     * Calculate the permission attachment for the given player.
     *
     * @param player The player.
     */
    private void  calculatePermissionAttachment(Player player) {

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

        if (permissionUser == null) {
            return new HashMap<>();
        }

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

        // Get the group
        PermissionGroup group = getGroup(groupName);

        if (group == null) {
            return new HashMap<>();
        }

        // The real permissions
        Map<String, Boolean> permissions = new LinkedHashMap<>();

        // Calculate inherited permissions
        List<String> inheritance = group.getInheritance();
        for (String parentGroup : inheritance) {
            Map<String, Boolean> parentGroupPermissions = calculateGroupPermissions(parentGroup, worldName);
            permissions.putAll(parentGroupPermissions);
        }

        // Group permissions
        Map<String, Boolean> groupPermissions = group.getPermissions();
        permissions.putAll(groupPermissions);

        // Group world permissions
        Map<String, Boolean> groupWorldPermissions = group.getWorldPermissions(worldName);
        permissions.putAll(groupWorldPermissions);

        return permissions;
    }

    /**
     * Load the permissions user with the given unique id.
     *
     * @param uniqueId The unique id of the user.
     * @return The user.
     */
    private PermissionUser getUser(UUID uniqueId) {

        return permissionUserRepository.findUser(uniqueId);
    }

    /**
     * Load the group with the given name.
     *
     * @param groupName The name of the group.
     * @return The group.
     */
    private PermissionGroup getGroup(String groupName) {

        return permissionGroupRepository.findGroup(groupName);
    }
}
