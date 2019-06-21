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

public class YamlPermissionUserRepository extends YamlPermissionRepository implements PermissionUserRepository {

    private final Map<UUID, PermissionUser> permissionUsers = Maps.newHashMap();

    public YamlPermissionUserRepository(Path config) {
        super(config);
        readUsers();
    }

    private void readUsers() {

        ObjectMapper objectMapper = getObjectMapper();

        try {
            PermissionUserConfig permissionUserConfig = objectMapper.readValue(getConfig().toFile(), PermissionUserConfig.class);
            permissionUsers.putAll(permissionUserConfig.getUsers());
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't read users.", e);
        }
    }

    @Override
    public PermissionUser findUser(UUID uniqueId) {

        PermissionUser permissionUser = permissionUsers.get(uniqueId);
        if (permissionUser == null) {
            return null;
        }

        permissionUser.setUniqueId(uniqueId);
        return permissionUser;
    }

    @Override
    public void reloadUsers() {

        permissionUsers.clear();
        readUsers();
    }

    @Override
    public void saveUsers() {

        ObjectMapper objectMapper = getObjectMapper();
        PermissionUserConfig permissionUserConfig = new PermissionUserConfig(permissionUsers);

        try {
            objectMapper.writeValue(getConfig().toFile(), permissionUserConfig);
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't write users.", e);
        }
    }
}
