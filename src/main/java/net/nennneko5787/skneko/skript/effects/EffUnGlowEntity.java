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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Name("unglow an entity")
@Description(
        "Unglow an {{types|Entity|entity}} for {{types|Player|players}}.")
@Since("0.0.1")
@Example("""
            command /unglow:
                trigger:
                    make player unglow for all players
                    send "You are no longer glowing now" to player
        
            on damage:
                make victim unglowing for attacker
        """)
public class EffUnGlowEntity extends Effect {
    private static final SkNeko plugin = SkNeko.getPlugin();
    private Expression<Entity> entityExpr;
    private Expression<Player> playersExpr;

    @Override
    protected void execute(Event event) {
        Entity entity = entityExpr.getSingle(event);
        if (entity == null) {
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
                plugin.getGlowingEntities()
                        .unsetGlowing(entity, player);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "SkNeko-EffUnGlowEntity";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions,
                        int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        entityExpr = (Expression<Entity>) expressions[0];
        playersExpr = (Expression<Player>) expressions[1];
        return true;
    }
}
