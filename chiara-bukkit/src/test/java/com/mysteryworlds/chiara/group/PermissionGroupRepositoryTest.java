package com.mysteryworlds.chiara.group;

import java.nio.file.Paths;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yaml.snakeyaml.Yaml;

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
      new Yaml()
    );
  }

  @Test
  void testLoad() {
    groupRepository.load();
    var groups = groupRepository.findAll();
  }
}
