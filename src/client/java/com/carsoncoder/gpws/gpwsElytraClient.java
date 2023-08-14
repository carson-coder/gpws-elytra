package com.carsoncoder.gpws;

import net.fabricmc.api.ClientModInitializer;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.conversation.ConversationManager;
import nl.enjarai.cicada.api.util.JsonSource;
import nl.enjarai.cicada.api.util.ProperLogger;
import org.slf4j.Logger;

public class gpwsElytraClient implements ClientModInitializer, CicadaEntrypoint  {
    public static final Logger LOGGER = ProperLogger.getLogger("gpws-elytra");

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
	}

	@Override
    public void registerConversations(ConversationManager conversationManager) {
        conversationManager.registerSource(
                JsonSource.fromUrl("https://raw.githubusercontent.com/carson-coder/gpws-elytra/master/fabric/src/main/resources/cicada/gpws-elytra/conversations.json")
                        .or(JsonSource.fromResource("cicada/gpws-elytra/conversations.json")),
                LOGGER::info
        );
    }
}