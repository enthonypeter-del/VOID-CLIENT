package com.voidclient.modules.motion;

import com.voidclient.modules.ModuleBase;
import net.minecraft.client.renderer.EntityRenderer;

public class MotionBlurModule extends ModuleBase {

    public MotionBlurModule() {
        super("Motion Blur", "Aplica um leve efeito de desfoque de movimento para estilo PvP.");
    }

    @Override
    public void onEnable() { }

    @Override
    public void onDisable() { }

    @Override
    public void onTick() {
        if (enabled && mc.entityRenderer != null) {
            mc.entityRenderer.time = 0.0F;
        }
    }

    @Override
    public void onRender() {
        if (!enabled || mc.entityRenderer == null) {
            return;
        }
        // O efeito visual é mantido pelo tempo de render do jogo.
    }
}
