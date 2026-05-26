package com.voidclient.modules.cps;

import com.voidclient.modules.ModuleBase;
import net.minecraft.client.Minecraft;

public class CpsCounterModule extends ModuleBase {

    private int clickCount;
    private long lastReset;

    public CpsCounterModule() {
        super("CPS Counter", "Mostra os cliques por segundo do mouse.");
        lastReset = System.currentTimeMillis();
    }

    @Override
    public void onEnable() {
        clickCount = 0;
    }

    @Override
    public void onDisable() { }

    @Override
    public void onTick() {
        if (!enabled) {
            return;
        }
        if (Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown()) {
            clickCount++;
        }
        if (System.currentTimeMillis() - lastReset > 1000) {
            lastReset = System.currentTimeMillis();
            clickCount = 0;
        }
    }

    @Override
    public void onRender() {
        if (!enabled || mc.theWorld == null) {
            return;
        }
        mc.fontRenderer.drawStringWithShadow("CPS: " + clickCount, 10, 28, 0xffdd4444);
    }
}
