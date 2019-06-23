package de.felixklauke.chiara.bukkit.command;

import de.felixklauke.chiara.bukkit.service.PermissionService;
import de.felixklauke.chiara.bukkit.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PermissionsCommand implements CommandExecutor, TabCompleter {

    private static final String MESSAGE_PREFIX = "§7[§cChiara§7]: §e";
    private final PermissionService permissionService;

    public PermissionsCommand(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        switch (args.length) {
            case 1: {
                String arg = args[0];

                if (arg.equalsIgnoreCase("list")) {
                    return showPermissions(commandSender, command);
                } else if (arg.equalsIgnoreCase("reload")) {
                    return reloadPermissions(commandSender, command);
                } else if (arg.equalsIgnoreCase("save")) {
                    return savePermissions(commandSender, command);
                }

                break;
            }
            case 2: {
                String arg = args[0];

                if (arg.equalsIgnoreCase("group")) {
                    String arg1 = args[1];
                    if (arg1.equalsIgnoreCase("list")) {
                        return showGroups(commandSender, command);
                    }
                }

                break;
            }
            case 4: {
                String arg0 = args[0];

                if (arg0.equalsIgnoreCase("user")) {

                    // The selected user
                    String arg1 = args[1];
                    String arg2 = args[2];

                    if (arg2.equalsIgnoreCase("group")) {

                        String arg3 = args[3];
                        if (arg3.equalsIgnoreCase("list")) {

                            return showUserGroups(commandSender, command, arg1);
                        }
                    }
                }

                break;
            }
            case 5: {
                String arg0 = args[0];
                if (arg0.equalsIgnoreCase("user")) {

                    // The selected user
                    String arg1 = args[1];
                    String arg2 = args[2];

                    if (arg2.equalsIgnoreCase("group")) {

                        String arg3 = args[3];
                        if (arg3.equalsIgnoreCase("add")) {

                            // The selected group
                            String arg4 = args[4];
                            return addUserGroup(commandSender, command, arg1, arg4);

                        } else if (arg3.equalsIgnoreCase("remove")) {

                            // The selected group
                            String arg4 = args[4];
                            return removeUserGroup(commandSender, command, arg1, arg4);
                        }
                    }
                }

                break;
            }
            default: {
                return false;
            }
        }

        return false;
    }

    /**
     * Show the groups of the given user.
     *
     * @param commandSender The command sender.
     * @param command       The command.
     * @param playerName    The name of the user.
     *
     * @return If its a valid command.
     */
    private boolean showUserGroups(CommandSender commandSender, Command command, String playerName) {

        // Check permission
        if (!commandSender.hasPermission("chiara.command.permissions.user.group.list")) {
            commandSender.sendMessage(command.getPermissionMessage());
            return true;
        }

        UUID uniqueId = BukkitUtils.getUniqueIdByPlayerName(playerName);
        List<String> groups = permissionService.getGroups(uniqueId);

        if (groups.isEmpty()) {
            commandSender.sendMessage(String.format("%sIt seems like %s%s%s isn't member of any group.", MESSAGE_PREFIX, ChatColor.GOLD, playerName, ChatColor.YELLOW));
        } else {
            commandSender.sendMessage(String.format("%s%s%s%s is member %s%d%s groups:", MESSAGE_PREFIX, ChatColor.GOLD, playerName, ChatColor.YELLOW, ChatColor.GOLD, groups.size(), ChatColor.YELLOW));

            for (String group : groups) {
                commandSender.sendMessage(String.format("%s- %s%s", MESSAGE_PREFIX, ChatColor.GOLD, group));
            }
        }

        return true;
    }

    /**
     * Try to remove a user from a group.
     *
     * @param commandSender The command sender.
     * @param command       The command.
     * @param playerName    The name of the user.
     * @param groupName     The name of the group.
     *
     * @return If its a valid command.
     */
    private boolean removeUserGroup(CommandSender commandSender, Command command, String playerName, String groupName) {

        // Check permission
        if (!commandSender.hasPermission("chiara.command.permissions.user.group.remove")) {
            commandSender.sendMessage(command.getPermissionMessage());
            return true;
        }

        UUID uniqueId = BukkitUtils.getUniqueIdByPlayerName(playerName);
        permissionService.removeUserGroup(uniqueId, groupName);
        commandSender.sendMessage(String.format("%sDu hast %s%s%s aus der Gruppe Gruppe %s%s%s entfernt.", MESSAGE_PREFIX, ChatColor.GOLD, playerName, ChatColor.YELLOW, ChatColor.GOLD, groupName, ChatColor.YELLOW));
        return true;
    }

    /**
     * Try to add a user to a group.
     *
     * @param commandSender The command sender.
     * @param command       The command.
     * @param playerName    The player to add to a group.
     * @param groupName     The group.
     *
     * @return If its a valid command.
     */
    private boolean addUserGroup(CommandSender commandSender, Command command, String playerName, String groupName) {

        // Check permission
        if (!commandSender.hasPermission("chiara.command.permissions.user.group.add")) {
            commandSender.sendMessage(command.getPermissionMessage());
            return true;
        }

        UUID uniqueId = BukkitUtils.getUniqueIdByPlayerName(playerName);
        permissionService.addUserGroup(uniqueId, groupName);
        commandSender.sendMessage(String.format("%You have added %s%s%s to group %s%s%s.", MESSAGE_PREFIX, ChatColor.GOLD, playerName, ChatColor.YELLOW, ChatColor.GOLD, groupName, ChatColor.YELLOW));
        return true;
    }

    /**
     * Try to save the permissions to disk.
     *
     * @param commandSender The command sender.
     * @param command       The command.
     *
     * @return If ots a valid command.
     */
    private boolean savePermissions(CommandSender commandSender, Command command) {

        // Check permission
        if (!commandSender.hasPermission("chiara.command.permissions.save")) {
            commandSender.sendMessage(command.getPermissionMessage());
            return true;
        }

        permissionService.savePermissions();
        commandSender.sendMessage(String.format("%sThe permissions have been saved.", MESSAGE_PREFIX));
        return true;
    }

    /**
     * Show all existent groups.
     *
     * @param commandSender The command sender.
     * @param command       The command.
     *
     * @return If its a valid command.
     */
    private boolean showGroups(CommandSender commandSender, Command command) {

        // Check permission
        if (!commandSender.hasPermission("chiara.command.permissions.group.list")) {
            commandSender.sendMessage(command.getPermissionMessage());
            return true;
        }

        List<String> groups = permissionService.getGroups();
        int groupAmount = groups.size();

        commandSender.sendMessage(String.format("%sEs gibt %d Gruppen:", MESSAGE_PREFIX, groupAmount));
        for (String group : groups) {
            commandSender.sendMessage(String.format("%s- %s", MESSAGE_PREFIX, group));
        }

        return true;
    }

    /**
     * Reload the permissions users and groups.
     *
     * @param commandSender The one who wants to reload.
     * @param command       The command.
     *
     * @return If its a valid command.
     */
    private boolean reloadPermissions(CommandSender commandSender, Command command) {

        // Check permission
        if (!commandSender.hasPermission("chiara.command.permissions.reload")) {
            commandSender.sendMessage(command.getPermissionMessage());
            return true;
        }

        permissionService.reloadPermissions();
        commandSender.sendMessage(MESSAGE_PREFIX + "The permissions have been reload.");

        return true;
    }

    /**
     * Show the commandSender a list of his effective permissions.
     *
     * @param commandSender The player.
     * @param command       The command.
     *
     * @return If its a valid command.
     */
    private boolean showPermissions(CommandSender commandSender, Command command) {

        // Check permission
        if (!commandSender.hasPermission("chiara.command.permissions.list")) {
            commandSender.sendMessage(command.getPermissionMessage());
            return true;
        }

        // Get effective permissions and send them to the player.
        Set<PermissionAttachmentInfo> permissions = commandSender.getEffectivePermissions();
        permissions.stream()
                .map(permission -> permission.getPermission() + ": " + permission.getValue())
                .forEach(commandSender::sendMessage);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> completions = new ArrayList<>();

        // Check command ident level and copy partial matches
        switch (args.length) {
            case 1: {
                List<String> candidates = Arrays.asList("list", "reload", "save", "group", "user");
                StringUtil.copyPartialMatches(args[0], candidates, completions);
                break;
            }
            case 2: {

                String arg0 = args[0];
                if (arg0.equalsIgnoreCase("group")) {
                    List<String> candidates = Arrays.asList("list");
                    StringUtil.copyPartialMatches(args[1], candidates, completions);
                } else if (arg0.equalsIgnoreCase("user")) {
                    List<String> candidates = Bukkit.getOnlinePlayers().stream()
                            .map(HumanEntity::getName)
                            .collect(Collectors.toList());
                    StringUtil.copyPartialMatches(args[1], candidates, completions);
                }

                break;
            }
            case 3: {
                String arg0 = args[0];

                if (arg0.equalsIgnoreCase("user")) {
                    List<String> candidates = Arrays.asList("group");
                    StringUtil.copyPartialMatches(args[2], candidates, completions);
                }

                break;
            }
            case 4: {
                String arg0 = args[0];
                String arg2 = args[2];

                if (arg0.equalsIgnoreCase("user")) {

                    if (arg2.equalsIgnoreCase("group")) {

                        List<String> candidates = Arrays.asList("add", "remove", "list");
                        StringUtil.copyPartialMatches(args[3], candidates, completions);
                    }
                }

                break;
            }
            case 5: {

                String arg0 = args[0];
                String arg2 = args[2];
                String arg3 = args[3];

                if (arg0.equalsIgnoreCase("user")) {

                    if (arg2.equalsIgnoreCase("group")) {

                        if (arg3.equalsIgnoreCase("add") || arg3.equalsIgnoreCase("remove")) {

                            List<String> candidates = permissionService.getGroups();
                            StringUtil.copyPartialMatches(args[4], candidates, completions);
                        }
                    }
                }

                break;
            }
        }

        // Only sort if there actually are completions available
        if (completions.size() > 0) {
            Collections.sort(completions);
        }

        return completions;
    }
}
