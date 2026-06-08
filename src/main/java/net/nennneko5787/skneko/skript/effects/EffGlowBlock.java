package net.nennneko5787.skneko.skript.effects;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Example;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.util.SkriptColor;
import ch.njol.util.Kleenean;
import net.nennneko5787.skneko.SkNeko;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Name("glow a block")
@Description(
        "Glow a {{types|Block|block}} for {{types|Player|players}}.")
@Since("0.0.1")
@Example("""
            on left click:
                make event-block glow for all players
                send "The block is glowing now! :)" to player
        """)
public class EffGlowBlock extends Effect {
    private static final SkNeko plugin = SkNeko.getPlugin();
    private Expression<Block> blockExpr;
    private Expression<SkriptColor> colorExpr;
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

        SkriptColor color;
        if (colorExpr != null) {
            color = colorExpr.getSingle(event);
        } else {
            color = SkriptColor.WHITE;
        }

        players.forEach(player -> {
            try {
                plugin.getGlowingBlocks()
                        .setGlowing(block, player,
                                Objects.requireNonNull(color)
                                        .asChatColor());
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "SkNeko-EffGlowBlock";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions,
                        int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        blockExpr = (Expression<Block>) expressions[0];
        colorExpr = (Expression<SkriptColor>) expressions[1];
        playersExpr = (Expression<Player>) expressions[2];
        return true;
    }
}
