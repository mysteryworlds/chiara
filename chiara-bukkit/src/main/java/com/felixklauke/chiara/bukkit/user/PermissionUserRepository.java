package com.felixklauke.chiara.bukkit.user;

import java.util.Optional;
import java.util.UUID;
import javax.inject.Singleton;

@Singleton
public final class PermissionUserRepository {

  public Optional<PermissionUser> findUser(UUID playerUniqueId) {
    return Optional.empty();
  }

  public PermissionUser findOrCreateUser(UUID uniqueId) {
    return null;
  }
}
