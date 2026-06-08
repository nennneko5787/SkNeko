package net.nennneko5787.skneko.skript.conditions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.skytasul.glowingentities.GlowingBlocks;
import net.nennneko5787.skneko.SkNeko;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

@Name("is block glowing for")
@Description("Check is block glowing for player.")
@Since("0.0.1")
@Example("""
            on right click:
                if event-block is glowing for player:
                    send "%event-block% is glowing for %player%" to player
                else:
                    send "%event-block% isn't glowing for %player%" to player
        """)
public class CondIsBlockGlowingFor extends Condition {
    private static final SkNeko plugin = SkNeko.getPlugin();
    private Expression<Block> blockExpr;
    private Expression<Player> playerExpr;

    @SuppressWarnings("unchecked")
    private static Map<Player, ?> getPlayerMap() {
        GlowingBlocks glowingBlocks = plugin.getGlowingBlocks();
        Field field;
        try {
            field = GlowingBlocks.class.getDeclaredField("glowing");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);

        Map<Player, ?> glowingMap;
        try {
            glowingMap = (Map<Player, ?>) field.get(glowingBlocks);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return glowingMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean check(Event event) {
        Block block = blockExpr.getSingle(event);
        Player player = playerExpr.getSingle(event);

        if (block == null || player == null) {
            return false;
        }

        // 標準ではGlowingBlockからブロックの状態を見ることはできないのでリフレクション
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
                    Objects.requireNonNull(block).getLocation());

        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "SkNeko-CondIsBlockGlowingFor";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions,
                        int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        setNegated(matchedPattern == 2);
        blockExpr = (Expression<Block>) expressions[0];
        playerExpr = (Expression<Player>) expressions[1];
        return true;
    }
}
