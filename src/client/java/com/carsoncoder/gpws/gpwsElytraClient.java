package com.carsoncoder.gpws;

import org.slf4j.Logger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import nl.enjarai.cicada.api.conversation.ConversationManager;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.JsonSource;
import nl.enjarai.cicada.api.util.ProperLogger;

public class gpwsElytraClient implements ClientModInitializer, CicadaEntrypoint  {
    public static final Logger LOGGER = ProperLogger.getLogger("gpws-elytra");
	public static gpwsElytraClient instance;

	public static final Identifier MY_SOUND_ID = new Identifier("gpws-elytra:retard");
    public static SoundEvent RETARD = SoundEvent.of(MY_SOUND_ID);

	private String gpwsState = "Loading";

	@Override
	public void onInitializeClient() {
		instance = this;
		HudRenderCallback.EVENT.register(new gpwsHud(this));
		Registry.register(Registries.SOUND_EVENT, gpwsElytraClient.MY_SOUND_ID, RETARD);
	}

	@Override
    public void registerConversations(ConversationManager conversationManager) {
        conversationManager.registerSource(
                JsonSource.fromUrl("https://raw.githubusercontent.com/carson-coder/gpws-elytra/master/fabric/src/main/resources/cicada/gpws-elytra/conversations.json")
                        .or(JsonSource.fromResource("cicada/gpws-elytra/conversations.json")),
                LOGGER::info
        );
    }

	public void tick()
	{
		LOGGER.debug("ticked");
		gpwsState = "";
	}

	private String StateLogic() {
		int StallAngle = -50;

		Entity cam = MinecraftClient.getInstance().getCameraEntity();
		float Pitch = cam.getPitch();
		LOGGER.info(String.valueOf(Pitch));

		if (Pitch < StallAngle) {
			return "Bank Angle";
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