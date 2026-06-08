package net.nennneko5787.skneko.skript.expressions;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.nennneko5787.skneko.SkNeko;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

@Name("Minecraft Version")
@Description(
        "Returns the minecraft version of {{types|Player|player}}.")
@Examples("""
            on join:
                send "you are playing minecraft %player's version%" to player
        """)
@Since("0.0.1")
public class ExprPlayerVersion extends SimplePropertyExpression<Player, String> {
    private final SkNeko plugin = SkNeko.getPlugin();

    @Override
    public @Nullable String convert(Player from) {
        int version = plugin.getViaAPI().getPlayerVersion(from);
        return ProtocolVersion.getProtocol(version).getName()
                .replace(".x", "");
    }

    @Override
    protected String getPropertyName() {
        return "minecraft version";
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }
}
