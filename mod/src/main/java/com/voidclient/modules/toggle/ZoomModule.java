package com.voidclient.modules.toggle;

import com.voidclient.modules.ModuleBase;

public class ZoomModule extends ModuleBase {

    public ZoomModule() {
        super("Zoom", "Ativa uma visão ampliada enquanto o módulo estiver ativo.");
    }

    @Override
    public void onEnable() {
        if (mc.gameSettings != null) {
            mc.gameSettings.fovSetting = 30.0F;
        }
    }

    @Override
    public void onDisable() {
        if (mc.gameSettings != null) {
            mc.gameSettings.fovSetting = 70.0F;
        }
    }

    @Override
    public void onTick() { }

    @Override
    public void onRender() { }
}
