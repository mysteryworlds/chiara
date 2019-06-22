package de.felixklauke.chiara.bukkit.command;

import de.felixklauke.chiara.bukkit.service.PermissionService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
                }
                return false;
            }
            case 2: {
                String arg = args[0];
                if (arg.equalsIgnoreCase("group")) {
                    arg = args[1];
                    if (arg.equalsIgnoreCase("list")) {
                        return showGroups(commandSender, command);
                    }
                }
            }
            default: {
                return false;
            }
        }
    }

    /**
     * Show all existent groups.
     *
     * @param commandSender The command sender.
     * @param command The command.
     * @return If its a valid command.
     */
    private boolean showGroups(CommandSender commandSender, Command command) {

        // Check permission
        if (!commandSender.hasPermission("chiara.command.permissions.group.list")) {
            commandSender.sendMessage(command.getPermissionMessage());
            return true;
        }

        String[] groups = permissionService.getGroups();
        int groupAmount = groups.length;

        commandSender.sendMessage(String.format("%sEs gibt %d Gruppen:", MESSAGE_PREFIX, groupAmount));
        for (String group : groups) {
            commandSender.sendMessage(String.format("%s- %s", MESSAGE_PREFIX, group));
        }

        return true;
    }

    /**
     * Reload the permissions users and groups.
     *
     * @param commandSender The oe who wants to reload.
     * @param command The command.
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
     * @param command The command.
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
                List<String> candidates = Arrays.asList("list", "reload", "group");
                StringUtil.copyPartialMatches(args[0], candidates, completions);
                break;
            }
            case 2: {
                List<String> candidates = Arrays.asList("list");
                StringUtil.copyPartialMatches(args[1], candidates, completions);
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
