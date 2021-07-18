# chiara
Simple but effective bukkit permissions plugin.

## Description
The intention of chiara is a provide a very simple and comprehensive concept of permissions. It is heavily
inspired by [PermissionsBukkit](https://github.com/SpaceManiac/PermissionsBukkit) in terms of
configuration and simplicity. This plugin is meant to be used for small servers where all data can
be held in memory easily.

## Build Status
| Build Status | Gradle Build                                                                                                                                                                          | GitHub Release  |
|--------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------- |
| master       | [![Gradle Build](https://github.com/mysteryworlds/chiara/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/mysteryworlds/chiara/actions/workflows/gradle.yml) | [![GitHub Release](https://github.com/mysteryworlds/chiara/actions/workflows/create-release.yml/badge.svg)](https://github.com/mysteryworlds/chiara/actions/workflows/create-release.yml) |

## Information
- [Website](https://www.mysteryworlds.com)
- [Issue Tracker](https://github.com/mysteryworlds/chiara/issues)
- [Wiki](https://github.com/mysteryworlds/chiara/wiki)
- [License](LICENSE)

## Contributing
If you are interested in contributing, please read the [CONTRIBUTING.md](CONTRIBUTING.md).

## Configuration

In the following you can see a full reference of the groups and users with all available entry types:
- Player / Group Permissions
- World Permissions
- Players Groups / Group Inheritance

### Users
Default users.yml:
```yaml
users:
  f675a756-4b50-4e6e-a6bf-6713e869f83d:
    # User permissions
    permissions:
      epic.*: true
      world.build: true
      worldedit.butcher: false
      world.admin.*: true
      worldguard.region: false
    # World specific permissions
    worlds:
      world:
        command.spawn: true
        command.kill: false
        world.build: false
      world_nether:
        command.spawn: false
        world.build: false
    # Groups
    groups:
      - moderator
      - builder
```

### Groups
Default groups.yml:
```yaml
groups:
  # Admin Group
  admin:
    # Group permissions
    permissions:
      worldedit.admin.*: true
    # World specific permissions
    worlds:
      world:
        worldguard.*: true
    # Inheritance
    inheritance:
      - moderator

  # Moderator group
  moderator:
    # Group permissions
    permissions:
      server.broadcast: true
      server.kick: true
      server.kill: true
    # World specific permissions
    worlds:
      world_nether:
        command.gamemode: false
```

## Commands

There is a general management command:

`/permissions [list|reload]` (Aliases: `perms`, `perm`)

## Permissions
```yaml
permissions:

  chiara.*:
    description: Gives you full access to chiara
    children:
      - chiara.command.*

  chiara.command.*:
    description: Gives you access to all commands
    children:
      - chiara.command.permissions.*

  chiara.command.permissions.*:
    description: Permissions to work with all permissions
    children:
      - chiara.command.permissions
      - chiara.command.permissions.list
      - chiara.command.permissions.reload
      - chiara.command.permissions.save
      - chiara.command.permissions.group.*
      - chiara.command.permissions.user.*

  chiara.command.permissions:
    description: Permissions to work with permissions

  chiara.command.permissions.list:
    description: Permissions to show all your permissions

  chiara.command.permissions.reload:
    description: Reload the permissions from the config

  chiara.command.permissions.save:
    description: Save the permissions to the config

  chiara.command.permissions.group.*:
    description: Access to group commands
    children:
      - chiara.command.permissions.group.list

  chiara.command.permissions.group.list:
    description: Permission to print out all groups

  chiara.command.permissions.user.*:
    description: Access to iser commands
    children:
      - chiara.command.permissions.user.group.*

  chiara.command.permissions.user.group.*:
    description: Access to user group commands
    children:
      - chiara.command.permissions.user.group.add
      - chiara.command.permissions.user.group.remove
      - chiara.command.permissions.user.group.list

  chiara.command.permissions.user.group.add:
    description: Permission to add a user to a group

  chiara.command.permissions.user.group.remove:
    description: Permission to remove a user from a group

  chiara.command.permissions.user.group.list:
    description: Permission to list a users groups.
```

## Developers
- [Felix Klauke](https://github.com/felixklauke)
