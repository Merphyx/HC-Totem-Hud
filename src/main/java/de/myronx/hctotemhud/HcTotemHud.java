package de.myronx.hctotemhud;

import de.myronx.hctotemhud.config.Config;
import de.myronx.hctotemhud.hud.HudRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HcTotemHud implements ClientModInitializer {
    private static final Config CONFIG = Config.getInstance();
    public static final Logger LOGGER = LoggerFactory.getLogger("hctotemhud");

    @Override
    public void onInitializeClient() {
        CONFIG.load();
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> HudRenderer.render(drawContext));
    }
}