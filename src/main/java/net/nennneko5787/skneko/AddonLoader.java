package net.nennneko5787.skneko;

import ch.njol.skript.Skript;
import ch.njol.skript.util.Version;
import net.nennneko5787.skneko.skript.conditions.CondGlowing;
import net.nennneko5787.skneko.skript.elements.EffGlowEntity;
import net.nennneko5787.skneko.skript.elements.EffUnGlowEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.skriptlang.skript.addon.SkriptAddon;
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

    boolean canLoadPlugin() {
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

        registerElements();
        return true;
    }

    private void registerElements() {
        SkriptAddon addon = Skript.instance()
                .registerAddon(SkNeko.class, "SkNeko");
        SyntaxRegistry registry = addon.syntaxRegistry();

        // Condition
        // is entity glowing for
        registry.register(SyntaxRegistry.CONDITION,
                SyntaxInfo.builder(CondGlowing.class)
                        .priority(Priority.base()).addPatterns(
                                "%entity% is glowing for %player%",
                                "%entity% (isn't|is not) glowing for %player%")
                        .build());
        // Effect
        // Glow an entity
        registry.register(SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffGlowEntity.class)
                        .priority(Priority.base()).addPatterns(
                                "make %entity% glow[ing] [[with color] %-color%] for %players%")
                        .build());
        // Unglow an entity
        registry.register(SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffUnGlowEntity.class)
                        .priority(Priority.base()).addPatterns(
                                "make %entity% unglow[ing] for %players%")
                        .build());
    }
}
