package com.voidclient.cosmetics;

import java.util.HashMap;
import java.util.Map;

public class CosmeticManager {

    private final Map<String, Boolean> cosmetics = new HashMap<>();

    public CosmeticManager() {
        cosmetics.put("Red Particle Trail", true);
        cosmetics.put("Void Wing Glow", false);
    }

    public Map<String, Boolean> getCosmetics() {
        return cosmetics;
    }

    public void toggle(String key) {
        cosmetics.put(key, !cosmetics.getOrDefault(key, false));
    }
}
