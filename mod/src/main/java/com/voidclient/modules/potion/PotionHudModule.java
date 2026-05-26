package com.voidclient.modules.potion;

import com.voidclient.modules.ModuleBase;
import net.minecraft.potion.PotionEffect;

public class PotionHudModule extends ModuleBase {

    public PotionHudModule() {
        super("Potion HUD", "Mostra efeitos de poção ativos com estilo vermelho.");
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
        int y = 86;
        for (PotionEffect effect : mc.thePlayer.getActivePotionEffects()) {
            String name = effect.getEffectName().replace("potion.", "");
            String text = name + " " + (effect.getDuration() / 20) + "s";
            mc.fontRenderer.drawStringWithShadow(text, 10, y, 0xffff5555);
            y += 10;
        }
    }
}
