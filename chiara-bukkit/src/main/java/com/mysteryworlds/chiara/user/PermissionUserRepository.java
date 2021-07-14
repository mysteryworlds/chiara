package com.mysteryworlds.chiara.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysteryworlds.chiara.group.GroupTable;
import com.mysteryworlds.chiara.group.PermissionGroup;
import com.mysteryworlds.chiara.group.PermissionGroupRepository;
import com.mysteryworlds.chiara.permission.PermissionEntity.Metadata;
import com.mysteryworlds.chiara.permission.PermissionTable;
import com.mysteryworlds.chiara.permission.WorldPermissionTable;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.bukkit.Bukkit;

@Singleton
public final class PermissionUserRepository {
  private final Map<UUID, PermissionUser> users = new ConcurrentHashMap<>();
  private final String defaultGroupName;
  private final PermissionGroupRepository groupRepository;
  private final PermissionUserFactory userFactory;
  private final ObjectMapper objectMapper;
  private final Path usersPath;

  @Inject
  private PermissionUserRepository(
    @Named("defaultGroupName") String defaultGroupName,
    PermissionGroupRepository groupRepository,
    PermissionUserFactory userFactory,
    ObjectMapper objectMapper,
    @UserConfig Path usersPath
  ) {
    this.defaultGroupName = defaultGroupName;
    this.groupRepository = groupRepository;
    this.userFactory = userFactory;
    this.objectMapper = objectMapper;
    this.usersPath = usersPath;
  }

  public Optional<PermissionUser> findUser(UUID playerUniqueId) {
    Preconditions.checkNotNull(playerUniqueId);
    return Optional.ofNullable(users.get(playerUniqueId));
  }

  public PermissionUser findOrCreateUser(String name) {
    Preconditions.checkNotNull(name);
    var offlinePlayer = Bukkit.getOfflinePlayer(name);
    return findOrCreateUser(offlinePlayer.getUniqueId());
  }

  public PermissionUser findOrCreateUser(UUID uniqueId) {
    Preconditions.checkNotNull(uniqueId);
    return users.computeIfAbsent(uniqueId, this::createUser);
  }

  private PermissionUser createUser(UUID uniqueId) {
    Preconditions.checkNotNull(uniqueId);
    var defaultGroupTable = createDefaultGroupTable();
    return userFactory.createUser(
      uniqueId,
      PermissionTable.empty(),
      defaultGroupTable,
      WorldPermissionTable.empty(),
      Metadata.empty()
    );
  }

  private GroupTable createDefaultGroupTable() {
    var defaultGroup = groupRepository.findGroup(defaultGroupName);
    return GroupTable.withGroups(
      defaultGroup.orElseThrow(
        () -> new IllegalArgumentException("Couldn't find default group: " + defaultGroupName)
      )
    );
  }

  public void load() {
    try {
      var content = Files.readString(usersPath);
      var usersConfig = objectMapper.readValue(
        content,
        PermissionUserConfig.class
      );
      readUsers(usersConfig);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void readUsers(PermissionUserConfig groupConfig) {
    System.out.println(groupConfig);
    var userEntries = groupConfig.getUsers().entrySet();
    for (var userEntry : userEntries) {
      var user = readUser(userEntry.getKey(), userEntry.getValue());
      users.put(userEntry.getKey(), user);
    }
  }

  private PermissionUser readUser(
    UUID id,
    PermissionUserConfigEntry configEntry
  ) {
    var groups = readGroups(configEntry.getGroups());
    return userFactory.createUser(
      id,
      PermissionTable.withBoolPermissions(configEntry.getPermissions()),
      GroupTable.withGroups(groups),
      WorldPermissionTable.withMapWorldBoolPermissions(configEntry.getWorlds()),
      Metadata.withContent(configEntry.getMetadata())
    );
  }

  private List<PermissionGroup> readGroups(List<String> groupNames) {
    return groupNames.stream()
      .map(groupName -> groupRepository.findGroup(groupName).orElseThrow())
      .collect(Collectors.toList());
  }

  public void save() {
    var userConfig = writeUserConfig();
    try {
      var content = objectMapper.writeValueAsString(userConfig);
      Files.writeString(usersPath, content);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private PermissionUserConfig writeUserConfig() {
    var configEntries = users.values().stream().collect(Collectors.toMap(
      PermissionUser::id,
      PermissionUserConfigEntry::fromUser
    ));
    return PermissionUserConfig.withEntries(configEntries);
  }

  public static final class PermissionUserConfig {
    private Map<UUID, PermissionUserConfigEntry> users = new HashMap<>();

    public PermissionUserConfig() {
    }

    public PermissionUserConfig(Map<UUID, PermissionUserConfigEntry> users) {
      this.users = users;
    }

    public static PermissionUserConfig withEntries(
      Map<UUID, PermissionUserConfigEntry> entries
    ) {
      return new PermissionUserConfig(entries);
    }

    public Map<UUID, PermissionUserConfigEntry> getUsers() {
      return users;
    }

    public void setUsers(Map<UUID, PermissionUserConfigEntry> users) {
      this.users = users;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
        .add("users", users)
        .toString();
    }
  }

  public static final class PermissionUserConfigEntry {
    private Map<String, Boolean> permissions = new HashMap<>();
    private Map<String, Map<String, Boolean>> worlds = new HashMap<>();
    private List<String> groups = new ArrayList<>();
    private Map<String, Object> metadata = new HashMap<>();

    public PermissionUserConfigEntry() {
    }

    public PermissionUserConfigEntry(
      Map<String, Boolean> permissions,
      Map<String, Map<String, Boolean>> worlds,
      List<String> groups,
      Map<String, Object> metadata
    ) {
      this.permissions = permissions;
      this.worlds = worlds;
      this.groups = groups;
      this.metadata = metadata;
    }

    public static PermissionUserConfigEntry fromUser(PermissionUser user) {
      return new PermissionUserConfigEntry(
        user.basePermissions().asMap(),
        user.worldPermissions().asMap(),
        user.groups().stream().map(PermissionGroup::name)
          .collect(Collectors.toList()),
        user.metadata()
      );
    }

    public void setGroups(List<String> groups) {
      this.groups = groups;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
      this.permissions = permissions;
    }

    public void setWorlds(
      Map<String, Map<String, Boolean>> worlds
    ) {
      this.worlds = worlds;
    }

    public Map<String, Boolean> getPermissions() {
      return permissions;
    }

    public Map<String, Map<String, Boolean>> getWorlds() {
      return worlds;
    }

    public List<String> getGroups() {
      return groups;
    }

    public Map<String, Object> getMetadata() {
      return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
      this.metadata = metadata;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
        .add("permissions", permissions)
        .add("worlds", worlds)
        .add("groups", groups)
        .add("metadata", metadata)
        .toString();
    }
  }
}
