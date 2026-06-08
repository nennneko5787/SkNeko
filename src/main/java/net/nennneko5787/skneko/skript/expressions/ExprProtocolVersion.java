package net.nennneko5787.skneko.skript.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import net.nennneko5787.skneko.SkNeko;
import org.bukkit.entity.Player;

@Name("Protocol Version")
@Description(
        "Returns the protocol version of {{types|Player|player}}.")
@Examples("""
            on join:
                send "you are playing minecraft %player's mc version% [%player's viaver protocol version%]" to player
        """)
@Since("0.0.1")
public class ExprProtocolVersion extends SimplePropertyExpression<Player, Integer> {
    private final SkNeko plugin = SkNeko.getPlugin();

    @Override
    public Integer convert(Player from) {
        return plugin.getViaAPI().getPlayerVersion(from);
    }

    @Override
    protected String getPropertyName() {
        return "protocol version";
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return int.class;
    }
}
