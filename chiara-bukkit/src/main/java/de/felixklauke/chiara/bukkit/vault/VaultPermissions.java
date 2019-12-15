package de.felixklauke.chiara.bukkit.vault;

import de.felixklauke.chiara.bukkit.group.PermissionGroup;
import de.felixklauke.chiara.bukkit.group.PermissionGroupRepository;
import de.felixklauke.chiara.bukkit.user.PermissionUser;
import de.felixklauke.chiara.bukkit.user.PermissionUserRepository;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class VaultPermissions extends Permission {

  private static final boolean SUPERPERMS_SUPPORT = true;
  private static final boolean GROUP_SUPPORT = true;

  private final Plugin plugin;
  private final PermissionGroupRepository permissionGroupRepository;
  private final PermissionUserRepository permissionUserRepository;

  @Inject
  private VaultPermissions(Plugin plugin,
    PermissionGroupRepository permissionGroupRepository,
    PermissionUserRepository permissionUserRepository) {
    this.plugin = plugin;
    this.permissionGroupRepository = permissionGroupRepository;
    this.permissionUserRepository = permissionUserRepository;
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

    return SUPERPERMS_SUPPORT;
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

   PermissionUser permissionUser = permissionUserRepository.findByNameOrCreate(player);

    if (world == null) {
      permissionUser.setPermission(permission, true);
      return true;
    }

    permissionUser.setWorldPermission(permission, world, true);
    return true;
  }

  @Override
  public boolean playerRemove(String world, String player, String permission) {

    PermissionUser permissionUser = permissionUserRepository.findByNameOrCreate(player);

    if (world == null) {
      permissionUser.removePermission(permission);
      return true;
    }

    permissionUser.removeWorldPermission(permission, world);
    return true;
  }

  @Override
  public boolean groupHas(String world, String group, String permission) {

    Optional<PermissionGroup> groupOptional = permissionGroupRepository.find(group);
    if (groupOptional.isEmpty()) {
      return false;
    }

    PermissionGroup permissionGroup = groupOptional.get();

    if (world == null) {
      return permissionGroup.hasPermission(permission);
    }

    return permissionGroup.hasWorldPermission(world, permission);
  }

  @Override
  public boolean groupAdd(String world, String group, String permission) {

    Optional<PermissionGroup> groupOptional = permissionGroupRepository.find(group);
    if (groupOptional.isEmpty()) {
      return false;
    }

    PermissionGroup permissionGroup = groupOptional.get();

    if (world == null) {
      permissionGroup.setPermission(permission, true);
      return true;
    }

    permissionGroup.setWorldPermission(permission, world, true);
    return true;
  }

  @Override
  public boolean groupRemove(String world, String group, String permission) {

    Optional<PermissionGroup> groupOptional = permissionGroupRepository.find(group);
    if (groupOptional.isEmpty()) {
      return false;
    }

    PermissionGroup permissionGroup = groupOptional.get();

    if (world == null) {
      permissionGroup.removePermission(permission);
      return true;
    }

    permissionGroup.removeWorldPermission(permission, world);
    return true;
  }

  @Override
  public boolean playerInGroup(String world, String player, String group) {

    Optional<PermissionGroup> groupOptional = permissionGroupRepository.find(group);
    if (groupOptional.isEmpty()) {
      return false;
    }

    PermissionGroup permissionGroup = groupOptional.get();

    Optional<PermissionUser> optionalUser = permissionUserRepository.findByName(player);
    if (optionalUser.isEmpty()) {
      return false;
    }

    PermissionUser permissionUser = optionalUser.get();

    return permissionUser.isInGroup(permissionGroup);
  }

  @Override
  public boolean playerAddGroup(String world, String player, String group) {

    Optional<PermissionGroup> groupOptional = permissionGroupRepository.find(group);
    if (groupOptional.isEmpty()) {
      return false;
    }

    PermissionGroup permissionGroup = groupOptional.get();
    PermissionUser permissionUser = permissionUserRepository.findByNameOrCreate(player);

    permissionUser.addGroup(permissionGroup);
    return true;
  }

  @Override
  public boolean playerRemoveGroup(String world, String player, String group) {

    Optional<PermissionGroup> groupOptional = permissionGroupRepository.find(group);
    if (groupOptional.isEmpty()) {
      return false;
    }

    PermissionGroup permissionGroup = groupOptional.get();
    PermissionUser permissionUser = permissionUserRepository.findByNameOrCreate(player);

    permissionUser.removeGroup(permissionGroup);
    return true;
  }

  @Override
  public String[] getPlayerGroups(String world, String player) {

    PermissionUser permissionUser = permissionUserRepository.findByNameOrCreate(player);
    Set<String> groups = permissionUser.getPermissionsGroups().stream()
      .map(PermissionGroup::getName)
      .collect(Collectors.toSet());
    return groups.toArray(new String[]{});
  }

  @Override
  public String getPrimaryGroup(String world, String player) {

    PermissionUser permissionUser = permissionUserRepository.findByNameOrCreate(player);
    Set<PermissionGroup> permissionsGroups = permissionUser.getPermissionsGroups();
    return permissionsGroups.stream()
      .findFirst()
      .orElseThrow()
      .getName();
  }

  @Override
  public String[] getGroups() {

    Set<String> groups = permissionGroupRepository.findAll().stream()
      .map(PermissionGroup::getName)
      .collect(Collectors.toSet());
    return groups.toArray(new String[]{});
  }

  @Override
  public boolean hasGroupSupport() {

    return GROUP_SUPPORT;
  }
}
