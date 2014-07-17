package com.elmakers.mine.bukkit.effect;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages EffectLib integration
 */
public class EffectLibManager {
    private static EffectManager effectManager;
    private static final Map<String, String> nameMap = new HashMap<String, String>();

    public EffectLibManager() {
    }

    public static EffectLibManager initialize(Plugin plugin) {
        if (effectManager == null) {
            effectManager = new EffectManager(plugin);
        }

        return new EffectLibManager();
    }

    public Effect[] play(ConfigurationSection configuration, EffectPlayer player, Location origin, Location target) {
        Entity sourcePlayer = player.getOriginEntity();
        Entity sourceEntity = player.playAtOrigin ? player.getOriginEntity() : null;
        Entity targetEntity = player.playAtTarget ? player.getTargetEntity() : null;
        origin = player.playAtOrigin ? origin : null;
        target = player.playAtTarget ? target : null;
        if (sourcePlayer != null && sourcePlayer instanceof Player) {
            nameMap.put("$name", ((Player)sourcePlayer).getName());
        } else {
            nameMap.put("$name", "Unknown");
        }

        Effect[] effects = null;
        String effectClass = configuration.getString("class");
        try {
            effects = effectManager.start(effectClass, configuration, origin, target, sourceEntity, targetEntity, nameMap);
        } catch (Throwable ex) {
            Bukkit.getLogger().warning("Error playing effects of class: " + effectClass);
            ex.printStackTrace();
        }
        return effects;
    }

    public void cancel(Effect[] effects) {
        for (Effect effect : effects) {
            try {
                effect.cancel();
            } catch (Throwable ex) {
                Bukkit.getLogger().warning("Error cancelling effects");
                ex.printStackTrace();
            }
        }
    }
}
