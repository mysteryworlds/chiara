package de.felixklauke.chiara.bukkit.group;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PermissionGroupRepository {

  Optional<PermissionGroup> find(String uniqueId);

  Set<PermissionGroup> findAll();
}
