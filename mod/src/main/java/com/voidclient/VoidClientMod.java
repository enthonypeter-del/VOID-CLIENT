package com.voidclient;

import com.google.gson.Gson;
import com.voidclient.client.GuiVoidMainMenu;
import com.voidclient.config.ConfigManager;
import com.voidclient.modules.ModuleManager;
import com.voidclient.networking.HandshakeChannel;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.client.Minecraft;

@Mod(modid = "voidclient", name = "Void Client", version = "1.0.0", clientSideOnly = true)
public class VoidClientMod {

    public static final Gson GSON = new Gson();
    public static ModuleManager moduleManager;
    public static ConfigManager configManager;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configManager = new ConfigManager(event.getModConfigurationDirectory().toPath().resolve("voidclient.json"));
        moduleManager = new ModuleManager();
        moduleManager.registerDefaultModules();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerChannel(new HandshakeChannel(), "VoidClientSync");
        Minecraft.getMinecraft().displayGuiScreen(new GuiVoidMainMenu());
    }
}
