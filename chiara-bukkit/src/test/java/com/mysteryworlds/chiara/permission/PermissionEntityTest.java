package com.mysteryworlds.chiara.permission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mysteryworlds.chiara.group.GroupTable;
import com.mysteryworlds.chiara.group.PermissionGroup;
import com.mysteryworlds.chiara.permission.PermissionEntity.Metadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class PermissionEntityTest {
  private PermissionTable permissions;
  private GroupTable groups;
  private WorldPermissionTable worldPermissions;
  private PermissionEntity permissionEntity;

  @BeforeEach
  void setUp() {
    permissions = PermissionTable.empty();
    groups = GroupTable.empty();
    worldPermissions = WorldPermissionTable.empty();
    permissionEntity = new PermissionEntity(
      permissions,
      groups,
      worldPermissions,
      Metadata.empty()
    );
  }

  @Test
  void testGroups() {
    var groups = permissionEntity.groups();
    assertTrue(groups.isEmpty());
  }

  @Test
  void testAddGroup() {
    var group = Mockito.mock(PermissionGroup.class);
    permissionEntity.addGroup(group);
    assertEquals(1, groups.count());
  }

  @Test
  void testRemoveGroup() {
    var group = Mockito.mock(PermissionGroup.class);
    permissionEntity.addGroup(group);

    permissionEntity.removeGroup(group);
    assertEquals(0, groups.count());
  }
}
