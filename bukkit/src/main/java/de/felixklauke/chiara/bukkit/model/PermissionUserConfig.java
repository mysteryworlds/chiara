package de.felixklauke.chiara.bukkit.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.UUID;

public class PermissionUserConfig {

    @JsonProperty("users")
    private Map<UUID, PermissionUser> users;

    public PermissionUserConfig() {
    }

    public PermissionUserConfig(Map<UUID, PermissionUser> permissionUsers) {
        this.users = permissionUsers;
    }

    public Map<UUID, PermissionUser> getPermissionUsers() {
        return users;
    }

    public void setPermissionUsers(Map<UUID, PermissionUser> permissionUsers) {
        this.users = permissionUsers;
    }
}
