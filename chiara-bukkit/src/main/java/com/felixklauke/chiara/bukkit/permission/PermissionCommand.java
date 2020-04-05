package com.felixklauke.chiara.bukkit.permission;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public final class PermissionCommand implements CommandExecutor, TabCompleter {
  @Override
  public boolean onCommand(
    CommandSender sender,
    Command command,
    String label,
    String[] args
  ) {
    return true;
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
