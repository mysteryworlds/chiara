package com.felixklauke.chiara.bukkit.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.felixklauke.chiara.bukkit.permission.WorldPermissionTable;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class PermissionGroupRepository {
  private final Map<String, PermissionGroup> groups = new ConcurrentHashMap<>();

  private final PermissionGroupFactory groupFactory;
  private final Path configPath;
  private final ObjectMapper objectMapper;

  @Inject
  PermissionGroupRepository(
    PermissionGroupFactory groupFactory,
    @GroupConfig Path configPath,
    ObjectMapper objectMapper
  ) {
    this.groupFactory = groupFactory;
    this.configPath = configPath;
    this.objectMapper = objectMapper;
  }

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
    try {
      var content = Files.readString(configPath);
      var groupConfig = objectMapper.readValue(content, PermissionGroupConfig.class);
      readGroups(groupConfig);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readGroups(PermissionGroupConfig groupConfig) {
    var groupEntries = groupConfig.getGroups();
    for (var groupName : groupEntries.keySet()) {
      var permissionGroup = readGroup(groupName, groupEntries);
      groups.put(groupName, permissionGroup);
    }
  }

  private PermissionGroup readGroup(
    String group,
    Map<String, PermissionGroupConfigEntry> groups
  ) {
    var configEntry = groups.get(group);
    var permissions = PermissionTable
      .withBoolPermissions(configEntry.permissions());
    var mappedGroups = configEntry.inheritance()
      .stream()
      .map(groupName -> readGroup(groupName, groups))
      .collect(Collectors.toList());
    var mappedWorldPermissions = configEntry.worlds().entrySet().stream()
      .collect(Collectors.toMap(
        Entry::getKey,
        entry -> PermissionTable.withBoolPermissions(entry.getValue())
      ));
    return groupFactory.createGroup(
      group,
      permissions,
      GroupTable.withGroups(mappedGroups),
      WorldPermissionTable.withWorldPermissions(mappedWorldPermissions)
    );
  }

  public static final class PermissionGroupConfig {
    private Map<String, PermissionGroupConfigEntry> groups = new HashMap<>();

    public PermissionGroupConfig() {
    }

    public PermissionGroupConfig(Map<String, PermissionGroupConfigEntry> groups) {
      this.groups = groups;
    }

    public Map<String, PermissionGroupConfigEntry> getGroups() {
      return groups;
    }

    public void setGroups(
      Map<String, PermissionGroupConfigEntry> groups) {
      this.groups = groups;
    }
  }

  public static final class PermissionGroupConfigEntry {
    private Map<String, Boolean> permissions = new HashMap<>();
    private Map<String, Map<String, Boolean>> worlds = new HashMap<>();
    private List<String> inheritance = new ArrayList<>();

    public PermissionGroupConfigEntry() {
    }

    public PermissionGroupConfigEntry(
      Map<String, Boolean> permissions,
      Map<String, Map<String, Boolean>> worlds,
      List<String> inheritance
    ) {
      this.permissions = permissions;
      this.worlds = worlds;
      this.inheritance = inheritance;
    }

    public void setInheritance(List<String> inheritance) {
      this.inheritance = inheritance;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
      this.permissions = permissions;
    }

    public void setWorlds(
      Map<String, Map<String, Boolean>> worlds) {
      this.worlds = worlds;
    }

    public Map<String, Boolean> permissions() {
      return permissions;
    }

    public Map<String, Map<String, Boolean>> worlds() {
      return worlds;
    }

    public List<String> inheritance() {
      return inheritance;
    }
  }
}
