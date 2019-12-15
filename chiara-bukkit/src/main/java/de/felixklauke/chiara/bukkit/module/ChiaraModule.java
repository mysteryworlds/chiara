package de.felixklauke.chiara.bukkit.module;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import de.felixklauke.chiara.bukkit.vault.VaultPermissions;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;

public class ChiaraModule extends AbstractModule {
  private final Plugin plugin;
  private final Server server;
  private final PluginManager pluginManager;
  private final ServicesManager servicesManager;
  private final Logger logger;

  private ChiaraModule(
    Plugin plugin,
    Server server,
    PluginManager pluginManager,
    ServicesManager servicesManager,
    Logger logger
  ) {
    this.plugin = plugin;
    this.server = server;
    this.pluginManager = pluginManager;
    this.servicesManager = servicesManager;
    this.logger = logger;
  }

  public static ChiaraModule withPlugin(Plugin plugin) {
    Preconditions.checkNotNull(plugin, "Plugin should not be null");
    var server = plugin.getServer();
    var pluginManager = server.getPluginManager();
    var servicesManager = server.getServicesManager();
    var logger = plugin.getLogger();
    return new ChiaraModule(plugin, server, pluginManager, servicesManager, logger);
  }

  @Override
  protected void configure() {
    bind(Plugin.class).toInstance(plugin);
    bind(Server.class).toInstance(server);
    bind(PluginManager.class).toInstance(pluginManager);
    bind(ServicesManager.class).toInstance(servicesManager);
    bind(Logger.class).toInstance(logger);
    bind(Permission.class).to(VaultPermissions.class).asEagerSingleton();
  }
}
