package com.felixklauke.chiara.bukkit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixklauke.chiara.bukkit.group.GroupTable;
import com.felixklauke.chiara.bukkit.group.PermissionGroup;
import com.felixklauke.chiara.bukkit.group.PermissionGroupRepository;
import com.felixklauke.chiara.bukkit.group.PermissionGroupRepository.PermissionGroupConfig;
import com.felixklauke.chiara.bukkit.group.PermissionGroupRepository.PermissionGroupConfigEntry;
import com.felixklauke.chiara.bukkit.permission.PermissionTable;
import com.felixklauke.chiara.bukkit.permission.WorldPermissionTable;
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
import javax.inject.Singleton;

@Singleton
public final class PermissionUserRepository {
  private final Map<UUID, PermissionUser> users = new ConcurrentHashMap<>();
  private final PermissionGroupRepository groupRepository;
  private final PermissionUserFactory userFactory;
  private final ObjectMapper objectMapper;
  private final Path usersPath;

  @Inject
  private PermissionUserRepository(
    PermissionGroupRepository groupRepository,
    PermissionUserFactory userFactory,
    ObjectMapper objectMapper,
    @UserConfig Path usersPath
  ) {
    this.groupRepository = groupRepository;
    this.userFactory = userFactory;
    this.objectMapper = objectMapper;
    this.usersPath = usersPath;
  }

  public Optional<PermissionUser> findUser(UUID playerUniqueId) {
    return Optional.ofNullable(users.get(playerUniqueId));
  }

  public PermissionUser findOrCreateUser(UUID uniqueId) {
    return users.computeIfAbsent(uniqueId, this::createUser);
  }

  private PermissionUser createUser(UUID uniqueId) {
    return userFactory.createUser(
      uniqueId,
      PermissionTable.empty(),
      GroupTable.empty(),
      WorldPermissionTable.empty()
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
    var groups = readGroups(configEntry.groups());
    return userFactory.createUser(
      id,
      PermissionTable.withBoolPermissions(configEntry.permissions()),
      GroupTable.withGroups(groups),
      WorldPermissionTable.withMapWorldBoolPermissions(configEntry.worlds())
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
    private Map<UUID, PermissionUserConfigEntry> users;

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
  }

  public static final class PermissionUserConfigEntry {
    private Map<String, Boolean> permissions = new HashMap<>();
    private Map<String, Map<String, Boolean>> worlds = new HashMap<>();
    private List<String> groups = new ArrayList<>();

    public PermissionUserConfigEntry() {
    }

    public PermissionUserConfigEntry(
      Map<String, Boolean> permissions,
      Map<String, Map<String, Boolean>> worlds,
      List<String> groups
    ) {
      this.permissions = permissions;
      this.worlds = worlds;
      this.groups = groups;
    }

    public static PermissionUserConfigEntry fromUser(PermissionUser user) {
      return new PermissionUserConfigEntry(
        user.basePermissions().asMap(),
        user.worldPermissions().asMap(),
        user.groups().stream().map(PermissionGroup::name)
          .collect(Collectors.toList())
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

    public Map<String, Boolean> permissions() {
      return permissions;
    }

    public Map<String, Map<String, Boolean>> worlds() {
      return worlds;
    }

    public List<String> groups() {
      return groups;
    }
  }
}
