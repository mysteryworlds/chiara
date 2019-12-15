package de.felixklauke.chiara.bukkit.user;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionUserRepository {

  Optional<PermissionUser> find(UUID uniqueId);

  Optional<PermissionUser> findByName(String username);

  List<PermissionUser> findAll();

  PermissionUser findByNameOrCreate(String username);
}
