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
import net.milkbowl.vault.permission.Permission;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

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
    public void onEnable() {

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
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> permissionService.registerPlayer(onlinePlayer));

        // Register listener
        getServer().getPluginManager().registerEvents(new PlayerListener(permissionService), this);

        // Register commands
        PermissionsCommand permissionsCommand = new PermissionsCommand(permissionService);
        getCommand("permissions").setExecutor(permissionsCommand);
        getCommand("permissions").setTabCompleter(permissionsCommand);

        // Register services
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {

            logger.info("Found Vault. Hooking into permissions API.");
            VaultPermissions vaultPermissions = new VaultPermissions(this, permissionService);
            getServer().getServicesManager().register(Permission.class, vaultPermissions, this, ServicePriority.Highest);
        }
    }

    private void setupMetrics() {

        getLogger().info("Setting up Metrics.");

        Metrics metrics = new Metrics(this);
        Metrics.SingleLineChart groupsChart = new Metrics.SingleLineChart("groups", () -> permissionService.getGroups().length);
        metrics.addCustomChart(groupsChart);
    }

    @Override
    public void onDisable() {

        // Unregister all players
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> permissionService.unregisterPlayer(onlinePlayer));

        groupRepository.writeGroups();
        userRepository.writeUsers();
    }
}
