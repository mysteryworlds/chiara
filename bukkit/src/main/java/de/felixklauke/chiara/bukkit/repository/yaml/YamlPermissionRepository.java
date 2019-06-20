package de.felixklauke.chiara.bukkit.repository.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.nio.file.Path;

public class YamlPermissionRepository {

    private final Path config;
    private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    public YamlPermissionRepository(Path config) {
        this.config = config;
    }

    public Path getConfig() {
        return config;
    }

    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
