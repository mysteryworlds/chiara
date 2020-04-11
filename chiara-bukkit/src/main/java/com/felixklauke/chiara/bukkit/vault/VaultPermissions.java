package com.felixklauke.chiara.bukkit.vault;

import com.felixklauke.chiara.bukkit.group.PermissionGroup;
import com.felixklauke.chiara.bukkit.group.PermissionGroupRepository;
import com.felixklauke.chiara.bukkit.permission.PermissionStatus;
import com.felixklauke.chiara.bukkit.user.PermissionUser;
import com.felixklauke.chiara.bukkit.user.PermissionUserRepository;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@Singleton
public final class VaultPermissions extends Permission {
  private static final boolean SUPER_PERMS_SUPPORT = true;
  private static final boolean GROUP_SUPPORT = true;
  private final Plugin plugin;
  private final PermissionGroupRepository groupRepository;
  private final PermissionUserRepository userRepository;

  @Inject
  private VaultPermissions(
    Plugin plugin,
    PermissionGroupRepository groupRepository,
    PermissionUserRepository userRepository
  ) {
    this.plugin = plugin;
    this.groupRepository = groupRepository;
    this.userRepository = userRepository;
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
    return SUPER_PERMS_SUPPORT;
  }

  @Override
  public boolean playerHas(String world, String player, String permission) {
    var userId = findPlayerUniqueId(player);
    if (world == null) {
      return playerHas(userId, permission);
    }
    return playerHasWorld(userId, permission, world);
  }

  private boolean playerHas(UUID userId, String permission) {
    var userOptional = userRepository.findUser(userId);
    return userOptional
      .map(permissionUser -> permissionUser.hasPermission(permission))
      .orElse(false);
  }

  private boolean playerHasWorld(UUID userId, String permission, String world) {
    var userOptional = userRepository.findUser(userId);
    return userOptional
      .map(permissionUser -> permissionUser.hasPermission(permission, world))
      .orElse(false);
  }

  @Override
  public boolean playerAdd(String world, String player, String permission) {
    if (world == null) {
      return setPlayerPermission(player, permission, PermissionStatus.ALLOWED);
    }
    return setPlayerWorldPermission(player, permission, world,
      PermissionStatus.ALLOWED);
  }

  @Override
  public boolean playerRemove(String world, String player, String permission) {
    if (world == null) {
      return setPlayerPermission(player, permission, PermissionStatus.NOT_SET);
    }
    return setPlayerWorldPermission(player, permission, world,
      PermissionStatus.NOT_SET);
  }

  private boolean setPlayerPermission(
    String player,
    String permission,
    PermissionStatus status
  ) {
    var userId = findPlayerUniqueId(player);
    return userRepository.findUser(userId)
      .map(permissionUser -> permissionUser.setPermissionStatus(
        com.felixklauke.chiara.bukkit.permission.Permission.of(permission),
        status))
      .orElse(false);
  }

  private boolean setPlayerWorldPermission(
    String player,
    String permission,
    String world,
    PermissionStatus status
  ) {
    var userId = findPlayerUniqueId(player);
    return userRepository.findUser(userId)
      .map(permissionUser -> permissionUser.setWorldPermissionStatus(
        com.felixklauke.chiara.bukkit.permission.Permission.of(permission),
        status, world))
      .orElse(false);
  }

  @Override
  public boolean groupHas(String world, String group, String permission) {
    if (world == null) {
      return groupHas(group, permission);
    }
    return groupHasWorld(group, permission, world);
  }

  private boolean groupHas(String group, String permission) {
    var groupOptional = groupRepository.findGroup(group);
    return groupOptional
      .map(permissionGroup -> permissionGroup.hasPermission(permission))
      .orElse(false);
  }

  private boolean groupHasWorld(String group, String permission, String world) {
    var groupOptional = groupRepository.findGroup(group);
    return groupOptional
      .map(permissionGroup -> permissionGroup.hasPermission(permission, world))
      .orElse(false);
  }

  @Override
  public boolean groupAdd(String world, String group, String permission) {
    return setGroupWorldPermission(group, permission, world, PermissionStatus.ALLOWED);
  }

  @Override
  public boolean groupRemove(String world, String group, String permission) {
    return setGroupWorldPermission(group, permission, world, PermissionStatus.NOT_SET);
  }

  private boolean setGroupPermission(
    String group,
    String permission,
    PermissionStatus status
  ) {
    return groupRepository.findGroup(group)
      .map(permissionGroup -> permissionGroup.setPermissionStatus(
        com.felixklauke.chiara.bukkit.permission.Permission.of(permission),
        status
      ))
      .orElse(false);
  }

  private boolean setGroupWorldPermission(
    String group,
    String permission,
    String world,
    PermissionStatus status
  ) {
    return groupRepository.findGroup(group)
      .map(permissionGroup -> permissionGroup.setWorldPermissionStatus(
        com.felixklauke.chiara.bukkit.permission.Permission.of(permission),
        status, world
      ))
      .orElse(false);
  }

  @Override
  public boolean playerInGroup(String world, String player, String group) {
    String[] playerGroups = getPlayerGroups(world, player);
    return Arrays.asList(playerGroups).contains(group);
  }

  @Override
  public boolean playerAddGroup(String world, String player, String group) {
    var groupOptional = groupRepository.findGroup(group);
    if (groupOptional.isEmpty()) {
      return false;
    }
    var id = findPlayerUniqueId(player);
    var userOptional = userRepository.findUser(id);
    if (userOptional.isEmpty()) {
      return false;
    }
    var permissionUser = userOptional.get();
    var permissionGroup = groupOptional.get();
    return permissionUser.addGroup(permissionGroup);
  }

  @Override
  public boolean playerRemoveGroup(String world, String player, String group) {
    var groupOptional = groupRepository.findGroup(group);
    if (groupOptional.isEmpty()) {
      return false;
    }
    var id = findPlayerUniqueId(player);
    var userOptional = userRepository.findUser(id);
    if (userOptional.isEmpty()) {
      return false;
    }
    var permissionUser = userOptional.get();
    var permissionGroup = groupOptional.get();
    return permissionUser.removeGroup(permissionGroup);
  }

  @Override
  public String[] getPlayerGroups(String world, String player) {
    return userRepository.findUser(findPlayerUniqueId(player))
      .map(PermissionUser::groups)
      .orElse(Sets.newHashSet())
      .stream()
      .map(PermissionGroup::name)
      .toArray(String[]::new);
  }

  @Override
  public String getPrimaryGroup(String world, String player) {
    return getPlayerGroups(world, player)[0];
  }

  @Override
  public String[] getGroups() {
    return groupRepository.findAll().stream()
      .map(PermissionGroup::name)
      .toArray(String[]::new);
  }

  @Override
  public boolean hasGroupSupport() {
    return GROUP_SUPPORT;
  }

  private UUID findPlayerUniqueId(String player) {
    return Bukkit.getOfflinePlayer(player).getUniqueId();
  }
}
