package de.felixklauke.chiara.bukkit.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PermissionGroupTest {

    private static final String TEST_NAME = "test";
    private PermissionGroup permissionGroup;

    @BeforeEach
    void setUp() {
        permissionGroup = new PermissionGroup();
    }

    @Test
    void testGetPermissions() {

        Map<String, Boolean> permissions = permissionGroup.getPermissions();

        assertNotNull(permissions, "Permissions should not be null.");
    }

    @Test
    void testSetName() {

        permissionGroup.setName(TEST_NAME);
        assertEquals(TEST_NAME, permissionGroup.getName(), "Name should have changed.");
    }

    @Test
    void testGetName() {

        assertNull(permissionGroup.getName(), "Name should be null at beginning.");
    }

    @Test
    void testGetInheritance() {

        assertNotNull(permissionGroup.getInheritance(), "Inheritance should not be null.");
    }

    @Test
    void testGetWorldPermissions() {

        assertNotNull(permissionGroup.getWorldPermissions(), "World permissions should not be null.");
        assertNotNull(permissionGroup.getWorldPermissions(TEST_NAME), "World permissions should no be null.");
    }
}
