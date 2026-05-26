package com.voidclient.modules;

import com.voidclient.VoidClientMod;
import com.voidclient.modules.armor.ArmorHudModule;
import com.voidclient.modules.cps.CpsCounterModule;
import com.voidclient.modules.keystrokes.KeystrokesModule;
import com.voidclient.modules.motion.MotionBlurModule;
import com.voidclient.modules.potion.PotionHudModule;
import com.voidclient.modules.toggle.ToggleSprintModule;
import com.voidclient.modules.toggle.ZoomModule;
import com.voidclient.modules.fps.FpsCounterModule;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<ModuleBase> modules = new ArrayList<>();

    public void registerDefaultModules() {
        modules.add(new FpsCounterModule());
        modules.add(new CpsCounterModule());
        modules.add(new KeystrokesModule());
        modules.add(new ToggleSprintModule());
        modules.add(new ArmorHudModule());
        modules.add(new PotionHudModule());
        modules.add(new MotionBlurModule());
        modules.add(new ZoomModule());

        if (VoidClientMod.configManager != null) {
            for (ModuleBase module : modules) {
                if (VoidClientMod.configManager.getConfig().moduleStates.getOrDefault(module.getName(), false)) {
                    module.toggle();
                }
            }
        }
    }

    public List<ModuleBase> getModules() {
        return modules;
    }
}
