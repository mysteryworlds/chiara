package com.mysteryworlds.chiara.permission;

import java.util.List;
import javax.inject.Inject;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public final class PermissionCommand implements CommandExecutor, TabCompleter {
  private final Permission permission;

  @Inject
  private PermissionCommand(Permission permission) {
    this.permission = permission;
  }

  @Override
  public boolean onCommand(
    CommandSender sender,
    Command command,
    String label,
    String[] args
  ) {
    return false;
  }

  @Override
  public List<String> onTabComplete(
    CommandSender sender,
    Command command,
    String alias,
    String[] args
  ) {
    return List.of();
  }
}
