package de.felixklauke.chiara.bukkit.repository.yaml;

import com.google.common.collect.Maps;
import de.felixklauke.chiara.bukkit.model.PermissionUser;
import de.felixklauke.chiara.bukkit.repository.PermissionUserRepository;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class YamlPermissionUserRepository implements PermissionUserRepository {

    private final Path config;
    private final Logger logger;
    private final Map<UUID, PermissionUser> permissionUsers = Maps.newHashMap();

    public YamlPermissionUserRepository(Path config, Logger logger) {
        this.config = config;
        this.logger = logger;
        readUsers();
    }

    private void readUsers() {
        Yaml yaml = new Yaml();
    }

    @Override
    public void writeUsers() {
        Yaml yaml = new Yaml();
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
