package de.felixklauke.chiara.bukkit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PermissionUser {

    @JsonIgnore
    private transient UUID uniqueId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("permissions")
    private Map<String, Boolean> permissions;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("worlds")
    private Map<String, Map<String, Boolean>> worldPermissions;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("groups")
    private List<String> groups;

    public PermissionUser() {
    }

    public PermissionUser(UUID uniqueId, Map<String, Boolean> permissions, Map<String, Map<String, Boolean>> worldPermissions, List<String> groups) {
        this.uniqueId = uniqueId;
        this.permissions = permissions;
        this.worldPermissions = worldPermissions;
        this.groups = groups;
    }

    public List<String> getGroups() {
        return groups == null ? new ArrayList<>() : new ArrayList<>(groups);
    }

    public Map<String, Boolean> getPermissions() {
        return permissions == null ? new HashMap<>() : new HashMap<>(permissions);
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public Map<String, Map<String, Boolean>> getWorldPermissions() {
        return worldPermissions == null ? new HashMap<>() : new HashMap<>(worldPermissions);
    }

    public Map<String, Boolean> getWorldPermissions(String worldName) {

        if (worldPermissions == null) {
            return new HashMap<>();
        }
        Map<String, Boolean> worldPermissions = this.worldPermissions.get(worldName);
        return worldPermissions == null ? new HashMap<>() : new HashMap<>(worldPermissions);
    }
}
