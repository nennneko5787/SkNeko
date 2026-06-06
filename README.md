# SkNeko

A Skript addon.

ほぼ個人用です

## Expressions

### Glow an entity

エンティティを光らせます。色を付けることもできます。

```applescript
make %entity% glow[ing] [[with color] %-color%] for %players%
```

```applescript
command /glow:
    trigger:
        make player glow for all players
        send "You are glowing now! :)" to player

on damage:
    make attacker glowing with color red for victim
```

### Unglow an entity

エンティティの発光を停止します。

```applescript
make %entity% unglow[ing] for %players%
```

```applescript
command /unglow:
    trigger:
        make player unglow for all players
        send "You are no longer glowing now" to player

on damage:
    make victim unglowing for attacker
```

### Is entity glowing for

エンティティが光っているか確認します。

```applescript
%entity% is glowing for %player%
%entity% (isn't|is not) glowing for %player%
```

```applescript
command /checkglow <player> <player>:
    trigger:
        if arg-1 is glowing for arg-2:
            send "%arg-1% is glowing for %arg-2%"

on damage:
    if attacker isn't glowing for victim:
        make attacker glowing with color red for victim
    else:
        make attacker unglowing for victim
```
