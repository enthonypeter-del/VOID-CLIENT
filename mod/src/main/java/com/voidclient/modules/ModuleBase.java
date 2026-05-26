package com.voidclient.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class ModuleBase {

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected boolean enabled;
    protected String name;
    protected String description;

    public ModuleBase(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void onTick();
    public abstract void onRender();

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }
}
