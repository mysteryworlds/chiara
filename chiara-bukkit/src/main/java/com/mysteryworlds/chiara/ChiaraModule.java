package com.mysteryworlds.chiara;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import com.mysteryworlds.chiara.group.GroupConfig;
import com.mysteryworlds.chiara.user.PermissionUserSessionRegistry;
import com.mysteryworlds.chiara.user.UserConfig;
import com.mysteryworlds.chiara.vault.VaultChat;
import com.mysteryworlds.chiara.vault.VaultPermissions;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.inject.Named;
import javax.inject.Singleton;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;

public final class ChiaraModule extends AbstractModule {
  private static final String PLUGIN_CONFIG = "config.yml";
  private static final String GROUP_CONFIG = "groups.yml";
  private static final String USERS_CONFIG = "users.yml";
  private final Plugin plugin;

  private ChiaraModule(Plugin plugin) {
    this.plugin = plugin;
  }

  public static ChiaraModule withPlugin(Plugin plugin) {
    Preconditions.checkNotNull(plugin);
    return new ChiaraModule(plugin);
  }

  @Override
  protected void configure() {
    bind(Plugin.class).toInstance(plugin);
    bind(PluginManager.class).toInstance(plugin.getServer().getPluginManager());
    bind(ServicesManager.class)
      .toInstance(plugin.getServer().getServicesManager());
    bind(Permission.class).to(VaultPermissions.class);
    bind(Chat.class).to(VaultChat.class);
  }

  @Provides
  @Singleton
  @GroupConfig
  Path provideGroupPath() {
    return Paths.get(plugin.getDataFolder().getPath(), GROUP_CONFIG);
  }

  @Provides
  @Singleton
  @UserConfig
  Path provideUsersPath() {
    return Paths.get(plugin.getDataFolder().getPath(), USERS_CONFIG);
  }

  @Provides
  @Singleton
  PermissionUserSessionRegistry provideSessionRegistry() {
    return PermissionUserSessionRegistry.empty();
  }

  @Provides
  @Singleton
  YAMLFactory provideYamLFactory() {
    return YAMLFactory.builder()
      .disable(Feature.WRITE_DOC_START_MARKER)
      .build();
  }

  @Provides
  @Singleton
  ObjectMapper provideObjectMapper(YAMLFactory yamlFactory) {
    return new ObjectMapper(yamlFactory);
  }

  @Provides
  @Singleton
  @PluginConfig
  Configuration providePluginConfig() {
    var file = new File(plugin.getDataFolder(), PLUGIN_CONFIG);
    return YamlConfiguration.loadConfiguration(file);
  }

  @Provides
  @Singleton
  @Named("defaultGroupName")
  String provideDefaultGroup(
    @PluginConfig Configuration configuration
  ) {
    return configuration.getString("default-group");
  }
}
