package com.mysteryworlds.chiara.group;

import com.google.common.base.Preconditions;
import com.mysteryworlds.chiara.permission.PermissionEntity.Metadata;
import com.mysteryworlds.chiara.permission.PermissionTable;
import com.mysteryworlds.chiara.permission.WorldPermissionTable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.yaml.snakeyaml.Yaml;

@Singleton
public final class PermissionGroupRepository {
  private final Map<String, PermissionGroup> groups = new ConcurrentHashMap<>();

  private final PermissionGroupFactory groupFactory;
  private final Path configPath;
  private final Yaml yaml;

  @Inject
  PermissionGroupRepository(
    PermissionGroupFactory groupFactory,
    @GroupConfig Path configPath,
    Yaml yaml
  ) {
    this.groupFactory = groupFactory;
    this.configPath = configPath;
    this.yaml = yaml;
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
    var groupConfig = writeGroupConfig();
    try {
      var content = yaml.dump(groupConfig);
      Files.writeString(configPath, content);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private PermissionGroupConfig writeGroupConfig() {
    var configEntries = groups.values().stream().collect(Collectors.toMap(
      PermissionGroup::name,
      PermissionGroupConfigEntry::fromGroup
    ));
    return PermissionGroupConfig.withEntries(configEntries);
  }

  public void load() {
    try {
      var content = Files.readString(configPath);
      var groupConfig = yaml.loadAs(
        content,
        PermissionGroupConfig.class
      );
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
    var permissions = PermissionTable.withBoolPermissions(
      configEntry.getPermissions()
    );
    var mappedGroups = readInheritance(configEntry, groups);
    return groupFactory.createGroup(
      group,
      permissions,
      GroupTable.withGroups(mappedGroups),
      WorldPermissionTable.withMapWorldBoolPermissions(configEntry.getWorlds()),
      Metadata.withContent(configEntry.getMetadata())
    );
  }

  private List<PermissionGroup> readInheritance(
    PermissionGroupConfigEntry configEntry,
    Map<String, PermissionGroupConfigEntry> groups
  ) {
    return configEntry.getInheritance()
      .stream()
      .map(groupName -> readGroup(groupName, groups))
      .collect(Collectors.toList());
  }

  public static final class PermissionGroupConfig {
    private Map<String, PermissionGroupConfigEntry> groups = new HashMap<>();

    public PermissionGroupConfig() {
    }

    public PermissionGroupConfig(
      Map<String, PermissionGroupConfigEntry> groups) {
      this.groups = groups;
    }

    public static PermissionGroupConfig withEntries(
      Map<String, PermissionGroupConfigEntry> entries
    ) {
      return new PermissionGroupConfig(entries);
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
    private Map<String, Object> metadata = new HashMap<>();

    public PermissionGroupConfigEntry() {
    }

    public PermissionGroupConfigEntry(Map<String, Boolean> permissions,
      Map<String, Map<String, Boolean>> worlds, List<String> inheritance,
      Map<String, Object> metadata) {
      this.permissions = permissions;
      this.worlds = worlds;
      this.inheritance = inheritance;
      this.metadata = metadata;
    }

    public static PermissionGroupConfigEntry fromGroup(PermissionGroup group) {
      return new PermissionGroupConfigEntry(
        group.basePermissions().asMap(),
        group.worldPermissions().asMap(),
        group.groups().stream().map(PermissionGroup::name)
          .collect(Collectors.toList()),
        group.metadata()
      );
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

    public Map<String, Boolean> getPermissions() {
      return permissions;
    }

    public Map<String, Map<String, Boolean>> getWorlds() {
      return worlds;
    }

    public List<String> getInheritance() {
      return inheritance;
    }

    public void setMetadata(Map<String, Object> metadata) {
      this.metadata = metadata;
    }

    public Map<String, Object> getMetadata() {
      return metadata;
    }
  }
}
