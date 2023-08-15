package com.carsoncoder.gpws;

import org.joml.Matrix4f;
import org.slf4j.Logger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import nl.enjarai.cicada.api.conversation.ConversationManager;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.JsonSource;
import nl.enjarai.cicada.api.util.ProperLogger;

public class gpwsElytraClient implements ClientModInitializer, CicadaEntrypoint, HudRenderCallback  {
    public static final Logger LOGGER = ProperLogger.getLogger("gpws-elytra");
	public static gpwsElytraClient instance;

	private String gpwsState = "Loading";

	@Override
	public void onInitializeClient() {
		instance = this;
		HudRenderCallback.EVENT.register(new gpwsHud(this));
	}

	@Override
    public void registerConversations(ConversationManager conversationManager) {
        conversationManager.registerSource(
                JsonSource.fromUrl("https://raw.githubusercontent.com/carson-coder/gpws-elytra/master/fabric/src/main/resources/cicada/gpws-elytra/conversations.json")
                        .or(JsonSource.fromResource("cicada/gpws-elytra/conversations.json")),
                LOGGER::info
        );
    }

	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta) {
		Matrix4f matrix = MinecraftClient.getInstance().gameRenderer.getBasicProjectionMatrix(MinecraftClient.getInstance().options.getFov().getValue());
	}

	public void tick()
	{
		LOGGER.debug("ticked");
		gpwsState = "";
	}

	private String StateLogic() {
		int StallAngle = -70;

		Entity cam = MinecraftClient.getInstance().getCameraEntity();
		float Pitch = cam.getPitch();
		LOGGER.info(String.valueOf(Pitch));

		if (Pitch < StallAngle) {
			return "Stall";
		}

		return "";
	}

	public String GetState() {
		if (gpwsState == "") {
			gpwsState = StateLogic();
		}
		return gpwsState;
	}
	public static boolean isFallFlying() {
        var player = MinecraftClient.getInstance().player;
        return player != null && player.isFallFlying();
    }
}