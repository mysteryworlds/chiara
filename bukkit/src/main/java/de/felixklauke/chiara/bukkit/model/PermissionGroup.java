package de.felixklauke.chiara.bukkit.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionGroup {

    private transient String name;
    private Map<String, Boolean> permissions;
    private Map<String, Map<String, Boolean>> worldPermissions;
    private List<String> inheritance;

    public PermissionGroup() {
    }

    public PermissionGroup(String name, Map<String, Boolean> permissions, Map<String, Map<String, Boolean>> worldPermissions, List<String> inheritance) {
        this.name = name;
        this.permissions = permissions;
        this.worldPermissions = worldPermissions;
        this.inheritance = inheritance;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions == null ? new HashMap<>() : new HashMap<>(permissions);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getInheritance() {
        return inheritance == null ? new ArrayList<>() : new ArrayList<>(inheritance);
    }

    public void setInheritance(List<String> inheritance) {
        this.inheritance = inheritance;
    }

    public Map<String, Map<String, Boolean>> getWorldPermissions() {
        return worldPermissions == null ? new HashMap<>() : new HashMap<>(worldPermissions);
    }

    public void setWorldPermissions(Map<String, Map<String, Boolean>> worldPermissions) {
        this.worldPermissions = worldPermissions;
    }

    public Map<String, Boolean> getWorldPermissions(String worldName) {

        if (worldPermissions == null) {
            return new HashMap<>();
        }

        Map<String, Boolean> worldPermissions = this.worldPermissions.get(worldName);
        return worldPermissions == null ? new HashMap<>() : new HashMap<>(worldPermissions);
    }

    public void setPermission(String permission, boolean value) {

        if (permissions == null) {
            permissions = new HashMap<>();
        }

        permissions.put(permission, value);
    }

    public void setWorldPermission(String world, String permission, boolean value) {

        if (worldPermissions == null) {
            worldPermissions = new HashMap<>();
        }

        Map<String, Boolean> worldPermissions = this.worldPermissions.computeIfAbsent(world, k -> new HashMap<>());
        worldPermissions.put(permission, value);
    }

    public void unsetWorldPermission(String world, String permission) {

        if (worldPermissions == null) {
            return;
        }

        Map<String, Boolean> worldPermissions = this.worldPermissions.get(world);
        if (worldPermissions == null) {
            return;
        }

        worldPermissions.remove(permission);
    }

    public void unsetPermission(String permission) {

        if (permissions == null) {
            return;
        }

        permissions.remove(permission);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PermissionGroup{");
        sb.append("name='").append(name).append('\'');
        sb.append(", permissions=").append(permissions);
        sb.append(", worldPermissions=").append(worldPermissions);
        sb.append(", inheritance=").append(inheritance);
        sb.append('}');
        return sb.toString();
    }
}
