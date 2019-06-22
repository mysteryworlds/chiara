package de.felixklauke.chiara.bukkit.repository.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import java.nio.file.Path;

public class YamlPermissionRepository {

    private final Path config;


    public YamlPermissionRepository(Path config) {
        this.config = config;
    }

    public Path getConfig() {
        return config;
    }

    protected ObjectMapper getObjectMapper() {
        return new ObjectMapper(new YAMLFactory()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES));
    }
}
