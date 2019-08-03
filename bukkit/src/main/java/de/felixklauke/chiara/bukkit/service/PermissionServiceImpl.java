package de.felixklauke.chiara.bukkit.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.felixklauke.chiara.bukkit.model.PermissionGroup;
import de.felixklauke.chiara.bukkit.model.PermissionUser;
import de.felixklauke.chiara.bukkit.repository.PermissionGroupRepository;
import de.felixklauke.chiara.bukkit.repository.PermissionUserRepository;
import de.felixklauke.chiara.bukkit.util.ReflectionUtils;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

public class PermissionServiceImpl implements PermissionService {

  /**
   * All currently managed permissions attachments.
   */
  private final Map<UUID, PermissionAttachment> permissionAttachments = Maps.newConcurrentMap();
  private final Plugin plugin;
  private final PermissionGroupRepository permissionGroupRepository;
  private final PermissionUserRepository permissionUserRepository;

  public PermissionServiceImpl(Plugin plugin, PermissionGroupRepository permissionGroupRepository,
      PermissionUserRepository permissionUserRepository) {
    this.plugin = plugin;
    this.permissionGroupRepository = permissionGroupRepository;
    this.permissionUserRepository = permissionUserRepository;
  }

  @Override
  public void registerPlayer(Player player) {

    Objects.requireNonNull(player, "Player cannot be null.");

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

    Objects.requireNonNull(player, "Player cannot be null.");

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

    Objects.requireNonNull(player, "Player cannot be null.");

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
  public void savePermissions() {

    permissionGroupRepository.writeGroups();
    permissionUserRepository.writeUsers();
  }

  @Override
  public List<String> getGroups() {

    return permissionGroupRepository.findGroups()
        .stream()
        .map(PermissionGroup::getName)
        .collect(Collectors.toList());
  }

  @Override
  public List<String> getGroups(UUID uniqueId) {

    Objects.requireNonNull(uniqueId, "UUID cannot be null.");

    PermissionUser permissionUser = permissionUserRepository.findUser(uniqueId);
    if (permissionUser == null) {
      return Lists.newArrayList();
    }

    return permissionUser.getGroups();
  }

  @Override
  public void setUserPermission(UUID uniqueId, String permission, boolean value) {

    Objects.requireNonNull(uniqueId, "UUID cannot be null.");
    Objects.requireNonNull(permission, "Permission cannot be null.");

    PermissionUser permissionUser = getUserOrCreateUser(uniqueId);
    permissionUser.setPermission(permission, value);
    permissionUserRepository.saveUser(permissionUser);

    refreshPlayer(uniqueId);
  }

  @Override
  public void setUserPermission(UUID uniqueId, String world, String permission, boolean value) {

    Objects.requireNonNull(uniqueId, "UUID cannot be null.");
    Objects.requireNonNull(permission, "Permission cannot be null.");

    PermissionUser permissionUser = getUserOrCreateUser(uniqueId);
    permissionUser.setWorldPermission(world, permission, value);
    permissionUserRepository.saveUser(permissionUser);

    refreshPlayer(uniqueId);
  }

  @Override
  public void unsetUserPermission(UUID uniqueId, String permission) {

    Objects.requireNonNull(uniqueId, "UUID cannot be null.");
    Objects.requireNonNull(permission, "Permission cannot be null.");

    PermissionUser permissionUser = getUser(uniqueId);
    if (permissionUser == null) {
      return;
    }

    permissionUser.unsetPermission(permission);
    permissionUserRepository.saveUser(permissionUser);

    refreshPlayer(uniqueId);
  }

  @Override
  public void unsetUserPermission(UUID uniqueId, String world, String permission) {

    Objects.requireNonNull(uniqueId, "UUID cannot be null.");
    Objects.requireNonNull(world, "World cannot be null.");
    Objects.requireNonNull(permission, "Permission cannot be null.");

    PermissionUser permissionUser = permissionUserRepository.findUser(uniqueId);
    if (permissionUser == null) {
      return;
    }

    permissionUser.unsetWorldPermission(world, permission);
    permissionUserRepository.saveUser(permissionUser);

    refreshPlayer(uniqueId);
  }

  @Override
  public boolean hasGroupPermission(String group, String permission, boolean value) {

    Objects.requireNonNull(permission, "Permission cannot be null.");

    return hasGroupPermission(group, null, permission, value);
  }

  @Override
  public boolean hasGroupPermission(String group, String world, String permission, boolean value) {

    Objects.requireNonNull(group, "Group cannot be null.");
    Objects.requireNonNull(permission, "Permission cannot be null.");

    Map<String, Boolean> groupPermissions = calculateGroupPermissions(group, world);
    if (!groupPermissions.containsKey(permission)) {
      return false;
    }

    return groupPermissions.get(permission);
  }

  @Override
  public void setGroupPermission(String group, String permission, boolean value) {

    Objects.requireNonNull(group, "Group cannot be null.");
    Objects.requireNonNull(permission, "Permission cannot be null.");

    PermissionGroup permissionGroup = getGroupOrCreateGroup(group);
    permissionGroup.setPermission(permission, value);
    permissionGroupRepository.saveGroup(permissionGroup);

    refreshGroup(group);
  }

  @Override
  public void setGroupPermission(String group, String world, String permission, boolean value) {

    Objects.requireNonNull(group, "Group cannot be null.");
    Objects.requireNonNull(permission, "Permission cannot be null.");

    PermissionGroup permissionGroup = getGroup(group);
    permissionGroup.setWorldPermission(world, permission, value);
    permissionGroupRepository.saveGroup(permissionGroup);

    refreshGroup(group);
  }

  @Override
  public void unsetGroupPermission(String group, String permission) {

    Objects.requireNonNull(group, "Group cannot be null.");
    Objects.requireNonNull(permission, "Permission cannot be null.");

    PermissionGroup permissionGroup = permissionGroupRepository.findGroup(group);
    if (permissionGroup == null) {
      return;
    }

    permissionGroup.unsetPermission(permission);
    permissionGroupRepository.saveGroup(permissionGroup);

    refreshGroup(group);
  }

  @Override
  public void unsetGroupPermission(String group, String world, String permission) {

    Objects.requireNonNull(group, "Group cannot be null.");
    Objects.requireNonNull(permission, "Permission cannot be null.");

    PermissionGroup permissionGroup = permissionGroupRepository.findGroup(group);
    if (permissionGroup == null) {
      return;
    }

    permissionGroup.unsetWorldPermission(world, permission);
    permissionGroupRepository.saveGroup(permissionGroup);

    refreshGroup(group);
  }

  @Override
  public void addUserGroup(UUID uniqueId, String group) {

    Objects.requireNonNull(group, "Group cannot be null.");
    Objects.requireNonNull(uniqueId, "UUID cannot be null.");

    PermissionUser permissionUser = getUserOrCreateUser(uniqueId);
    permissionUser.addGroup(group);
    permissionUserRepository.saveUser(permissionUser);

    refreshPlayer(uniqueId);
  }

  @Override
  public void removeUserGroup(UUID uniqueId, String group) {

    Objects.requireNonNull(group, "Group cannot be null.");
    Objects.requireNonNull(uniqueId, "UUID cannot be null.");

    PermissionUser permissionUser = getUser(uniqueId);
    if (permissionUser == null) {
      return;
    }

    permissionUser.removeGroup(group);
    permissionUserRepository.saveUser(permissionUser);

    refreshPlayer(uniqueId);
  }

  private void refreshPlayer(UUID uniqueId) {

    Player player = Bukkit.getPlayer(uniqueId);
    if (player != null) {
      refreshPlayer(player);
    }
  }

  private void refreshGroup(String group) {

    Set<String> childGroups = getChildGroups(group);
    for (String childGroup : childGroups) {

      for (UUID uniqueId : permissionAttachments.keySet()) {
        PermissionUser permissionUser = permissionUserRepository.findUser(uniqueId);
        List<String> permissionUserGroups = permissionUser.getGroups();

        if (permissionUserGroups.contains(childGroup) || permissionUserGroups.contains(group)) {
          refreshPlayer(uniqueId);
        }
      }
    }
  }

  private Set<String> getChildGroups(String group) {

    Set<String> childGroups = Sets.newHashSet();
    List<PermissionGroup> permissionGroups = permissionGroupRepository.findGroups();

    for (PermissionGroup permissionGroup : permissionGroups) {

      List<String> inheritance = permissionGroup.getInheritance();
      if (!inheritance.contains(group)) {
        continue;
      }

      String childName = permissionGroup.getName();
      Set<String> recursiveChildGroups = getChildGroups(childName);
      childGroups.addAll(recursiveChildGroups);
    }

    return childGroups;
  }

  private PermissionGroup getGroupOrCreateGroup(String group) {

    PermissionGroup permissionGroup = getGroup(group);
    if (permissionGroup == null) {
      permissionGroup = permissionGroupRepository.createGroup(group);
    }

    return permissionGroup;
  }

  private PermissionUser getUserOrCreateUser(UUID uniqueId) {

    PermissionUser permissionUser = getUser(uniqueId);
    if (permissionUser == null) {
      permissionUser = permissionUserRepository.createUser(uniqueId);
    }

    return permissionUser;
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
      attachmentPermissions = (Map<String, Boolean>) ReflectionUtils
          .getFieldValue(attachment, "permissions");
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
      return Maps.newLinkedHashMap();
    }

    // The real permissions
    Map<String, Boolean> permissions = Maps.newLinkedHashMap();

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
      return Maps.newLinkedHashMap();
    }

    // The real permissions
    Map<String, Boolean> permissions = Maps.newLinkedHashMap();

    // Calculate inherited permissions
    List<String> inheritance = group.getInheritance();
    for (String parentGroup : inheritance) {
      Map<String, Boolean> parentGroupPermissions = calculateGroupPermissions(parentGroup,
          worldName);
      permissions.putAll(parentGroupPermissions);
    }

    // Group permissions
    Map<String, Boolean> groupPermissions = group.getPermissions();
    permissions.putAll(groupPermissions);

    // Group world permissions
    if (worldName == null) {
      return permissions;
    }

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
