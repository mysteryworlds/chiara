package com.mysteryworlds.chiara.vault;

import com.mysteryworlds.chiara.group.PermissionGroupRepository;
import com.mysteryworlds.chiara.user.PermissionUserRepository;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

@Singleton
public class VaultChat extends Chat {
  private static final String DEFAULT_PREFIX_SUFFIX = ChatColor.WHITE + "";
  private static final String PREFIX_KEY = "prefix";
  private static final String SUFFIX_KEY = "suffix";
  private final Plugin plugin;
  private final PermissionUserRepository userRepository;
  private final PermissionGroupRepository groupRepository;

  @Inject
  private VaultChat(
    Plugin plugin,
    Permission permission,
    PermissionUserRepository userRepository,
    PermissionGroupRepository groupRepository
  ) {
    super(permission);
    this.plugin = plugin;
    this.userRepository = userRepository;
    this.groupRepository = groupRepository;
  }

  @Override
  public String getName() {
    return plugin.getName();
  }

  @Override
  public boolean isEnabled() {
    return plugin.isEnabled();
  }

  @Override
  public String getPlayerPrefix(String world, String player) {
    System.out.println(userRepository.findOrCreateUser(player).metadata());
    return userRepository.findOrCreateUser(player)
      .metadata(PREFIX_KEY)
      .orElse(DEFAULT_PREFIX_SUFFIX)
      .toString();
  }

  @Override
  public void setPlayerPrefix(String world, String player, String prefix) {
    userRepository.findOrCreateUser(player)
      .metadata(PREFIX_KEY, prefix);
  }

  @Override
  public String getPlayerSuffix(String world, String player) {
    return userRepository.findOrCreateUser(player)
      .metadata(SUFFIX_KEY)
      .orElse(DEFAULT_PREFIX_SUFFIX)
      .toString();
  }

  @Override
  public void setPlayerSuffix(String world, String player, String suffix) {
    userRepository.findOrCreateUser(player)
      .metadata(SUFFIX_KEY, suffix);
  }

  @Override
  public String getGroupPrefix(String world, String group) {
    return groupRepository.findGroup(group)
      .orElseThrow()
      .metadata(PREFIX_KEY)
      .orElse(DEFAULT_PREFIX_SUFFIX)
      .toString();
  }

  @Override
  public void setGroupPrefix(String world, String group, String prefix) {
    groupRepository.findGroup(group)
      .orElseThrow()
      .metadata(PREFIX_KEY, prefix);
  }

  @Override
  public String getGroupSuffix(String world, String group) {
    return groupRepository.findGroup(group)
      .orElseThrow()
      .metadata(SUFFIX_KEY)
      .orElse(DEFAULT_PREFIX_SUFFIX)
      .toString();
  }

  @Override
  public void setGroupSuffix(String world, String group, String suffix) {
    groupRepository.findGroup(group)
      .orElseThrow()
      .metadata(SUFFIX_KEY, suffix);
  }

  @Override
  public int getPlayerInfoInteger(
    String world,
    String player,
    String node,
    int defaultValue
  ) {
    return (int) userRepository.findOrCreateUser(player)
      .metadata(node)
      .orElse(defaultValue);
  }

  @Override
  public void setPlayerInfoInteger(
    String world,
    String player,
    String node,
    int value
  ) {
    userRepository.findOrCreateUser(player)
      .metadata(node, value);
  }

  @Override
  public int getGroupInfoInteger(
    String world,
    String group,
    String node,
    int defaultValue
  ) {
    return (int) groupRepository.findGroup(group)
      .orElseThrow()
      .metadata(node)
      .orElse(defaultValue);
  }

  @Override
  public void setGroupInfoInteger(
    String world,
    String group,
    String node,
    int value
  ) {
    groupRepository.findGroup(group)
      .orElseThrow()
      .metadata(node, value);
  }

  @Override
  public double getPlayerInfoDouble(
    String world,
    String player,
    String node,
    double defaultValue
  ) {
    return (double) userRepository.findOrCreateUser(player)
      .metadata(node)
      .orElse(defaultValue);
  }

  @Override
  public void setPlayerInfoDouble(
    String world,
    String player,
    String node,
    double value
  ) {
    userRepository.findOrCreateUser(player)
      .metadata(node, value);
  }

  @Override
  public double getGroupInfoDouble(
    String world,
    String group,
    String node,
    double defaultValue
  ) {
    return (double) groupRepository.findGroup(group)
      .orElseThrow()
      .metadata(node)
      .orElse(defaultValue);
  }

  @Override
  public void setGroupInfoDouble(
    String world,
    String group,
    String node,
    double value
  ) {
    groupRepository.findGroup(group)
      .orElseThrow()
      .metadata(node, value);
  }

  @Override
  public boolean getPlayerInfoBoolean(String world, String player, String node,
    boolean defaultValue) {
    return (boolean) userRepository.findOrCreateUser(player)
      .metadata(node)
      .orElse(defaultValue);
  }

  @Override
  public void setPlayerInfoBoolean(
    String world,
    String player,
    String node,
    boolean value
  ) {
    userRepository.findOrCreateUser(player)
      .metadata(node, value);
  }

  @Override
  public boolean getGroupInfoBoolean(
    String world,
    String group,
    String node,
    boolean defaultValue
  ) {
    return (boolean) groupRepository.findGroup(group)
      .orElseThrow()
      .metadata(node)
      .orElse(defaultValue);
  }

  @Override
  public void setGroupInfoBoolean(
    String world,
    String group,
    String node,
    boolean value
  ) {
    groupRepository.findGroup(group)
      .orElseThrow()
      .metadata(node, value);
  }

  @Override
  public String getPlayerInfoString(
    String world,
    String player,
    String node,
    String defaultValue
  ) {
    return (String) userRepository.findOrCreateUser(player)
      .metadata(node)
      .orElse(defaultValue);
  }

  @Override
  public void setPlayerInfoString(
    String world,
    String player,
    String node,
    String value
  ) {
    userRepository.findOrCreateUser(player)
      .metadata(node, value);
  }

  @Override
  public String getGroupInfoString(
    String world,
    String group,
    String node,
    String defaultValue
  ) {
    return (String) groupRepository.findGroup(group)
      .orElseThrow()
      .metadata(node)
      .orElse(defaultValue);
  }

  @Override
  public void setGroupInfoString(
    String world,
    String group,
    String node,
    String value
  ) {
    groupRepository.findGroup(group)
      .orElseThrow()
      .metadata(node, value);
  }
}
