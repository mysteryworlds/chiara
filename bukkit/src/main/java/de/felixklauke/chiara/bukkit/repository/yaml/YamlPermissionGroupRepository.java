package de.felixklauke.chiara.bukkit.repository.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import de.felixklauke.chiara.bukkit.model.PermissionGroup;
import de.felixklauke.chiara.bukkit.model.PermissionGroupConfig;
import de.felixklauke.chiara.bukkit.repository.PermissionGroupRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class YamlPermissionGroupRepository extends YamlPermissionRepository implements PermissionGroupRepository {

    private final Map<String, PermissionGroup> permissionGroups = Maps.newHashMap();

    public YamlPermissionGroupRepository(Path config) {
        super(config);
        readGroups();
    }

    private void readGroups() {

        ObjectMapper objectMapper = getObjectMapper();

        try {
            PermissionGroupConfig permissionGroupConfig = objectMapper.readValue(getConfig().toFile(), PermissionGroupConfig.class);
            permissionGroups.putAll(permissionGroupConfig.getGroups());
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't read groups.", e);
        }
    }

    @Override
    public PermissionGroup findGroup(String groupName) {

        PermissionGroup permissionGroup = permissionGroups.get(groupName);
        if (permissionGroup == null) {
            return null;
        }

        permissionGroup.setName(groupName);
        return permissionGroup;
    }

    @Override
    public void saveGroups() {

        PermissionGroupConfig permissionGroupConfig = new PermissionGroupConfig(permissionGroups);
        ObjectMapper objectMapper = getObjectMapper();

        try {
            objectMapper.writeValue(getConfig().toFile(), permissionGroupConfig);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't write groups", e);
        }
    }

    @Override
    public void reloadGroups() {

        permissionGroups.clear();
        readGroups();
    }
}
