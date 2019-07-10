package de.felixklauke.chiara.bukkit;

import de.felixklauke.chiara.bukkit.command.PermissionsCommand;
import de.felixklauke.chiara.bukkit.listener.PlayerListener;
import de.felixklauke.chiara.bukkit.repository.PermissionGroupRepository;
import de.felixklauke.chiara.bukkit.repository.PermissionUserRepository;
import de.felixklauke.chiara.bukkit.repository.yaml.YamlPermissionGroupRepository;
import de.felixklauke.chiara.bukkit.repository.yaml.YamlPermissionUserRepository;
import de.felixklauke.chiara.bukkit.service.PermissionService;
import de.felixklauke.chiara.bukkit.service.PermissionServiceImpl;
import de.felixklauke.chiara.bukkit.vault.VaultPermissions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.OptionalDouble;
import java.util.logging.Logger;
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

  private PermissionUserRepository userRepository;
  private PermissionGroupRepository groupRepository;
  private PermissionService permissionService;

  @Override
  public void onLoad() {

  }

  @Override
  public void onDisable() {

    // Unregister all players
    Bukkit.getOnlinePlayers()
        .forEach(onlinePlayer -> permissionService.unregisterPlayer(onlinePlayer));

    permissionService.savePermissions();
  }

  @Override
  public void onEnable() {

    PluginManager pluginManager = getServer().getPluginManager();
    ServicesManager servicesManager = getServer().getServicesManager();
    Logger logger = getLogger();

    // Metrics
    setupMetrics();

    // Configs
    Path groupsPath = Paths.get(getDataFolder().getAbsolutePath(), GROUPS_CONFIG_FILE_NAME);
    Path usersPath = Paths.get(getDataFolder().getAbsolutePath(), USERS_CONFIG_FILE_NAME);

    if (!Files.exists(groupsPath)) {
      logger.info("Can't find groups config. Copying default.");
      saveResource(GROUPS_CONFIG_FILE_NAME, false);
    }

    if (!Files.exists(usersPath)) {
      logger.info("Can't find users config. Copying default.");
      saveResource(USERS_CONFIG_FILE_NAME, false);
    }

    // Init service and repos
    userRepository = new YamlPermissionUserRepository(usersPath, logger);
    groupRepository = new YamlPermissionGroupRepository(groupsPath, logger);
    permissionService = new PermissionServiceImpl(this, groupRepository, userRepository);

    // Register all players
    Bukkit.getOnlinePlayers()
        .forEach(onlinePlayer -> permissionService.registerPlayer(onlinePlayer));

    // Register listener
    pluginManager.registerEvents(new PlayerListener(permissionService), this);

    // Register commands
    PermissionsCommand permissionsCommand = new PermissionsCommand(permissionService);
    getCommand("permissions").setExecutor(permissionsCommand);
    getCommand("permissions").setTabCompleter(permissionsCommand);

    // Register services
    if (pluginManager.isPluginEnabled("Vault")) {

      logger.info("Found Vault. Hooking into permissions API.");
      VaultPermissions vaultPermissions = new VaultPermissions(this, permissionService);
      servicesManager.register(Permission.class, vaultPermissions, this, ServicePriority.Highest);
    }

    servicesManager.register(PermissionService.class, permissionService, this, ServicePriority.Highest);
  }

  private void setupMetrics() {

    getLogger().info("Setting up Metrics.");

    Metrics metrics = new Metrics(this);

    Metrics.SingleLineChart groupsChart = new Metrics.SingleLineChart("groups",
        () -> permissionService.getGroups().size());
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
