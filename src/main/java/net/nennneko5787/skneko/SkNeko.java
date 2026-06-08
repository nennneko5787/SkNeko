package net.nennneko5787.skneko;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import fr.skytasul.glowingentities.GlowingBlocks;
import fr.skytasul.glowingentities.GlowingEntities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkNeko extends JavaPlugin {
    private static SkNeko instance;
    private GlowingEntities glowingEntities;
    private GlowingBlocks glowingBlocks;
    private ViaAPI<Player> viaAPI;

    public static SkNeko getPlugin() {
        return instance;
    }

    public GlowingEntities getGlowingEntities() {
        return glowingEntities;
    }

    public GlowingBlocks getGlowingBlocks() {
        return glowingBlocks;
    }

    public ViaAPI<Player> getViaAPI() {
        return viaAPI;
    }

    @Override
    public void onEnable() {
        // Initialize GlowingEntities and GlowingBlocks
        glowingEntities = new GlowingEntities(this);
        glowingBlocks = new GlowingBlocks(this);

        boolean viaVersionLoaded = false;
        if (Bukkit.getPluginManager().getPlugin("ViaVersion") !=
                null) {
            viaAPI = Via.getAPI();
            viaVersionLoaded = true;
        }

        AddonLoader loader = new AddonLoader(this);
        if (!loader.canLoadPlugin(viaVersionLoaded)) {
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
        glowingBlocks.disable();
    }
}
