package de.felixklauke.chiara.bukkit.command;

import de.felixklauke.chiara.bukkit.service.PermissionsService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class PermissionsCommand implements CommandExecutor, TabCompleter {

    private static final String MESSAGE_PREFIX = "§7[§cChiara§7]: §e";
    private final PermissionsService permissionsService;

    public PermissionsCommand(PermissionsService permissionsService) {
        this.permissionsService = permissionsService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        switch (args.length) {
            case 1: {
                String arg = args[0];
                if (arg.equalsIgnoreCase("list")) {
                    return showPermissions(player, command);
                } else if (arg.equalsIgnoreCase("reload")) {
                    return reloadPermissions(player, command);
                }
                return false;
            }
            default: {
                return false;
            }
        }
    }

    /**
     * Reload the permissions users and groups.
     *
     * @param player The player who wants to reload.
     * @param command The command.
     * @return If its a valid command.
     */
    private boolean reloadPermissions(Player player, Command command) {

        // Check permission
        if (!player.hasPermission("chiara.command.permissions.reload")) {
            player.sendMessage(command.getPermissionMessage());
            return true;
        }

        permissionsService.reloadPermissions();
        player.sendMessage(MESSAGE_PREFIX + "The permissions have been reload.");

        return true;
    }

    /**
     * Show the player a list of his effective permissions.
     *
     * @param player The player.
     * @param command The command.
     * @return If its a valid command.
     */
    private boolean showPermissions(Player player, Command command) {

        // Check permission
        if (!player.hasPermission("chiara.command.permissions.list")) {
            player.sendMessage(command.getPermissionMessage());
            return true;
        }

        // Get effective permissions and send them to the player.
        Set<PermissionAttachmentInfo> permissions = player.getEffectivePermissions();
        permissions.stream()
                .map(permission -> permission.getPermission() + ": " + permission.getValue())
                .forEach(player::sendMessage);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> completions = new ArrayList<>();

        // Check command ident level and copy partial matches
        switch (args.length) {
            case 1: {
                List<String> candidates = Arrays.asList("list", "reload");
                StringUtil.copyPartialMatches(args[0], candidates, completions);
            }
        }

        // Only sort if there actually are completions available
        if (completions.size() > 0) {
            Collections.sort(completions);
        }

        return completions;
    }
}
