package de.felixklauke.chiara.bukkit.vault;

import de.felixklauke.chiara.bukkit.service.PermissionsService;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.UUID;

public class VaultPermissions extends Permission {

    private final Plugin plugin;
    private final PermissionsService permissionsService;

    public VaultPermissions(Plugin plugin, PermissionsService permissionsService) {
        this.plugin = plugin;
        this.permissionsService = permissionsService;
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
    public boolean hasGroupSupport() {

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

        UUID uniqueId = getUniqueId(player);

        if (world == null) {
            permissionsService.setUserPermission(uniqueId, permission, true);
            return true;
        }

        permissionsService.setUserPermission(uniqueId, world, permission, true);
        return true;
    }

    @Override
    public boolean playerRemove(String world, String player, String permission) {

        UUID uniqueId = getUniqueId(player);

        if (world == null) {
            permissionsService.unsetUserPermission(uniqueId, permission);
            return true;
        }

        permissionsService.unsetUserPermission(uniqueId, world, permission);
        return true;
    }

    @Override
    public boolean groupHas(String world, String group, String permission) {

        if (world == null) {
            return permissionsService.hasGroupPermission(group, permission, true);
        }

        return permissionsService.hasGroupPermission(group, world, permission, true);
    }

    @Override
    public boolean groupAdd(String world, String group, String permission) {

        if (world == null) {
            permissionsService.setGroupPermission(group, permission, true);
            return true;
        }

        permissionsService.setGroupPermission(group, world, permission, true);
        return true;
    }

    @Override
    public boolean groupRemove(String world, String group, String permission) {

        if (world == null) {
            permissionsService.unsetGroupPermission(group, permission);
            return true;
        }

        permissionsService.unsetGroupPermission(group, world, permission);
        return true;
    }

    @Override
    public boolean playerInGroup(String world, String player, String group) {

        UUID uniqueId = getUniqueId(player);
        return permissionsService.getGroups(uniqueId).contains(group);
    }

    @Override
    public boolean playerAddGroup(String world, String player, String group) {

        UUID uniqueId = getUniqueId(player);
        permissionsService.addUserGroup(uniqueId, group);
        return true;
    }

    @Override
    public boolean playerRemoveGroup(String world, String player, String group) {

        UUID uniqueId = getUniqueId(player);
        permissionsService.removeUserGroup(uniqueId, group);
        return true;
    }

    @Override
    public String[] getPlayerGroups(String world, String player) {

        UUID uniqueId = getUniqueId(player);
        return permissionsService.getGroups(uniqueId).toArray(new String[0]);
    }

    @Override
    public String getPrimaryGroup(String world, String player) {

        UUID uniqueId = getUniqueId(player);
        List<String> groups = permissionsService.getGroups(uniqueId);

        if (groups.size() == 0) {
            return null;
        }

        return groups.get(0);
    }

    @Override
    public String[] getGroups() {

        return permissionsService.getGroups();
    }

    private UUID getUniqueId(String player) {

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
        return offlinePlayer.getUniqueId();
    }
}
