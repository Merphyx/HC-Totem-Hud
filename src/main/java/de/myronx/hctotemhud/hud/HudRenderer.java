package de.myronx.hctotemhud.hud;

import de.myronx.hctotemhud.config.Config;
import de.myronx.hctotemhud.utils.TotemCheck;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import org.lwjgl.opengl.GL11;

public class HudRenderer {

    private static final Identifier TOTEM_TEXTURE = new Identifier("minecraft", "textures/item/totem_of_undying.png");
    private static final int TEXTURE_WIDTH = 18;
    private static final int TEXTURE_HEIGHT = 18;
    private static final int TEXTURE_Y_POSITION = 37;
    private static final int TEXT_Y_POSITION = 36;
    private static int lastTotemCount = -1;
    private static float pulsation = 0f;
    private static int screenWidth;
    private static int screenHeight;

    private static final Config CONFIG = Config.getInstance();

    public static void render(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || !CONFIG.toggleMod) return;

        screenWidth = client.getWindow().getScaledWidth();
        screenHeight = client.getWindow().getScaledHeight();

        int totemCharges = TotemCheck.countTotemCharges();

        if (totemCharges > 0) {
            boolean totemDecreased = totemCharges < lastTotemCount || lastTotemCount == -1;

            if (totemCharges <= CONFIG.warningThreshold && totemDecreased) {
                if (CONFIG.warningToggle) {
                    sendWarningMessage(client, totemCharges);
                }
                if (CONFIG.soundToggle) {
                    playWarningSound(client);
                }
            }

            renderHud(drawContext, client, totemCharges);
        }

        lastTotemCount = totemCharges;
    }

    private static void renderHud(DrawContext drawContext, MinecraftClient client, int totemCharges) {
        TextRenderer textRenderer = client.textRenderer;
        String chargesText = String.valueOf(totemCharges);

        int textWidth = textRenderer.getWidth(chargesText);
        int x = screenWidth / 2 - TEXTURE_WIDTH / 2;

        int textureY = screenHeight - TEXTURE_Y_POSITION;

        if (totemCharges <= CONFIG.warningThreshold && CONFIG.vignetteToggle) {
            renderWarningVignette(drawContext);
        }

        if (totemCharges <= CONFIG.warningThreshold && CONFIG.pulsationToggle) {
            drawWithPulsation(drawContext, x, textureY);
        } else {
            drawContext.drawTexture(TOTEM_TEXTURE, x, textureY - TEXTURE_HEIGHT, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }

        int textColor = getTextColor(totemCharges);
        int outlineColor = 0x000000;

        int textY = screenHeight - TEXT_Y_POSITION;

        drawTextWithOutline(drawContext, textRenderer, chargesText, screenWidth / 2 - textWidth / 2, textY - 10, outlineColor, textColor);
    }

    private static void drawWithPulsation(DrawContext drawContext, int x, int textureY) {
        pulsation += 0.1f;
        if (pulsation > Math.PI * 2) {
            pulsation = 0;
        }
        float scale = 1f + 0.1f * (float)Math.sin(pulsation);

        drawContext.getMatrices().push();
        drawContext.getMatrices().translate(x + TEXTURE_WIDTH / 2f, textureY - TEXTURE_HEIGHT / 2f, 0);
        drawContext.getMatrices().scale(scale, scale, 1f);
        drawContext.drawTexture(TOTEM_TEXTURE, -TEXTURE_WIDTH / 2, -TEXTURE_HEIGHT / 2, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        drawContext.getMatrices().pop();
    }

    private static int getTextColor(int totemCharges) {
        if (totemCharges <= 2) {
            return 0xFF0000; // Rot
        } else if (totemCharges <= CONFIG.threshold) {
            return 0xFFA500; // Orange
        } else {
            return 0x80FF20; // Grün
        }
    }

    private static void drawTextWithOutline(DrawContext drawContext, TextRenderer textRenderer, String text, int x, int y, int outlineColor, int textColor) {
        drawContext.drawText(textRenderer, text, x - 1, y, outlineColor, false);
        drawContext.drawText(textRenderer, text, x + 1, y, outlineColor, false);
        drawContext.drawText(textRenderer, text, x, y - 1, outlineColor, false);
        drawContext.drawText(textRenderer, text, x, y + 1, outlineColor, false);

        drawContext.drawText(textRenderer, text, x, y, textColor, false);
    }

    public static void renderWarningVignette(DrawContext context) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 0.2F, 0.2F, 0.4F);

        context.drawTexture(new Identifier("hctotemhud", "textures/vignette.png"), 0, 0, -90, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);

        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void sendWarningMessage(MinecraftClient client, int totemCharges) {
        if (client.player != null) {
            client.player.sendMessage(Text.literal("§c§lWarnung §e⚠ §8» §fDu hast nur noch §e" + totemCharges + "x §fLeben!"), false);
        }
    }

    private static void playWarningSound(MinecraftClient client) {
        client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.BLOCK_NOTE_BLOCK_BIT, 0.5F));
    }
}