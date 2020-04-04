package com.felixklauke.chiara.bukkit.user;

import com.felixklauke.chiara.bukkit.group.GroupTable;
import com.felixklauke.chiara.bukkit.group.PermissionGroupRepository;
import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.felixklauke.chiara.bukkit.permission.WorldPermissionTable;
import com.google.common.base.Preconditions;
import java.util.UUID;
import javax.inject.Inject;
import org.bukkit.plugin.PluginManager;

public final class PermissionUserFactory {
  private final PluginManager pluginManager;
  private final PermissionGroupRepository groupRepository;

  @Inject
  PermissionUserFactory(PluginManager pluginManager,
    PermissionGroupRepository groupRepository
  ) {
    this.pluginManager = pluginManager;
    this.groupRepository = groupRepository;
  }

  public PermissionUser createUser(
    UUID userId,
    PermissionTable permissions,
    GroupTable groups,
    WorldPermissionTable worldPermissions
  ) {
    Preconditions.checkNotNull(userId);
    Preconditions.checkNotNull(permissions);
    Preconditions.checkNotNull(groups);
    Preconditions.checkNotNull(worldPermissions);
    return new PermissionUser(
      userId,
      permissions,
      groups,
      worldPermissions,
      pluginManager
    );
  }
}
