package com.mysteryworlds.chiara.group;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mysteryworlds.chiara.permission.Permission;
import com.mysteryworlds.chiara.permission.PermissionEntity.Metadata;
import com.mysteryworlds.chiara.permission.PermissionStatus;
import com.mysteryworlds.chiara.permission.PermissionTable;
import com.mysteryworlds.chiara.permission.WorldPermissionTable;
import java.util.List;
import java.util.Map;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class GroupTableTest {
  private static final String EXAMPLE_WORLD = "exampleWorld";

  @Mock
  private PluginManager pluginManager;
  private GroupTable groups;

  @BeforeEach
  void setUp() {
    groups = GroupTable.empty();
  }

  @Test
  void testCalculateEffectiveWorldPermissions() {
    var permission = Permission.of("test");
    var permissionGroup = new PermissionGroup(
      "TestGroup",
      PermissionTable.withPermissions(List.of(permission)),
      GroupTable.empty(),
      WorldPermissionTable.withWorldPermissions(
        Map.of(EXAMPLE_WORLD, PermissionTable.withPermissions(Map.of(permission, PermissionStatus.DECLINED)))
      ),
      Metadata.empty(),
      pluginManager
    );
    groups.add(permissionGroup);
    var worldPermissions = groups.calculateEffectivePermissions(EXAMPLE_WORLD);
    var permissionStatus = worldPermissions.statusOf(permission);
    assertEquals(PermissionStatus.DECLINED, permissionStatus);
  }
}
