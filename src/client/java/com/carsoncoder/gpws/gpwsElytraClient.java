package com.carsoncoder.gpws;

import net.fabricmc.api.ClientModInitializer;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;

public class gpwsElytraClient implements ClientModInitializer, CicadaEntrypoint  {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}

	@Override
    public void registerConversations(ConversationManager conversationManager) {
        conversationManager.registerSource(
                JsonSource.fromUrl("https://raw.githubusercontent.com/carsoncoder/gpws-elytra/master/fabric/src/main/resources/cicada/gpws-elytra/conversations.json")
                        .or(JsonSource.fromResource("cicada/gpws-elytra/conversations.json")),
                LOGGER::info
        );
    }
}