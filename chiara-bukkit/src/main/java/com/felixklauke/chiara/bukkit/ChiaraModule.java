package com.felixklauke.chiara.bukkit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import com.felixklauke.chiara.bukkit.group.GroupConfig;
import com.felixklauke.chiara.bukkit.user.PermissionUserSessionRegistry;
import com.felixklauke.chiara.bukkit.vault.VaultPermissions;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.inject.Singleton;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;

public final class ChiaraModule extends AbstractModule {
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
    bind(ServicesManager.class).toInstance(plugin.getServer().getServicesManager());
    bind(Permission.class).to(VaultPermissions.class);
  }

  private static final String GROUP_CONFIG = "groups.yml";

  @Provides
  @Singleton
  @GroupConfig
  Path provideGroupPath() {
    return Paths.get(plugin.getDataFolder().getPath(), GROUP_CONFIG);
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
}
