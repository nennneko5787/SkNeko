# SkNeko

A Skript addon.

ほぼ個人用です

## Requirements
- Paper (not Spigot) >= 1.21.11
- Skript >= v2.15.0

これ以外のバージョンでの動作は保証できません。自己責任でお願いします。

## Features
- Glow an entity
- Glow a block

> [!important]
> Glow系構文はプレイヤー/エンティティに既に設定されているチームと競合します。

- ViaVersion
- and more...

## Syntaxes

### Do nothing

はい。文字通り、何もしません

```applescript
[do] nothing
```

```applescript
command /notimplemented:
    trigger:
        do nothing # I will implement the command
```

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

プレイヤーに対してエンティティが光っているか確認します。

```applescript
%entity% is glowing for %player%
%entity% (isn't|is not) glowing for %player%
```

```applescript
command /checkglow <player> <player>:
    trigger:
        if arg-1 is glowing for arg-2:
            send "%arg-1% is glowing for %arg-2%" to player
        else:
            send "%arg-1% isn't glowing for %arg-2%" to player

on damage:
    if attacker isn't glowing for victim:
        make attacker glowing with color red for victim
    else:
        make attacker unglowing for victim
```

### Glow a block

ブロックを光らせます。色を付けることもできます。

> [!note]
> ブロックディスプレイを使用しないため一部のブロックで不自然な光り方をします。将来のアップデートで修正される予定です。

```applescript
make %block% glow[ing] [[with color] %-color%] for %players%
```

```applescript
on left click:
    make event-block glow for all players
```

### Unglow a block

ブロックの発光を停止します。

```applescript
make %block% unglow[ing] for %players%
```

```applescript
on right click:
    make event-block unglow for all players
```

### Is block glowing for

プレイヤーに対してブロックが光っているか確認します。

```applescript
%block% is glowing for %player%
%block% (isn't|is not) glowing for %player%
```

```applescript
on left click:
    if event-block is glowing for player:
        send "The selected block is glowing for you" to player
    else:
        send "The selected block isn't glowing for you" to player
```

### Minecraft version

> Requirements: ViaVersion

プレイヤーが遊んでいるバージョン。

```applescript
[the] (mc|minecraft) version of %player%
%player%'s (mc|minecraft) version
```

```applescript
on join:
    send "you are playing minecraft %player's mc version%" to player
```

### Protocol version

> Requirements: ViaVersion

プレイヤーが遊んでいるゲームのプロトコルバージョン。

```applescript
[the] (viaver|vv) [protocol] version of %player%
%player%'s (viaver|vv) [protcol] version
```

```applescript
on join:
    send "you are playing minecraft %player's mc version% [%player's viaver protocol version%]" to player
```
