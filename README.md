[![Quality Gate Status](https://sonar.klauke-enterprises.com/api/project_badges/measure?project=chiara&metric=alert_status)](https://sonar.klauke-enterprises.com/dashboard?id=chiara)  
  
[![Coverage](https://sonar.klauke-enterprises.com/api/project_badges/measure?project=chiara&metric=coverage)](https://sonar.klauke-enterprises.com/dashboard?id=chiara)
  
# chiara
Simple but effective bukkit permissions plugin.

# Build Status
|             | Build Status                                                                                                            |
|-------------|-------------------------------------------------------------------------------------------------------------------------|
| Master      | [![Build Status](https://travis-ci.org/FelixKlauke/chiara.svg?branch=master)](https://travis-ci.org/FelixKlauke/chiara) |

# Configuration

In the following you can see a full reference of the groups and users with all available entry types:
- Player / Group Permissions
- World Permissions
- Players Groups / Group Inheritance

## Users
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

## Groups
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

# Commands

There is a general management command:
  
`/permissions [list|reload]` (Aliases: `perms`, `perm`)

# Permissions
```text
Admin Permission                   : chiara.*
Access to all commands             : chiara.command.*
Full access to permissions command : chiara.command.permissions.*
Command Permissions                : chiara.command.permissions
Command Permissions list           : chiara.command.permissions.list
Command Permissions reload         : chiara.command.permissions.reload
```
