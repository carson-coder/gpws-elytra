package com.carsoncoder.gpws;


import org.slf4j.Logger;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.random.Random;
import nl.enjarai.cicada.api.util.ProperLogger;

public class gpwsHud implements HudRenderCallback  {
    public static final Logger LOGGER = ProperLogger.getLogger("gpws-elytra");

    TextRenderer renderer;

    int repeatTime = 3;

    String prevstate = "";   
    float lastPlayedSoundTime = 0;

    class Color {
        public int R;
        public int G;
        public int B;

        public Color(int R, int G, int B)
        {
            this.R = R;            
            this.G = G;
            this.B = B;
        }
    }

    private gpwsElytraClient client;
    private Color white = new Color(255, 255, 255);

    public gpwsHud(gpwsElytraClient c) {
        client = c;
    }

    public Text newText(
        String text,
        boolean bold,
        boolean italic,
        boolean strikethrough,
        boolean underline,
        boolean possesed
    ) {
        MutableText txt = Text.literal(text);
        if (bold) {
            txt = txt.formatted(Formatting.BOLD);
        }
        if (italic) {
            txt = txt.formatted(Formatting.ITALIC);
        }
        if (strikethrough) {
            txt = txt.formatted(Formatting.STRIKETHROUGH);
        }
        if (underline) {
            txt = txt.formatted(Formatting.UNDERLINE);
        }
        if (possesed) {
            txt = txt.formatted(Formatting.OBFUSCATED);
        }

        return txt;
    }

    public void renderText(DrawContext ctx, Text text, Color color, int x, int y) {
        MatrixStack stack = ctx.getMatrices();
        stack.push();
        stack.scale(2, 2, 2);
        // renderer.draw(text, x, y, color.R * 0x010000 + color.G * 0x000100 + color.B * 0x000001, true, RenderSystem.getProjectionMatrix() /* ctx.getMatrices() */, ctx.getVertexConsumers(), TextLayerType.NORMAL, 0x000000, 0);
        ctx.drawText(renderer, text, x, y, color.R * 0x010000 + color.G * 0x000100 + color.B * 0x000001, true);
        stack.pop();
    }

    private void PlaySound(SoundEvent sound) {
        MinecraftClient.getInstance().getSoundManager().play(new EntityTrackingSoundInstance(
            gpwsElytraClient.RETARD,
            SoundCategory.BLOCKS,
            1f,
            1f,
            MinecraftClient.getInstance().player,
            Random.create().nextLong()));
        // if (MinecraftClient.getInstance().player == null) {return;}
        //     MinecraftClient.getInstance().world.playSound(
        //     MinecraftClient.getInstance().player, // Player - if non-null, will play sound for every nearby player *except* the specified player
        //     MinecraftClient.getInstance().player.getBlockPos(), // The position of where the sound will come from
        //     SoundEvents.BLOCK_FENCE_GATE_OPEN, // The sound that will play
        //     SoundCategory.BLOCKS, // This determines which of the volume sliders affect this sound
        //     1f, //Volume multiplier, 1 is normal, 0.5 is half volume, etc
        //     1f // Pitch multiplier, 1 is normal, 0.5 is half pitch, etc
		// );
    }

	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta) {
        if (!client.isFallFlying()) {
            return;
        }

        while (renderer == null)
        {
            renderer = MinecraftClient.getInstance().textRenderer;
        }

        String state = client.GetState();

        if (state != prevstate) {
            PlaySound(gpwsElytraClient.RETARD);
        }

        // Render Bold Underlined Hello In the center of the screen
        Text gpwsText = newText(
            client.GetState(),
            false,
            false, 
            false, 
            false, 
            false
        );

        renderText(
            drawContext,
            gpwsText,
            white,
            (MinecraftClient.getInstance().getWindow().getWidth() / 4 - renderer.getWidth(gpwsText)) / 2,
            10
        );

        prevstate = state;
	}
}