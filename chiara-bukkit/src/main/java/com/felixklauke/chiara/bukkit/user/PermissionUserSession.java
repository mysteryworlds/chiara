package com.felixklauke.chiara.bukkit.user;

import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.google.common.base.Preconditions;
import java.io.Closeable;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public final class PermissionUserSession implements Closeable {
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

  public static PermissionUserSession of(
    Player player,
    PermissionUser user,
    PermissionAttachment permissionAttachment
  ) {
    Preconditions.checkNotNull(player);
    Preconditions.checkNotNull(user);
    Preconditions.checkNotNull(permissionAttachment);
    return new PermissionUserSession(player, user, permissionAttachment);
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

  @Override
  public void close() {
    permissionAttachment.remove();
  }

  public PermissionUser user() {
    return permissionUser;
  }
}
