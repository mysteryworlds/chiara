package de.felixklauke.chiara.bukkit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PermissionUser {

    private UUID uniqueId;
    private Map<String, Boolean> permissions;
    private Map<String, Map<String, Boolean>> worldPermissions;
    private List<String> groups;

    public PermissionUser(UUID uniqueId, Map<String, Boolean> permissions, Map<String, Map<String, Boolean>> worldPermissions, List<String> groups) {
        this.uniqueId = uniqueId;
        this.permissions = permissions;
        this.worldPermissions = worldPermissions;
        this.groups = groups;
    }

    public List<String> getGroups() {
        return new ArrayList<>(groups);
    }

    public Map<String, Boolean> getPermissions() {
        return permissions == null ? new HashMap<>() : new HashMap<>(permissions);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Map<String, Boolean> getWorldPermissions(String worldName) {

        Map<String, Boolean> worldPermissions = this.worldPermissions.get(worldName);
        return worldPermissions == null ? new HashMap<>() : new HashMap<>(worldPermissions);
    }
}
