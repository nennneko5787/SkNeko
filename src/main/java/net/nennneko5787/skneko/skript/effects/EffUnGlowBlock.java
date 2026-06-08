package net.nennneko5787.skneko.skript.effects;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import net.nennneko5787.skneko.SkNeko;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("unglow a block")
@Description(
        "Unglow a {{types|Block|block}} for {{types|Player|players}}.")
@Since("0.0.1")
@Example("""
            on right click:
                make event-block unglow for all players
                send "The block is no longer glowing now" to player
        """)
public class EffUnGlowBlock extends Effect {
    private static final SkNeko plugin = SkNeko.getPlugin();
    private Expression<Block> blockExpr;
    private Expression<Player> playersExpr;

    @Override
    protected void execute(Event event) {
        Block block = blockExpr.getSingle(event);
        if (block == null) {
            return;
        }

        List<Player> players = new ArrayList<>();
        if (playersExpr.canBeSingle()) {
            if (playersExpr.getSingle(event) == null) {
                return;
            }
            players.add(playersExpr.getSingle(event));
        } else {
            players.addAll(List.of(playersExpr.getArray(event)));
        }

        players.forEach(player -> {
            try {
                plugin.getGlowingBlocks()
                        .unsetGlowing(block, player);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "SkNeko-EffUnGlowBlock";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions,
                        int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        blockExpr = (Expression<Block>) expressions[0];
        playersExpr = (Expression<Player>) expressions[1];
        return true;
    }
}
