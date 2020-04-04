package com.felixklauke.chiara.bukkit.user;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.inject.Singleton;

@Singleton
public final class PermissionUserSessionRegistry {
  private final Set<PermissionUserSession> sessions;

  private PermissionUserSessionRegistry(
    Set<PermissionUserSession> sessions
  ) {
    this.sessions = sessions;
  }

  public static PermissionUserSessionRegistry empty() {
    return new PermissionUserSessionRegistry(Sets.newHashSet());
  }

  public Optional<PermissionUserSession> findSession(UUID userId) {
    Preconditions.checkNotNull(userId);
    return sessions.stream()
      .filter(session -> session.user().id().equals(userId))
      .findFirst();
  }

  public List<PermissionUserSession> findSessionsByGroup(String groupName) {
    Preconditions.checkNotNull(groupName);
    return new ArrayList<>();
  }

  public void register(PermissionUserSession session) {
    Preconditions.checkNotNull(session);
    sessions.add(session);
  }

  public void removeByUser(UUID userId) {
    sessions.removeIf(session -> session.user().id().equals(userId));
  }

  public List<PermissionUserSession> findAll() {
    return List.copyOf(sessions);
  }

  public void clear() {
    sessions.forEach(PermissionUserSession::close);
    sessions.clear();
  }
}
