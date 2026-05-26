package com.voidclient.modules.fps;

import com.voidclient.modules.ModuleBase;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;

public class FpsCounterModule extends ModuleBase {

    public FpsCounterModule() {
        super("FPS Counter", "Mostra o FPS atual com estilo Void Client.");
    }

    @Override
    public void onEnable() { }

    @Override
    public void onDisable() { }

    @Override
    public void onTick() { }

    @Override
    public void onRender() {
        if (!enabled || mc.theWorld == null) {
            return;
        }
        int fps = mc.getDebugFPS();
        String text = "FPS: " + fps;
        int x = 10;
        int y = 10;
        mc.fontRenderer.drawStringWithShadow(text, x, y, 0xffee3333);
    }
}
