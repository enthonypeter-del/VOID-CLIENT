package com.voidclient.modules.keystrokes;

import com.voidclient.modules.ModuleBase;
import net.minecraft.client.Minecraft;

public class KeystrokesModule extends ModuleBase {

    public KeystrokesModule() {
        super("Keystrokes", "Mostra as teclas de movimento e clique em HUD.");
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
        int x = 10;
        int y = 46;
        String up = Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown() ? "W" : "w";
        String left = Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown() ? "A" : "a";
        String down = Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown() ? "S" : "s";
        String right = Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown() ? "D" : "d";
        mc.fontRenderer.drawStringWithShadow("Keys: " + up + " " + left + " " + down + " " + right, x, y, 0xffff5555);
    }
}
