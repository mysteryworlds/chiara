package com.mysteryworlds.chiara.user;

import com.google.common.base.Preconditions;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Singleton
public final class PermissionUserSessionFactory {
  private final Plugin plugin;
  private final Chat chat;

  @Inject
  PermissionUserSessionFactory(Plugin plugin, Chat chat) {
    this.plugin = plugin;
    this.chat = chat;
  }

  public PermissionUserSession createSession(
    Player player,
    PermissionUser user
  ) {
    Preconditions.checkNotNull(player);
    Preconditions.checkNotNull(user);
    var permissionAttachment = player.addAttachment(plugin);
    var session = new PermissionUserSession(
      chat,
      player,
      user,
      permissionAttachment
    );
    session.recalculatePermissions();
    session.refreshNaming();
    return session;
  }
}
