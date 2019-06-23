package de.felixklauke.chiara.bukkit.vault;

import de.felixklauke.chiara.bukkit.service.PermissionService;
import de.felixklauke.chiara.bukkit.util.BukkitUtils;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class VaultPermissions extends Permission {

    private final Plugin plugin;
    private final PermissionService permissionService;

    public VaultPermissions(Plugin plugin, PermissionService permissionService) {
        this.plugin = plugin;
        this.permissionService = permissionService;
    }

    @Override
    public String getName() {

        return plugin.getName();
    }

    @Override
    public boolean isEnabled() {

        return plugin.isEnabled();
    }

    @Override
    public boolean hasSuperPermsCompat() {

        return true;
    }

    @Override
    public boolean playerHas(String world, String player, String permission) {

        Player bukkitPlayer = Bukkit.getPlayer(player);
        if (bukkitPlayer == null) {
            return false;
        }

        return bukkitPlayer.hasPermission(permission);
    }

    @Override
    public boolean playerAdd(String world, String player, String permission) {

        UUID uniqueId = BukkitUtils.getUniqueIdByPlayerName(player);

        if (world == null) {
            permissionService.setUserPermission(uniqueId, permission, true);
            return true;
        }

        permissionService.setUserPermission(uniqueId, world, permission, true);
        return true;
    }

    @Override
    public boolean playerRemove(String world, String player, String permission) {

        UUID uniqueId = BukkitUtils.getUniqueIdByPlayerName(player);

        if (world == null) {
            permissionService.unsetUserPermission(uniqueId, permission);
            return true;
        }

        permissionService.unsetUserPermission(uniqueId, world, permission);
        return true;
    }

    @Override
    public boolean groupHas(String world, String group, String permission) {

        if (world == null) {
            return permissionService.hasGroupPermission(group, permission, true);
        }

        return permissionService.hasGroupPermission(group, world, permission, true);
    }

    @Override
    public boolean groupAdd(String world, String group, String permission) {

        if (world == null) {
            permissionService.setGroupPermission(group, permission, true);
            return true;
        }

        permissionService.setGroupPermission(group, world, permission, true);
        return true;
    }

    @Override
    public boolean groupRemove(String world, String group, String permission) {

        if (world == null) {
            permissionService.unsetGroupPermission(group, permission);
            return true;
        }

        permissionService.unsetGroupPermission(group, world, permission);
        return true;
    }

    @Override
    public boolean playerInGroup(String world, String player, String group) {

        UUID uniqueId = BukkitUtils.getUniqueIdByPlayerName(player);
        return permissionService.getGroups(uniqueId).contains(group);
    }

    @Override
    public boolean playerAddGroup(String world, String player, String group) {

        UUID uniqueId = BukkitUtils.getUniqueIdByPlayerName(player);
        permissionService.addUserGroup(uniqueId, group);
        return true;
    }

    @Override
    public boolean playerRemoveGroup(String world, String player, String group) {

        UUID uniqueId = BukkitUtils.getUniqueIdByPlayerName(player);
        permissionService.removeUserGroup(uniqueId, group);
        return true;
    }

    @Override
    public String[] getPlayerGroups(String world, String player) {

        UUID uniqueId = BukkitUtils.getUniqueIdByPlayerName(player);
        return permissionService.getGroups(uniqueId).toArray(new String[0]);
    }

    @Override
    public String getPrimaryGroup(String world, String player) {

        UUID uniqueId = BukkitUtils.getUniqueIdByPlayerName(player);
        List<String> groups = permissionService.getGroups(uniqueId);

        if (groups.size() == 0) {
            return null;
        }

        return groups.get(0);
    }

    @Override
    public String[] getGroups() {

        return permissionService.getGroups().toArray(new String[0]);
    }

    @Override
    public boolean hasGroupSupport() {

        return true;
    }
}
