package com.mysteryworlds.chiara.user;

import com.mysteryworlds.chiara.permission.PermissionTable;
import java.io.Closeable;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public final class PermissionUserSession implements Closeable {
  private final Chat chat;
  private final Player player;
  private final PermissionUser permissionUser;
  private final PermissionAttachment permissionAttachment;

  PermissionUserSession(
    Chat chat,
    Player player,
    PermissionUser permissionUser,
    PermissionAttachment permissionAttachment
  ) {
    this.chat = chat;
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

  @Override
  public void close() {
    permissionAttachment.remove();
  }

  public PermissionUser user() {
    return permissionUser;
  }

  public void refreshNaming() {
    var playerPrefix = chat.getPlayerPrefix(player);
    var playerSuffix = chat.getPlayerSuffix(player);
    String qualifiedName = String.format(
      "%s%s%s",
      playerPrefix,
      player.getName(),
      playerSuffix
    );
    qualifiedName = ChatColor.translateAlternateColorCodes('&', qualifiedName);
    player.setCustomNameVisible(true);
    player.setCustomName(qualifiedName);
    player.setDisplayName(qualifiedName);
    player.setPlayerListName(qualifiedName);
  }
}
