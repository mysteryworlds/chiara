package de.felixklauke.chiara.bukkit.vault;

import de.felixklauke.chiara.bukkit.service.PermissionsService;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
    public boolean playerHas(String world, String player, String permission) {


    }

    private UUID getUniqueId(String player) {
        return null;
    }

    @Override
    public boolean playerAdd(String world, String player, String permission) {
        return false;
    }

    @Override
    public boolean playerRemove(String world, String player, String permission) {
        return false;
    }

    @Override
    public boolean groupHas(String world, String group, String permission) {
        return false;
    }

    @Override
    public boolean groupAdd(String world, String group, String permission) {
        return false;
    }

    @Override
    public boolean groupRemove(String world, String group, String permission) {
        return false;
    }

    @Override
    public boolean playerInGroup(String world, String player, String group) {
        return false;
    }

    @Override
    public boolean playerAddGroup(String world, String player, String group) {
        return false;
    }

    @Override
    public boolean playerRemoveGroup(String world, String player, String group) {
        return false;
    }

    @Override
    public String[] getPlayerGroups(String world, String player) {
        return new String[0];
    }

    @Override
    public String getPrimaryGroup(String world, String player) {

        String[] playerGroups = getPlayerGroups(world, player);
        if (playerGroups.length == 0) {
            return "";
        }

        return playerGroups[0];
    }

    @Override
    public String[] getGroups() {

        return permissionsService.getGroups();
    }

    @Override
    public boolean hasGroupSupport() {

        return true;
    }

    @Override
    public boolean has(CommandSender sender, String permission) {
        return super.has(sender, permission);
    }

    @Override
    public boolean has(Player player, String permission) {
        return super.has(player, permission);
    }

    @Override
    public boolean playerHas(String world, OfflinePlayer player, String permission) {
        return super.playerHas(world, player, permission);
    }

    @Override
    public boolean playerHas(Player player, String permission) {
        return super.playerHas(player, permission);
    }

    @Override
    public boolean playerAdd(String world, OfflinePlayer player, String permission) {
        return super.playerAdd(world, player, permission);
    }

    @Override
    public boolean playerAdd(Player player, String permission) {
        return super.playerAdd(player, permission);
    }

    @Override
    public boolean playerAddTransient(OfflinePlayer player, String permission) throws UnsupportedOperationException {
        return super.playerAddTransient(player, permission);
    }

    @Override
    public boolean playerAddTransient(Player player, String permission) {
        return super.playerAddTransient(player, permission);
    }

    @Override
    public boolean playerAddTransient(String worldName, OfflinePlayer player, String permission) {
        return super.playerAddTransient(worldName, player, permission);
    }

    @Override
    public boolean playerAddTransient(String worldName, Player player, String permission) {
        return super.playerAddTransient(worldName, player, permission);
    }

    @Override
    public boolean playerRemoveTransient(String worldName, OfflinePlayer player, String permission) {
        return super.playerRemoveTransient(worldName, player, permission);
    }

    @Override
    public boolean playerRemoveTransient(String worldName, Player player, String permission) {
        return super.playerRemoveTransient(worldName, player, permission);
    }

    @Override
    public boolean playerRemove(String world, OfflinePlayer player, String permission) {
        return super.playerRemove(world, player, permission);
    }

    @Override
    public boolean playerRemove(Player player, String permission) {
        return super.playerRemove(player, permission);
    }

    @Override
    public boolean playerRemoveTransient(OfflinePlayer player, String permission) {
        return super.playerRemoveTransient(player, permission);
    }

    @Override
    public boolean playerRemoveTransient(Player player, String permission) {
        return super.playerRemoveTransient(player, permission);
    }

    @Override
    public boolean groupHas(World world, String group, String permission) {
        return super.groupHas(world, group, permission);
    }

    @Override
    public boolean groupAdd(World world, String group, String permission) {
        return super.groupAdd(world, group, permission);
    }

    @Override
    public boolean groupRemove(World world, String group, String permission) {
        return super.groupRemove(world, group, permission);
    }

    @Override
    public boolean playerInGroup(String world, OfflinePlayer player, String group) {
        return super.playerInGroup(world, player, group);
    }

    @Override
    public boolean playerInGroup(Player player, String group) {
        return super.playerInGroup(player, group);
    }

    @Override
    public boolean playerAddGroup(String world, OfflinePlayer player, String group) {
        return super.playerAddGroup(world, player, group);
    }

    @Override
    public boolean playerAddGroup(Player player, String group) {
        return super.playerAddGroup(player, group);
    }

    @Override
    public boolean playerRemoveGroup(String world, OfflinePlayer player, String group) {
        return super.playerRemoveGroup(world, player, group);
    }

    @Override
    public boolean playerRemoveGroup(Player player, String group) {
        return super.playerRemoveGroup(player, group);
    }

    @Override
    public String[] getPlayerGroups(String world, OfflinePlayer player) {
        return super.getPlayerGroups(world, player);
    }

    @Override
    public String[] getPlayerGroups(Player player) {
        return super.getPlayerGroups(player);
    }

    @Override
    public String getPrimaryGroup(String world, OfflinePlayer player) {
        return super.getPrimaryGroup(world, player);
    }

    @Override
    public String getPrimaryGroup(Player player) {
        return super.getPrimaryGroup(player);
    }
}
