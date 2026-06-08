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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Name("glow an entity")
@Description(
        "Glow an {{types|Entity|entity}} for {{types|Player|players}}.")
@Since("0.0.1")
@Example("""
            command /glow:
                trigger:
                    make player glow for all players
                    send "You are glowing now! :)" to player
        
            on damage:
                make attacker glowing with color red for victim
        """)
public class EffGlowEntity extends Effect {
    private static final SkNeko plugin = SkNeko.getPlugin();
    private Expression<Entity> entityExpr;
    private Expression<SkriptColor> colorExpr;
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

        SkriptColor color;
        if (colorExpr != null) {
            color = colorExpr.getSingle(event);
        } else {
            color = SkriptColor.WHITE;
        }

        players.forEach(player -> {
            try {
                plugin.getGlowingEntities()
                        .setGlowing(entity, player,
                                Objects.requireNonNull(color)
                                        .asChatColor());
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "SkNeko-EffGlowEntity";
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions,
                        int matchedPattern, Kleenean isDelayed,
                        SkriptParser.ParseResult parseResult) {
        entityExpr = (Expression<Entity>) expressions[0];
        colorExpr = (Expression<SkriptColor>) expressions[1];
        playersExpr = (Expression<Player>) expressions[2];
        return true;
    }
}
