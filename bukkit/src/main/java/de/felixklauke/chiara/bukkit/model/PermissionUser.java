package de.felixklauke.chiara.bukkit.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PermissionUser {

  private transient UUID uniqueId;
  private Map<String, Boolean> permissions;
  private Map<String, Map<String, Boolean>> worldPermissions;
  private List<String> groups;

  public PermissionUser() {
  }

  public PermissionUser(UUID uniqueId, Map<String, Boolean> permissions,
      Map<String, Map<String, Boolean>> worldPermissions, List<String> groups) {
    this.uniqueId = uniqueId;
    this.permissions = permissions;
    this.worldPermissions = worldPermissions;
    this.groups = groups;
  }

  public List<String> getGroups() {
    return groups == null ? Lists.newLinkedList() : Lists.newLinkedList(groups);
  }

  public void setGroups(List<String> groups) {
    this.groups = groups;
  }

  public Map<String, Boolean> getPermissions() {
    return permissions == null ? Maps.newLinkedHashMap() : Maps.newLinkedHashMap(permissions);
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(UUID uniqueId) {
    this.uniqueId = uniqueId;
  }

  public Map<String, Map<String, Boolean>> getWorldPermissions() {
    return worldPermissions == null ? Maps.newLinkedHashMap()
        : Maps.newLinkedHashMap(worldPermissions);
  }

  public void setWorldPermissions(Map<String, Map<String, Boolean>> worldPermissions) {
    this.worldPermissions = worldPermissions;
  }

  public Map<String, Boolean> getWorldPermissions(String worldName) {

    if (worldPermissions == null) {
      return Maps.newLinkedHashMap();
    }
    Map<String, Boolean> worldPermissions = this.worldPermissions.get(worldName);
    return worldPermissions == null ? Maps.newLinkedHashMap()
        : Maps.newLinkedHashMap(worldPermissions);
  }

  public void setPermission(String permission, boolean value) {

    if (permissions == null) {
      permissions = new HashMap<>();
    }

    permissions.put(permission, value);
  }

  public void unsetPermission(String permission) {

    if (permissions == null) {
      return;
    }

    permissions.remove(permission);
  }

  public void setWorldPermission(String world, String permission, boolean value) {

    Map<String, Boolean> worldPermissions = this.worldPermissions
        .computeIfAbsent(world, k -> Maps.newLinkedHashMap());
    worldPermissions.put(permission, value);
  }

  public void unsetWorldPermission(String world, String permission) {

    if (worldPermissions == null) {
      return;
    }

    Map<String, Boolean> worldPermissions = this.worldPermissions.get(world);
    if (worldPermissions == null) {
      return;
    }

    worldPermissions.remove(permission);
  }

  public void addGroup(String group) {

    if (groups == null) {
      groups = Lists.newLinkedList();
    }

    groups.add(group);
  }

  public void removeGroup(String group) {

    if (groups == null) {
      return;
    }

    groups.remove(group);
  }
}
