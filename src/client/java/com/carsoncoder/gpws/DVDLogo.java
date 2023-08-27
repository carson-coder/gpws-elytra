// Not used in the mod
// Just a GUI Test

package com.carsoncoder.gpws;

import org.slf4j.Logger;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import nl.enjarai.cicada.api.util.ProperLogger;

public class DVDLogo implements HudRenderCallback  {
    public static final Logger LOGGER = ProperLogger.getLogger("gpws-elytra");

    TextRenderer renderer;

    float x = 0;
    int sx;
    float mul;

    float y = 0;
    int sy;
    float ymul;

    String text = "MINECRAFT";

    int tx;
    int ty;

    float speed = 10f;

    int xdir = 1;
    int ydir = 1;

	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta)
    {

        while (renderer == null)
        {
            renderer = MinecraftClient.getInstance().textRenderer;
        }

        int tx = renderer.getWidth(text);
        int ty = renderer.getWrappedLinesHeight(text, sx);

        sx = MinecraftClient.getInstance().getWindow().getWidth() - tx * 2;
        mul = 1f/ Integer.toUnsignedLong(sx);

        sy = MinecraftClient.getInstance().getWindow().getHeight() - ty * 2;
        ymul = 1f/ Integer.toUnsignedLong(sy);

        drawContext.fill(1, 1, sx + tx, sy + ty, 100, 100);
        drawContext.drawText(renderer, text, Math.round(x*sx), Math.round(y*sy), 0xFF0000, true);
        x += mul * speed * xdir * tickDelta;
        y += ymul * speed * ydir * tickDelta;
        
        if (x > 0.5f) {xdir = -1;}
        if (y > 0.5f) {ydir = -1;}

        if (x < 0f) {xdir = 1;}
        if (y < 0f) {ydir = 1;}
	}
}