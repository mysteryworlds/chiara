package de.felixklauke.chiara.bukkit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionGroup {

    private String name;
    private Map<String, Boolean> permissions;
    private Map<String, Map<String, Boolean>> worldPermissions;
    private List<String> inheritance;

    public PermissionGroup(String name, Map<String, Boolean> permissions, Map<String, Map<String, Boolean>> worldPermissions, List<String> inheritance) {
        this.name = name;
        this.permissions = permissions;
        this.worldPermissions = worldPermissions;
        this.inheritance = inheritance;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions == null ? new HashMap<>() : new HashMap<>(permissions);
    }

    public String getName() {
        return name;
    }

    public List<String> getInheritance() {
        return new ArrayList<>(inheritance);
    }

    public Map<String, Boolean> getWorldPermissions(String worldName) {

        Map<String, Boolean> worldPermissions = this.worldPermissions.get(worldName);
        return worldPermissions == null ? new HashMap<>() : new HashMap<>(worldPermissions);
    }
}
