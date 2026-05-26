package com.voidclient.modules.armor;

import com.voidclient.modules.ModuleBase;
import net.minecraft.item.ItemStack;

public class ArmorHudModule extends ModuleBase {

    public ArmorHudModule() {
        super("Armor HUD", "Exibe a durabilidade das armaduras usando tema vermelho.");
    }

    @Override
    public void onEnable() { }

    @Override
    public void onDisable() { }

    @Override
    public void onTick() { }

    @Override
    public void onRender() {
        if (!enabled || mc.thePlayer == null) {
            return;
        }
        int x = 10;
        int y = 66;
        int index = 0;
        for (ItemStack stack : mc.thePlayer.inventory.armorInventory) {
            if (stack != null) {
                String text = stack.getDisplayName() + " [" + stack.getMaxDamage() - stack.getItemDamage() + "]";
                mc.fontRenderer.drawStringWithShadow(text, x, y + (index * 10), 0xffee6666);
                index++;
            }
        }
    }
}
