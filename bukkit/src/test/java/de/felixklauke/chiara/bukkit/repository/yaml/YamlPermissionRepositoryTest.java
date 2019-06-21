package de.felixklauke.chiara.bukkit.repository.yaml;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class YamlPermissionRepositoryTest {

    @Mock
    private Path path;

    private YamlPermissionRepository yamlPermissionRepository;

    @BeforeEach
    void setUp() {
        yamlPermissionRepository = new YamlPermissionRepository(path);
    }

    @Test
    void testGetConfig() {

        Path config = yamlPermissionRepository.getConfig();
        assertEquals(path, config, "Path should match.");
    }

    @Test
    void testGetObjectMapper() {

        ObjectMapper objectMapper = yamlPermissionRepository.getObjectMapper();

        assertNotNull(objectMapper, "Object mapper should not be null.");

        JsonFactory factory = objectMapper.getFactory();

        assertTrue(factory instanceof YAMLFactory, "Factory should be a yaml factory.");

        YAMLFactory yamlFactory = (YAMLFactory) factory;

        assertTrue(yamlFactory.isEnabled(YAMLGenerator.Feature.MINIMIZE_QUOTES), "Minimize quotes should be enabled");
        assertFalse(yamlFactory.isEnabled(YAMLGenerator.Feature.WRITE_DOC_START_MARKER), "Write doc start marker should be disaled");
    }
}
