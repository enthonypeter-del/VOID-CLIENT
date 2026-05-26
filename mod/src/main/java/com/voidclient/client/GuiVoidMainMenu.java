package com.voidclient.client;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiVoidMainMenu extends GuiMainMenu {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Void Client", width / 2, 60, 0xffee3333);
        drawCenteredString(fontRendererObj, "PvP 1.8.9 Theme", width / 2, 80, 0xffcccccc);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 2 - 10, 200, 20, "Singleplayer"));
        buttonList.add(new GuiButton(2, width / 2 - 100, height / 2 + 16, 200, 20, "Multiplayer"));
        buttonList.add(new GuiButton(3, width / 2 - 100, height / 2 + 42, 200, 20, "Configurações Void"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 3) {
            mc.displayGuiScreen(new GuiVoidHudEditor(this));
        } else {
            super.actionPerformed(button);
        }
    }

    @Override
    public void drawDefaultBackground() {
        drawGradientRect(0, 0, width, height, 0xff0f0f0f, 0xff1a1a1a);
    }
}
