package com.mysteryworlds.chiara.group;

import com.mysteryworlds.chiara.permission.PermissionEntity.Metadata;
import com.mysteryworlds.chiara.permission.PermissionTable;
import com.mysteryworlds.chiara.permission.WorldPermissionTable;
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
    WorldPermissionTable worldPermissions,
    Metadata metadata
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
      metadata,
      pluginManager
    );
  }
}
