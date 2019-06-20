package de.felixklauke.chiara.bukkit.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PermissionGroupConfig {

    @JsonProperty("groups")
    private Map<String, PermissionGroup> groups;

    public PermissionGroupConfig() {
    }

    public PermissionGroupConfig(Map<String, PermissionGroup> permissionGroups) {
        this.groups = permissionGroups;
    }

    public Map<String, PermissionGroup> getPermissionGroups() {
        return groups;
    }

    public void setPermissionGroups(Map<String, PermissionGroup> permissionGroups) {
        this.groups = permissionGroups;
    }
}
