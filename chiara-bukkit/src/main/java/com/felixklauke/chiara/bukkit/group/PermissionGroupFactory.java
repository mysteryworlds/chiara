package com.felixklauke.chiara.bukkit.group;

import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.felixklauke.chiara.bukkit.permission.WorldPermissionTable;
import com.google.common.base.Preconditions;
import javax.inject.Inject;
import org.bukkit.plugin.PluginManager;

public final class PermissionGroupFactory {
  private final PluginManager pluginManager;

  @Inject
  PermissionGroupFactory(PluginManager pluginManager) {
    this.pluginManager = pluginManager;
  }

  public PermissionGroup createGroup(
    String name,
    PermissionTable permissions,
    GroupTable groups,
    WorldPermissionTable worldPermissions
  ) {
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(permissions);
    Preconditions.checkNotNull(groups);
    Preconditions.checkNotNull(worldPermissions);
    return new PermissionGroup(
      name,
      permissions,
      groups,
      worldPermissions,
      pluginManager
    );
  }
}
