package de.myronx.hctotemhud.config;

import net.fabricmc.loader.api.FabricLoader;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.NoticeScreen;
import net.minecraft.text.Text;

public class DefaultConfig implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config2")) {
            return ModMenuConfig::create;
        }

        return parent -> new NoticeScreen(() -> MinecraftClient.getInstance().setScreen(parent), Text.of("§nInfo"), Text.of("Die Mod \"HC-Totem Hud\" benötigt Cloth Config, um die Konfiguration anzeigen zu können."));
    }
}
