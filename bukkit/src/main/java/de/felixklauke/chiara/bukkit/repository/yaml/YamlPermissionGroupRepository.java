package de.felixklauke.chiara.bukkit.repository.yaml;

import com.google.common.collect.Maps;
import de.felixklauke.chiara.bukkit.model.PermissionGroup;
import de.felixklauke.chiara.bukkit.repository.PermissionGroupRepository;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class YamlPermissionGroupRepository implements PermissionGroupRepository {

    private final Path config;
    private final Logger logger;
    private final Map<String, PermissionGroup> permissionGroups = Maps.newHashMap();

    public YamlPermissionGroupRepository(Path config, Logger logger) {
        this.config = config;
        this.logger = logger;
        readGroups();
    }

    private void readGroups() {
        Yaml yaml = new Yaml();

        try (BufferedReader reader = Files.newBufferedReader(config)){
            Map map = yaml.loadAs(reader, Map.class);
            Map<String, Object> groups = (Map<String, Object>) map.get("groups");

            groups.forEach((key, value) -> {
                Map<String, Object> groupMap = (Map<String, Object>) value;
                PermissionGroup permissionGroup = groupFromMap(key, groupMap);
                permissionGroups.put(key, permissionGroup);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PermissionGroup findGroup(String groupName) {

        return permissionGroups.get(groupName);
    }

    @Override
    public void writeGroups() {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        Map<String, Object> document = new HashMap<>();

        Map<String, Object> groups = new HashMap<>();
        permissionGroups.forEach((groupName, permissionGroup) -> {
            Map<String, Object> map = groupToMap(permissionGroup);
            groups.put(groupName, map);
        });

        document.put("groups", groups);

        try (BufferedWriter writer = Files.newBufferedWriter(config)) {
            yaml.dump(document, writer);
        } catch (IOException e) {
            e.printStackTrace();
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

    private Map<String, Object> groupToMap(PermissionGroup permissionGroup) {

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("permissions", permissionGroup.getPermissions());
        map.put("worlds", permissionGroup.getWorldPermissions());
        map.put("inheritance", permissionGroup.getInheritance());
        return map;
    }

    private PermissionGroup groupFromMap(String groupName, Map<String, Object> map) {

        PermissionGroup permissionGroup = new PermissionGroup();
        permissionGroup.setName(groupName);
        permissionGroup.setPermissions((Map<String, Boolean>) map.get("permissions"));
        permissionGroup.setWorldPermissions((Map<String, Map<String, Boolean>>) map.get("worlds"));
        permissionGroup.setInheritance((List<String>) map.get("inheritance"));
        return permissionGroup;
    }
}
