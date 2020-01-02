package com.felixklauke.chiara.bukkit.group;

import com.google.common.base.Preconditions;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.bukkit.configuration.file.FileConfiguration;

@Singleton
public final class PermissionGroupRepository {
  private final Map<String, PermissionGroup> groups = new ConcurrentHashMap<>();

  public void save(PermissionGroup group) {
    Preconditions.checkNotNull(group);
    groups.put(group.name(), group);
  }

  public Set<PermissionGroup> findAll() {
    return Set.copyOf(groups.values());
  }

  public Optional<PermissionGroup> findGroup(String group) {
    Preconditions.checkNotNull(group);
    return Optional.ofNullable(groups.get(group));
  }

  public void save() {
  }

  public void load() {
  }
}
