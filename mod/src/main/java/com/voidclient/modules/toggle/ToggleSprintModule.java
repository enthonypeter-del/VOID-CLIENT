package com.voidclient.modules.toggle;

import com.voidclient.modules.ModuleBase;
import net.minecraft.client.Minecraft;

public class ToggleSprintModule extends ModuleBase {

    public ToggleSprintModule() {
        super("Toggle Sprint", "Ativa/desativa sprint automaticamente.");
    }

    @Override
    public void onEnable() { }

    @Override
    public void onDisable() { }

    @Override
    public void onTick() {
        if (!enabled || mc.thePlayer == null) {
            return;
        }
        mc.thePlayer.setSprinting(mc.thePlayer.moveForward > 0.0F);
    }

    @Override
    public void onRender() { }
}
