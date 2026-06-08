package net.nennneko5787.skneko.skript.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skytasul.glowingentities.GlowingEntities;
import net.nennneko5787.skneko.SkNeko;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

@Name("is entity glowing for")
@Description(
        "Check is {{types|Entity|entity}} glowing for {{types|Player|player}}.")
@Since("0.0.1")
@Example("""
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
        """)
public class CondIsEntityGlowingFor extends Condition {
    private static final SkNeko plugin = SkNeko.getPlugin();
    private Expression<Entity> entityExpr;
    private Expression<Player> playerExpr;

    @SuppressWarnings("unchecked")
    private static Map<Player, ?> getPlayerMap() {
        GlowingEntities glowingEntities = plugin.getGlowingEntities();
        Field field;
        try {
            field = GlowingEntities.class.getDeclaredField("glowing");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);

        Map<Player, ?> glowingMap;
        try {
            glowingMap = (Map<Player, ?>) field.get(glowingEntities);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return glowingMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean check(Event event) {
        Entity entity = entityExpr.getSingle(event);
        Player player = playerExpr.getSingle(event);

        if (entity == null || player == null) {
            return false;
        }

        // 標準ではGlowingEntitiesからエンティティの状態を見ることはできないのでリフレクション
        // こんなことして本当にいいのか... (^^;
        Map<Player, ?> glowingMap =
                getPlayerMap();
        Object playerData = glowingMap.get(player);

        if (playerData == null) {
            return false;
        }

        try {
            Field glowingDatasField =
                    playerData.getClass()
                            .getDeclaredField("glowingDatas");

            glowingDatasField.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<Integer, ?> glowingDatas =
                    (Map<Integer, ?>) glowingDatasField.get(
                            playerData);

            return glowingDatas.containsKey(
                    Objects.requireNonNull(entity).getEntityId());

        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "SkNeko-CondIsEntityGlowingFor";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions,
                        int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        setNegated(matchedPattern == 2);
        entityExpr = (Expression<Entity>) expressions[0];
        playerExpr = (Expression<Player>) expressions[1];
        return true;
    }
}
