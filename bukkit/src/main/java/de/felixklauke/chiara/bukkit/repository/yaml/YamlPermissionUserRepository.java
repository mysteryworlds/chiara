package de.felixklauke.chiara.bukkit.repository.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import de.felixklauke.chiara.bukkit.model.PermissionUser;
import de.felixklauke.chiara.bukkit.model.PermissionUserConfig;
import de.felixklauke.chiara.bukkit.repository.PermissionUserRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class YamlPermissionUserRepository extends YamlPermissionRepository implements PermissionUserRepository {

    private final Logger logger;
    private final Map<UUID, PermissionUser> permissionUsers = Maps.newHashMap();

    public YamlPermissionUserRepository(Path config, Logger logger) {
        super(config);
        this.logger = logger;
        readUsers();
    }

    private void readUsers() {

        ObjectMapper objectMapper = getObjectMapper();

        try {
            PermissionUserConfig permissionUserConfig = objectMapper.readValue(getConfig().toFile(), PermissionUserConfig.class);
            for (Map.Entry<UUID, PermissionUser> entry : permissionUserConfig.getUsers().entrySet()) {
                entry.getValue().setUniqueId(entry.getKey());
            }

            int userAmount = permissionUserConfig.getUsers().size();
            logger.fine(String.format("Successfully read %d users from permission config.", userAmount));

            permissionUsers.putAll(permissionUserConfig.getUsers());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't read permission users from config.", e);
        }
    }

    @Override
    public PermissionUser findUser(UUID uniqueId) {

        return permissionUsers.get(uniqueId);
    }

    @Override
    public void reloadUsers() {

        permissionUsers.clear();
        readUsers();
    }

    @Override
    public void writeUsers() {

        ObjectMapper objectMapper = getObjectMapper();
        PermissionUserConfig permissionUserConfig = new PermissionUserConfig(permissionUsers);

        try {
            objectMapper.writeValue(getConfig().toFile(), permissionUserConfig);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't write permission groups to config.", e);
        }
    }

    @Override
    public PermissionUser createUser(UUID uniqueId) {

        PermissionUser permissionUser = new PermissionUser(uniqueId, null, null, null);
        saveUser(permissionUser);
        return permissionUser;
    }

    @Override
    public void saveUser(PermissionUser permissionUser) {

        permissionUsers.put(permissionUser.getUniqueId(), permissionUser);
    }
}
