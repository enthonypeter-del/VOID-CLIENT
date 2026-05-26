package com.voidclient.config;

import java.util.HashMap;
import java.util.Map;

public class VoidConfig {

    public String version = "1.8.9";
    public String selectedProfile = "VoidPlayer";
    public boolean showVoidTag = true;
    public boolean hudEditorEnabled = true;
    public Map<String, Boolean> moduleStates = new HashMap<>();
    public HudSettings hudSettings = new HudSettings();

    public static VoidConfig createDefault() {
        VoidConfig config = new VoidConfig();
        config.moduleStates.put("FPS Counter", true);
        config.moduleStates.put("CPS Counter", true);
        config.moduleStates.put("Keystrokes", true);
        config.moduleStates.put("Toggle Sprint", false);
        config.moduleStates.put("Armor HUD", true);
        config.moduleStates.put("Potion HUD", true);
        config.moduleStates.put("Motion Blur", true);
        config.moduleStates.put("Zoom", false);
        return config;
    }

    public static class HudSettings {
        public int fpsX = 8;
        public int fpsY = 8;
        public int cpsX = 8;
        public int cpsY = 28;
        public boolean showBackground = true;
    }
}
