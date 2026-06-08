package net.nennneko5787.skneko;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Version;
import net.nennneko5787.skneko.skript.conditions.CondIsBlockGlowingFor;
import net.nennneko5787.skneko.skript.conditions.CondIsEntityGlowingFor;
import net.nennneko5787.skneko.skript.effects.*;
import net.nennneko5787.skneko.skript.expressions.ExprPlayerVersion;
import net.nennneko5787.skneko.skript.expressions.ExprProtocolVersion;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.registration.DefaultSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;
import org.skriptlang.skript.util.Priority;

public class AddonLoader {
    private final SkNeko plugin;
    private final Plugin skriptPlugin;

    public AddonLoader(SkNeko plugin) {
        this.plugin = plugin;
        PluginManager pluginManager =
                plugin.getServer().getPluginManager();
        this.skriptPlugin = pluginManager.getPlugin("Skript");
    }

    boolean canLoadPlugin(boolean viaVersionLoaded) {
        if (skriptPlugin == null) {
            plugin.getLogger()
                    .severe("Skript was not found. please install Skript");
            return false;
        }
        if (!skriptPlugin.isEnabled()) {
            plugin.getLogger().severe("Skript was not enabled.");
            return false;
        }
        Version skriptVersion = Skript.getVersion();
        if (skriptVersion.isSmallerThan(new Version(2, 14, 999))) {
            plugin.getLogger()
                    .severe("Skript outdated. please update the Skript plugin to 2.15.0 or more.");
            return false;
        }
        if (!Skript.isAcceptRegistrations()) {
            plugin.getLogger()
                    .severe("Skript is no longer accepting registrations.");
        }
        if (!viaVersionLoaded) {
            plugin.getLogger().warning(
                    "ViaVersion was not found. Some elements are disabled.");
        }

        registerElements(viaVersionLoaded);
        return true;
    }

    private void registerElements(boolean viaVersionLoaded) {
        SkriptAddon addon = Skript.instance()
                .registerAddon(SkNeko.class, "SkNeko");
        SyntaxRegistry registry = addon.syntaxRegistry();

        // ViaVersion
        if (viaVersionLoaded) {
            // minecraft version
            registry.register(SyntaxRegistry.EXPRESSION,
                    DefaultSyntaxInfos.Expression.builder(
                                    ExprPlayerVersion.class, String.class)
                            .priority(Priority.base()).addPatterns(
                                    "[the] (mc|minecraft) version of %player%",
                                    "%player%'[s] (mc|minecraft) version")
                            .build());
            // protocol version
            registry.register(SyntaxRegistry.EXPRESSION,
                    DefaultSyntaxInfos.Expression.builder(
                                    ExprProtocolVersion.class,
                                    Integer.class)
                            .priority(Priority.base()).addPatterns(
                                    "[the] (viaver|vv) [protocol] version of %player%",
                                    "%player%'[s] (viaver|vv) [protocol] version")
                            .build());
        }

        // Condition
        // is entity glowing for
        registry.register(SyntaxRegistry.CONDITION,
                SyntaxInfo.builder(CondIsEntityGlowingFor.class)
                        .priority(Priority.base()).addPatterns(
                                "%entity% is glowing for %player%",
                                "%entity% (isn't|is not) glowing for %player%")
                        .build());
        // is block glowing for
        registry.register(SyntaxRegistry.CONDITION,
                SyntaxInfo.builder(CondIsBlockGlowingFor.class)
                        .priority(Priority.base()).addPatterns(
                                "%block% is glowing for %player%",
                                "%block% (isn't|is not) glowing for %player%")
                        .build());

        // Effect
        // Glow an entity
        registry.register(SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffGlowEntity.class)
                        .priority(Priority.base()).addPatterns(
                                "make %entity% glow[ing] [[with color] %-color%] for %players%")
                        .build());
        // Glow a block
        registry.register(SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffGlowBlock.class)
                        .priority(Priority.base()).addPatterns(
                                "make %block% glow[ing] [[with color] %-color%] for %players%")
                        .build());
        // Unglow an entity
        registry.register(SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffUnGlowEntity.class)
                        .priority(Priority.base()).addPatterns(
                                "make %entity% unglow[ing] for %players%")
                        .build());
        // Unglow a block
        registry.register(SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffUnGlowBlock.class)
                        .priority(Priority.base()).addPatterns(
                                "make %block% unglow[ing] for %players%")
                        .build());
        // Do nothing
        registry.register(SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffDoNothing.class)
                        .priority(Priority.base()).addPatterns(
                                "[do] nothing")
                        .build());
    }
}
