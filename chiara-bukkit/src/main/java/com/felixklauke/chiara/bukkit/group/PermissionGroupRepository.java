package com.felixklauke.chiara.bukkit.group;

import java.util.Optional;
import java.util.Set;
import javax.inject.Singleton;

@Singleton
public final class PermissionGroupRepository {
  public Set<PermissionGroup> findAll() {
    return Set.of();
  }

  public Optional<PermissionGroup> findGroup(String group) {
    return Optional.empty();
  }
}
