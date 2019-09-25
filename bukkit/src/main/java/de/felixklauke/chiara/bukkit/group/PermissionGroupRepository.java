package de.felixklauke.chiara.bukkit.group;

import java.util.Optional;
import java.util.Set;

public interface PermissionGroupRepository {

  Optional<PermissionGroup> find(String uniqueId);

  Set<PermissionGroup> findAll();
}
