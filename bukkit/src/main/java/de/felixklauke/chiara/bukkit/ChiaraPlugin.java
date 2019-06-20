package de.felixklauke.chiara.bukkit;

import de.felixklauke.chiara.bukkit.command.PermissionsCommand;
import de.felixklauke.chiara.bukkit.listener.PlayerListener;
import de.felixklauke.chiara.bukkit.repository.PermissionGroupRepository;
import de.felixklauke.chiara.bukkit.repository.PermissionUserRepository;
import de.felixklauke.chiara.bukkit.repository.yaml.YamlPermissionGroupRepository;
import de.felixklauke.chiara.bukkit.repository.yaml.YamlPermissionUserRepository;
import de.felixklauke.chiara.bukkit.service.PermissionsService;
import de.felixklauke.chiara.bukkit.service.PermissionsServiceImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ChiaraPlugin extends JavaPlugin {

    private static final String GROUPS_CONFIG_FILE_NAME = "groups.yml";
    private static final String USERS_CONFIG_FILE_NAME = "users.yml";

    private PermissionsService permissionsService;

    @Override
    public void onLoad() {


    }

    @Override
    public void onEnable() {

        // Configs
        saveResource(GROUPS_CONFIG_FILE_NAME, false);
        saveResource(USERS_CONFIG_FILE_NAME, false);

        Path groupsPath = Paths.get(getDataFolder().getAbsolutePath(), GROUPS_CONFIG_FILE_NAME);
        Path usersPath = Paths.get(getDataFolder().getAbsolutePath(), USERS_CONFIG_FILE_NAME);

        // Init service and repos
        PermissionUserRepository userRepository = new YamlPermissionUserRepository(usersPath);
        PermissionGroupRepository groupRepository = new YamlPermissionGroupRepository(groupsPath);
        permissionsService = new PermissionsServiceImpl(this, groupRepository, userRepository);

        // Register all players
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> permissionsService.registerPlayer(onlinePlayer));

        // Register listener
        getServer().getPluginManager().registerEvents(new PlayerListener(permissionsService), this);

        // Register commands
        PermissionsCommand permissionsCommand = new PermissionsCommand();
        getCommand("permissions").setExecutor(permissionsCommand);
    }

    @Override
    public void onDisable() {

        // Unregister all players
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> permissionsService.unregisterPlayer(onlinePlayer));
    }
}
