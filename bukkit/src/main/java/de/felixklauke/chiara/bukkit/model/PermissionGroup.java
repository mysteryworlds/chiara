package de.felixklauke.chiara.bukkit.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionGroup {

    private transient String name;
    @JsonProperty("permissions")
    private Map<String, Boolean> permissions;
    @JsonProperty("worlds")
    private Map<String, Map<String, Boolean>> worldPermissions;
    @JsonProperty("inheritance")
    private List<String> inheritance;

    public PermissionGroup() {
    }

    public PermissionGroup(String name, Map<String, Boolean> permissions, Map<String, Map<String, Boolean>> worldPermissions, List<String> inheritance) {
        this.name = name;
        this.permissions = permissions;
        this.worldPermissions = worldPermissions;
        this.inheritance = inheritance;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions == null ? new HashMap<>() : new HashMap<>(permissions);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getInheritance() {
        return inheritance == null ? new ArrayList<>() : new ArrayList<>(inheritance);
    }

    public Map<String, Boolean> getWorldPermissions(String worldName) {

        if (worldPermissions == null) {
            return new HashMap<>();
        }

        Map<String, Boolean> worldPermissions = this.worldPermissions.get(worldName);
        return worldPermissions == null ? new HashMap<>() : new HashMap<>(worldPermissions);
    }
}
