package de.felixklauke.chiara.bukkit.user;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import de.felixklauke.chiara.bukkit.group.PermissionGroup;
import de.felixklauke.chiara.bukkit.permission.AbstractPermissionContainer;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

public class BukkitPermissionUser extends AbstractPermissionContainer implements PermissionUser {

  private AtomicReference<PermissionAttachment> attachmentReference = new AtomicReference<>();
  private final Set<PermissionGroup> permissionGroups;
  private final UUID uniqueId;

  private BukkitPermissionUser(
    Set<PermissionGroup> permissionGroups,
    UUID uniqueId) {
    this.permissionGroups = permissionGroups;
    this.uniqueId = uniqueId;
  }

  @Override
  public UUID getUniqueId() {

    return uniqueId;
  }

  @Override
  public Set<PermissionGroup> getPermissionsGroups() {

    return ImmutableSet.copyOf(permissionGroups);
  }

  @Override
  public void addGroup(PermissionGroup group) {
    Preconditions.checkNotNull(group, "Group should not be null");

    permissionGroups.add(group);
  }

  @Override
  public void calculatePermissionAttachment(Plugin plugin) {
    Preconditions.checkNotNull(plugin, "Plugin should not be null");

    Player player = Bukkit.getPlayer(uniqueId);
    if (player == null) {
      throw new IllegalStateException("Can't calculate a permission attachment for an offline player.");
    }

    PermissionAttachment permissionAttachment = attachmentReference.get();

    if (permissionAttachment == null) {
      permissionAttachment = player.addAttachment(plugin);
      attachmentReference.set(permissionAttachment);
    }

    String worldName = player.getWorld().getName();
    Map<String, Boolean> effectivePermissions = getEffectivePermissions(worldName);

    effectivePermissions.forEach(permissionAttachment::setPermission);
  }

  @Override
  public void removePermissionAttachment(Plugin plugin) {
    Preconditions.checkNotNull(plugin, "Plugin should not be null");

    Player player = Bukkit.getPlayer(uniqueId);
    if (player == null) {
      throw new IllegalStateException("Can't calculate a permission attachment for an offline player.");
    }

    PermissionAttachment permissionAttachment = attachmentReference.get();
    if (permissionAttachment == null) {
      return;
    }

    player.removeAttachment(permissionAttachment);
  }

  @Override
  public boolean isInGroup(PermissionGroup permissionGroup) {
    Preconditions.checkNotNull(permissionGroup, "Group should not be null");

    return permissionGroups.contains(permissionGroup);
  }

  @Override
  public void removeGroup(PermissionGroup permissionGroup) {
    Preconditions.checkNotNull(permissionGroup, "Group should not be null");

    permissionGroups.remove(permissionGroup);
  }

  @Override
  public Map<String, Boolean> getEffectivePermissions() {

    Map<String, Boolean> effectivePermissions = Maps.newConcurrentMap();

    // Calculate group permissions
    permissionGroups.forEach(permissionGroup -> {
      Map<String, Boolean> effectiveGroupPermissions = permissionGroup.getEffectivePermissions();
      effectivePermissions.putAll(effectiveGroupPermissions);
    });

    // Add base permissions
    Map<String, Boolean> basePermissions = getBasePermissions();
    effectivePermissions.putAll(basePermissions);

    return effectivePermissions;
  }

  @Override
  public Map<String, Boolean> getEffectivePermissions(String world) {
    Preconditions.checkNotNull(world, "World should not be null");

    Map<String, Boolean> effectivePermissions = Maps.newConcurrentMap();

    // Calculate group permissions
    permissionGroups.forEach(permissionGroup -> {
      Map<String, Boolean> effectiveGroupPermissions = permissionGroup.getEffectivePermissions(world);
      effectivePermissions.putAll(effectiveGroupPermissions);
    });

    // Add base permissions
    Map<String, Boolean> basePermissions = getBasePermissions();
    effectivePermissions.putAll(basePermissions);

    // Add world permissions
    Map<String, Boolean> worldPermissions = getWorldBasePermissions(world);
    effectivePermissions.putAll(worldPermissions);

    return effectivePermissions;
  }

  @Override
  public boolean hasPermission(String permission) {

    Player player = Bukkit.getPlayer(uniqueId);
    if (player == null) {
      return getEffectivePermissions().get(permission);
    }

    return player.hasPermission(permission);
  }
}
