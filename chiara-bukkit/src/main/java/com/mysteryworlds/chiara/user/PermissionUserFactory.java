package com.mysteryworlds.chiara.user;

import com.mysteryworlds.chiara.group.GroupTable;
import com.mysteryworlds.chiara.group.PermissionGroupRepository;
import com.mysteryworlds.chiara.permission.PermissionEntity.Metadata;
import com.mysteryworlds.chiara.permission.PermissionTable;
import com.mysteryworlds.chiara.permission.WorldPermissionTable;
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
    WorldPermissionTable worldPermissions,
    Metadata metadata
  ) {
    Preconditions.checkNotNull(userId);
    Preconditions.checkNotNull(permissions);
    Preconditions.checkNotNull(groups);
    Preconditions.checkNotNull(worldPermissions);
    Preconditions.checkNotNull(metadata);
    return new PermissionUser(
      userId,
      permissions,
      groups,
      worldPermissions,
      metadata,
      pluginManager
    );
  }
}
