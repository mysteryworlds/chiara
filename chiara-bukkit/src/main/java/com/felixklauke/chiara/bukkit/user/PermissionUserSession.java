package com.felixklauke.chiara.bukkit.user;

import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public final class PermissionUserSession {
  private final Player player;
  private final PermissionUser permissionUser;
  private final PermissionAttachment permissionAttachment;

  private PermissionUserSession(
    Player player,
    PermissionUser permissionUser,
    PermissionAttachment permissionAttachment
  ) {
    this.player = player;
    this.permissionUser = permissionUser;
    this.permissionAttachment = permissionAttachment;
  }

  public void recalculatePermissions() {
    var permissions = calculatePermissions();
    permissions.apply(permissionAttachment);
    player.recalculatePermissions();
  }

  private PermissionTable calculatePermissions() {
    var world = player.getWorld().getName();
    return permissionUser.calculateEffectivePermissions(world);
  }
}
