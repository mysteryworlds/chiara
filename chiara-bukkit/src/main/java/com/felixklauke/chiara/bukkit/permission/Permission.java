package com.felixklauke.chiara.bukkit.permission;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class Permission {
  private final String name;

  private Permission(String name) {
    this.name = name;
  }

  public static Permission of(String name) {
    Preconditions.checkNotNull(name);
    return new Permission(name);
  }

  public String name() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Permission that = (Permission) o;
    return Objects.equal(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}
