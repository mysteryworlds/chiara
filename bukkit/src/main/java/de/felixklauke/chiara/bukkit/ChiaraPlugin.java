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

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChiaraPlugin extends JavaPlugin {

    private static final String GROUPS_CONFIG_FILE_NAME = "groups.yml";
    private static final String USERS_CONFIG_FILE_NAME = "users.yml";

    private PermissionUserRepository userRepository;
    private PermissionGroupRepository groupRepository;
    private PermissionsService permissionsService;

    @Override
    public void onLoad() {


    }

    @Override
    public void onEnable() {

        // Configs
        Path groupsPath = Paths.get(getDataFolder().getAbsolutePath(), GROUPS_CONFIG_FILE_NAME);
        Path usersPath = Paths.get(getDataFolder().getAbsolutePath(), USERS_CONFIG_FILE_NAME);

        if (!Files.exists(groupsPath)) {
            saveResource(GROUPS_CONFIG_FILE_NAME, false);
        }

        if (!Files.exists(usersPath)) {
            saveResource(USERS_CONFIG_FILE_NAME, false);
        }

        // Init service and repos
        userRepository = new YamlPermissionUserRepository(usersPath);
        groupRepository = new YamlPermissionGroupRepository(groupsPath);
        permissionsService = new PermissionsServiceImpl(this, groupRepository, userRepository);

        // Register all players
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> permissionsService.registerPlayer(onlinePlayer));

        // Register listener
        getServer().getPluginManager().registerEvents(new PlayerListener(permissionsService), this);

        // Register commands
        PermissionsCommand permissionsCommand = new PermissionsCommand(permissionsService);
        getCommand("permissions").setExecutor(permissionsCommand);
        getCommand("permissions").setTabCompleter(permissionsCommand);
    }

    @Override
    public void onDisable() {

        // Unregister all players
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> permissionsService.unregisterPlayer(onlinePlayer));

        groupRepository.saveGroups();
        userRepository.saveUsers();
    }
}
