package com.felixklauke.chiara.bukkit;

import com.felixklauke.chiara.bukkit.group.PermissionGroupRepository;
import com.felixklauke.chiara.bukkit.user.PermissionUserRepository;
import com.felixklauke.chiara.bukkit.user.PermissionUserSession;
import com.felixklauke.chiara.bukkit.user.PermissionUserSessionFactory;
import com.felixklauke.chiara.bukkit.user.PermissionUserSessionRegistry;
import com.felixklauke.chiara.bukkit.user.PermissionUserSessionTrigger;
import com.google.inject.Guice;
import javax.inject.Inject;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChiaraPlugin extends JavaPlugin {
  @Inject
  private PermissionGroupRepository groupRepository;
  @Inject
  private PermissionUserRepository userRepository;
  @Inject
  private PermissionUserSessionFactory sessionFactory;
  @Inject
  private PermissionUserSessionRegistry sessionRegistry;
  @Inject
  private Permission vaultPermissions;
  @Inject
  private PluginManager pluginManager;
  @Inject
  private ServicesManager servicesManager;

  @Inject
  private PermissionUserSessionTrigger sessionTrigger;

  @Override
  public void onEnable() {
    loadGroups();
    setupAndStartDependencyInjection();
    registerListeners();
    registerVaultPermission();
    startUserSessions();
  }

  private void loadGroups() {
    groupRepository.load();
  }

  private void loadUsers() {
    userRepository.load();
  }

  private void setupAndStartDependencyInjection() {
    var injector = Guice.createInjector(ChiaraModule.withPlugin(this));
    injector.injectMembers(this);
  }

  private void registerListeners() {
    pluginManager.registerEvents(sessionTrigger, this);
  }

  private void registerVaultPermission() {
    servicesManager.register(
      Permission.class,
      vaultPermissions,
      this,
      ServicePriority.High
    );
  }

  private void startUserSessions() {
    Bukkit.getOnlinePlayers().forEach(this::startUserSession);
  }

  private void startUserSession(Player player) {
    var user = userRepository.findOrCreateUser(player.getUniqueId());
    var session = sessionFactory.createSession(player, user);
    sessionRegistry.register(session);
  }

  @Override
  public void onDisable() {
    closeUserSessions();
    saveUsers();
    saveGroups();
  }

  private void saveUsers() {
    userRepository.save();
  }

  private void saveGroups() {
    groupRepository.save();
  }

  private void closeUserSessions() {
    sessionRegistry.findAll().forEach(PermissionUserSession::close);
    sessionRegistry.clear();
  }
}
