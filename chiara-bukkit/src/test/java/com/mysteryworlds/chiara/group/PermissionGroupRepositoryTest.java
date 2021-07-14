package com.mysteryworlds.chiara.group;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.nio.file.Paths;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PermissionGroupRepositoryTest {
  PermissionGroupRepository groupRepository;

  @Mock
  private PluginManager pluginManager;

  @BeforeEach
  void setUp() {
    groupRepository = new PermissionGroupRepository(
      new PermissionGroupFactory(pluginManager),
      Paths.get("src", "test", "resources", "groups.yml"),
      new ObjectMapper(new YAMLFactory())
    );
  }

  @Test
  void testLoad() {
    groupRepository.load();
    var groups = groupRepository.findAll();
  }
}
