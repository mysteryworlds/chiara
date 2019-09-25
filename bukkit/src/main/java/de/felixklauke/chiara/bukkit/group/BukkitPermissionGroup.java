package de.felixklauke.chiara.bukkit.group;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import de.felixklauke.chiara.bukkit.permission.AbstractPermissionContainer;
import java.util.List;
import java.util.Map;

public class BukkitPermissionGroup extends AbstractPermissionContainer implements PermissionGroup {

  private final String name;
  private final List<PermissionGroup> inheritedGroups;

  public BukkitPermissionGroup(
    String name, List<PermissionGroup> inheritedGroups) {
    this.name = name;
    this.inheritedGroups = inheritedGroups;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Map<String, Boolean> getEffectivePermissions() {

    Map<String, Boolean> effectivePermissions = Maps.newHashMap();

    // Add inherited permissions
    inheritedGroups.forEach(inheritedGroup -> {
      Map<String, Boolean> inheritedGroupEffectivePermissions = inheritedGroup.getEffectivePermissions();
      effectivePermissions.putAll(inheritedGroupEffectivePermissions);
    });

    // Add base permissions
    Map<String, Boolean> basePermissions = getBasePermissions();
    effectivePermissions.putAll(basePermissions);

    return effectivePermissions;
  }

  @Override
  public Map<String, Boolean> getEffectivePermissions(String world) {
    Preconditions.checkNotNull(world, "World should not be null");

    Map<String, Boolean> effectivePermissions = Maps.newHashMap();

    inheritedGroups.forEach(inheritedGroup -> {
      Map<String, Boolean> inheritedGroupEffectivePermissions = inheritedGroup.getEffectivePermissions(world);
      effectivePermissions.putAll(inheritedGroupEffectivePermissions);
    });

    // Add base permissions
    Map<String, Boolean> basePermissions = getBasePermissions();
    effectivePermissions.putAll(basePermissions);

    // Add world permissions
    Map<String, Boolean> worldBasePermissions = getWorldBasePermissions(world);
    effectivePermissions.putAll(worldBasePermissions);

    return effectivePermissions;
  }

  @Override
  public boolean hasWorldPermission(String world, String permission) {

    Map<String, Boolean> effectivePermissions = getEffectivePermissions(world);
    return effectivePermissions.getOrDefault(permission, false);
  }

  @Override
  public boolean hasPermission(String permission) {

    Map<String, Boolean> effectivePermissions = getEffectivePermissions();
    return effectivePermissions.getOrDefault(permission, false);
  }
}
