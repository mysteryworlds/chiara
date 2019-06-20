package de.felixklauke.chiara.bukkit;

import de.felixklauke.chiara.bukkit.service.PermissionsService;
import de.felixklauke.chiara.bukkit.service.PermissionsServiceImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ChiaraPlugin extends JavaPlugin {

    private PermissionsService permissionsService;

    @Override
    public void onLoad() {


    }

    @Override
    public void onEnable() {

        // Init service
        permissionsService = new PermissionsServiceImpl(this);

        // Register all players
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> permissionsService.registerPlayer(onlinePlayer));
    }

    @Override
    public void onDisable() {

        // Unregister all players
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> permissionsService.unregisterPlayer(onlinePlayer));
    }
}
