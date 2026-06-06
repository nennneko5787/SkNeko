package net.nennneko5787.skneko;

import fr.skytasul.glowingentities.GlowingEntities;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkNeko extends JavaPlugin {
    private static SkNeko instance;
    private GlowingEntities glowingEntities;

    public static SkNeko getPlugin() {
        return instance;
    }

    public GlowingEntities getGlowingEntities() {
        return glowingEntities;
    }

    @Override
    public void onEnable() {
        // Initialize GlowingEntities
        glowingEntities = new GlowingEntities(this);

        AddonLoader loader = new AddonLoader(this);
        if (!loader.canLoadPlugin()) {
            getLogger().severe(
                    "SkNeko initialization failed. SkNeko will be shutdown.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
    }

    @Override
    public void onDisable() {
        glowingEntities.disable();
    }
}
