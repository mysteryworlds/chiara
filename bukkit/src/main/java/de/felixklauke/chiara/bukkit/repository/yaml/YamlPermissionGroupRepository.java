package de.felixklauke.chiara.bukkit.repository.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import de.felixklauke.chiara.bukkit.model.PermissionGroup;
import de.felixklauke.chiara.bukkit.model.PermissionGroupConfig;
import de.felixklauke.chiara.bukkit.repository.PermissionGroupRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YamlPermissionGroupRepository extends YamlPermissionRepository implements PermissionGroupRepository {

    private final Logger logger;
    private final Map<String, PermissionGroup> permissionGroups = Maps.newHashMap();

    public YamlPermissionGroupRepository(Path config, Logger logger) {
        super(config);
        this.logger = logger;
        readGroups();
    }

    private void readGroups() {

        ObjectMapper objectMapper = getObjectMapper();

        try {
            PermissionGroupConfig permissionGroupConfig = objectMapper.readValue(getConfig().toFile(), PermissionGroupConfig.class);
            for (Map.Entry<String, PermissionGroup> entry : permissionGroupConfig.getGroups().entrySet()) {
                entry.getValue().setName(entry.getKey());
            }

            int groupAmount = permissionGroupConfig.getGroups().size();
            logger.fine(String.format("Successfully read %d groups from permission config.", groupAmount));

            permissionGroups.putAll(permissionGroupConfig.getGroups());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't read permission groups from config.", e);
        }
    }

    @Override
    public PermissionGroup findGroup(String groupName) {

        return permissionGroups.get(groupName);
    }

    @Override
    public void writeGroups() {

        PermissionGroupConfig permissionGroupConfig = new PermissionGroupConfig(permissionGroups);
        ObjectMapper objectMapper = getObjectMapper();

        try {
            objectMapper.writeValue(getConfig().toFile(), permissionGroupConfig);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't write permission groups to config.", e);
        }
    }

    @Override
    public void reloadGroups() {

        permissionGroups.clear();
        readGroups();
    }

    @Override
    public List<PermissionGroup> findGroups() {

        return new ArrayList<>(permissionGroups.values());
    }

    @Override
    public PermissionGroup createGroup(String group) {

        PermissionGroup permissionGroup = new PermissionGroup(group, null, null, null);
        saveGroup(permissionGroup);
        return permissionGroup;
    }

    @Override
    public void saveGroup(PermissionGroup permissionGroup) {

        permissionGroups.put(permissionGroup.getName(), permissionGroup);
    }
}
