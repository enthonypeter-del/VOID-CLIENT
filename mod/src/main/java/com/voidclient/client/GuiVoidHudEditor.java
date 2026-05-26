package com.voidclient.client;

import com.voidclient.VoidClientMod;
import com.voidclient.config.VoidConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;

public class GuiVoidHudEditor extends GuiScreen {

    private final GuiScreen parent;
    private GuiButton saveButton;
    private GuiButton cancelButton;

    public GuiVoidHudEditor(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        buttonList.clear();
        int centerX = width / 2;
        saveButton = new GuiButton(1, centerX - 100, height - 70, 98, 20, "Salvar HUD");
        cancelButton = new GuiButton(2, centerX + 2, height - 70, 98, 20, "Cancelar");
        buttonList.add(saveButton);
        buttonList.add(cancelButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == saveButton) {
            VoidClientMod.configManager.save();
            mc.displayGuiScreen(parent);
        }
        if (button == cancelButton) {
            mc.displayGuiScreen(parent);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Editor de HUD Void", width / 2, 20, 0xffee3333);
        drawCenteredString(fontRendererObj, "Arraste itens de HUD e personalize seu estilo PvP.", width / 2, 40, 0xffcccccc);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
