package com.felixklauke.chiara.bukkit;

import com.felixklauke.chiara.bukkit.vault.VaultPermissions;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicesManager;

public final class ChiaraModule extends AbstractModule {
  private final Plugin plugin;

  private ChiaraModule(Plugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    bind(Plugin.class).toInstance(plugin);
    bind(ServicesManager.class).toInstance(plugin.getServer().getServicesManager());
    bind(Permission.class).to(VaultPermissions.class);
  }

  public static ChiaraModule withPlugin(Plugin plugin) {
    Preconditions.checkNotNull(plugin);
    return new ChiaraModule(plugin);
  }
}
