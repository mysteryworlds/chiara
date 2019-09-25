package de.felixklauke.chiara.bukkit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.felixklauke.chiara.bukkit.group.PermissionGroupRepository;
import de.felixklauke.chiara.bukkit.listener.PermissionListener;
import de.felixklauke.chiara.bukkit.module.ChiaraModule;
import de.felixklauke.chiara.bukkit.user.PermissionUserRepository;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.OptionalDouble;
import java.util.logging.Logger;
import javax.inject.Inject;
import net.milkbowl.vault.permission.Permission;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChiaraPlugin extends JavaPlugin {

  private static final String GROUPS_CONFIG_FILE_NAME = "groups.yml";
  private static final String USERS_CONFIG_FILE_NAME = "users.yml";

  @Inject
  private Logger logger;
  @Inject
  private ServicesManager servicesManager;
  @Inject
  private PluginManager pluginManager;
  @Inject
  private PermissionGroupRepository permissionGroupRepository;
  @Inject
  private PermissionUserRepository permissionUserRepository;
  @Inject
  private Permission permission;

  @Override
  public void onLoad() {

  }

  @Override
  public void onDisable() {


  }

  @Override
  public void onEnable() {

    ChiaraModule chiaraModule = ChiaraModule.withPlugin(this);
    Injector injector = Guice.createInjector(chiaraModule);
    injector.injectMembers(this);

    // Metrics
    setupMetrics();

    // Configs
    String pluginDataFolderPath = getDataFolder().getAbsolutePath();
    Path groupsPath = Paths.get(pluginDataFolderPath, GROUPS_CONFIG_FILE_NAME);
    Path usersPath = Paths.get(pluginDataFolderPath, USERS_CONFIG_FILE_NAME);

    if (!Files.exists(groupsPath)) {
      logger.info("Can't find groups config. Copying default.");
      saveResource(GROUPS_CONFIG_FILE_NAME, false);
    }

    if (!Files.exists(usersPath)) {
      logger.info("Can't find users config. Copying default.");
      saveResource(USERS_CONFIG_FILE_NAME, false);
    }

    // Init service and repos

    // Register all players

    // Register listener
    PermissionListener permissionListener = new PermissionListener(this, permissionUserRepository);
    pluginManager.registerEvents(permissionListener, this);

    // Register services
    if (pluginManager.isPluginEnabled("Vault")) {

      logger.info("Found Vault. Hooking into permissions API.");
      servicesManager.register(Permission.class, permission, this, ServicePriority.Highest);
    }
  }

  private void setupMetrics() {

    getLogger().info("Setting up Metrics.");

    Metrics metrics = new Metrics(this);

    Metrics.SingleLineChart groupsChart = new Metrics.SingleLineChart("groups",
        () -> permissionGroupRepository.findAll().size());
    Metrics.SingleLineChart averageFoodLevelChart = new Metrics.SingleLineChart(
        "average_food_level", () -> {
      OptionalDouble average = Bukkit.getOnlinePlayers()
          .stream()
          .mapToInt(Player::getFoodLevel).average();
      return (int) average.orElse(-1D);
    });
    Metrics.SingleLineChart averageExperienceLevelChart = new Metrics.SingleLineChart(
        "average_experience_level", () -> {
      OptionalDouble average = Bukkit.getOnlinePlayers()
          .stream()
          .mapToInt(Player::getLevel).average();
      return (int) average.orElse(-1D);
    });

    metrics.addCustomChart(groupsChart);
    metrics.addCustomChart(averageFoodLevelChart);
    metrics.addCustomChart(averageExperienceLevelChart);
  }
}
