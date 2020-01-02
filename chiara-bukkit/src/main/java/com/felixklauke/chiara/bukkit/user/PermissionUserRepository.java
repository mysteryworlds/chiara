package com.felixklauke.chiara.bukkit.user;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Singleton;

@Singleton
public final class PermissionUserRepository {
  private final Map<UUID, PermissionUser> users = new ConcurrentHashMap<>();

  public Optional<PermissionUser> findUser(UUID playerUniqueId) {
    return Optional.empty();
  }

  public PermissionUser findOrCreateUser(UUID uniqueId) {
    return null;
  }

  public void load() {

  }

  public void save() {

  }
}
