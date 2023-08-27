package com.carsoncoder.gpws;

import org.joml.Vector3f;
import org.slf4j.Logger;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import nl.enjarai.cicada.api.conversation.ConversationManager;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.JsonSource;
import nl.enjarai.cicada.api.util.ProperLogger;

public class gpwsElytraClient implements ClientModInitializer, CicadaEntrypoint  {
	public static gpwsConfig CONFIG = gpwsConfig.load();
    public static final Logger LOGGER = ProperLogger.getLogger("gpws-elytra");
	public static gpwsElytraClient instance;
	public static final gpwsSounds SOUNDS_MANAGER = new gpwsSounds();

	private String gpwsState = "Loading";

	@Override
	public void onInitializeClient() {
		instance = this;
		SOUNDS_MANAGER.init();
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

	public void tick()
	{
		gpwsState = "";
	}

	private boolean PullUp(float delta) {
		MinecraftClient client = MinecraftClient.getInstance();
		int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        Vec3d cameraDirection = client.cameraEntity.getRotationVec(delta);
        double fov = client.options.getFov().getValue();
        double angleSize = fov/height;
        Vector3f verticalRotationAxis = cameraDirection.toVector3f();
        verticalRotationAxis.cross(new Vector3f(0, 1, 0));
        if(verticalRotationAxis.normalize() == null) {
            return true;//The camera is pointing directly up or down, you'll have to fix this one
        }
        
        Vector3f horizontalRotationAxis = cameraDirection.toVector3f();
        horizontalRotationAxis.cross(verticalRotationAxis);
        horizontalRotationAxis.normalize();
        
        verticalRotationAxis = cameraDirection.toVector3f();
        verticalRotationAxis.cross(horizontalRotationAxis);
		Vec3d direction = gpwsRaycast.map(
			(float) angleSize,
			cameraDirection,
			horizontalRotationAxis,
			verticalRotationAxis,
			0,
			0,
			width,
			height
		);
		HitResult hit = gpwsRaycast.raycastInDirection(client, delta, direction);
		
		switch(hit.getType()) {
			case MISS:
				return false;
			default:
				return true;
		}
	}

	private String StateLogic(float delta) {
		int StallAngle = CONFIG.BankAngleAngle;

		Entity cam = MinecraftClient.getInstance().getCameraEntity();
		BlockPos pos = MinecraftClient.getInstance().player.getBlockPos();
		Chunk chunk = MinecraftClient.getInstance().world.getChunk(pos);
		Heightmap heightmap = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE);

		int YPos = (int)Math.round(MinecraftClient.getInstance().player.getBodyY(1)) - heightmap.get(Math.max(Math.min(pos.getX() - chunk.getPos().getStartX(), 15), 0), Math.max(Math.min(pos.getX() - chunk.getPos().getStartZ(), 15), 0));
		double DownVel = MinecraftClient.getInstance().player.getVelocity().y;
		float Pitch = cam.getPitch();

		// LOGGER.info(String.valueOf(heightmap.get(Math.max(Math.min(pos.getX() - chunk.getPos().getStartX(), 15), 0), Math.max(Math.min(pos.getX() - chunk.getPos().getStartZ(), 15), 0))));

		if (Pitch < StallAngle) {
			return "Bank Angle";
		} else if (PullUp(delta)) {
			return "Pull Up";
		} else if (YPos <= 2500) {
			return String.valueOf(YPos);
		}

		return "";
	}

	public String GetState(float delta) {
		if (gpwsState == "") {
			gpwsState = StateLogic(delta);
		}
		return gpwsState;
	}
	public static boolean isFallFlying() {
        var player = MinecraftClient.getInstance().player;
        return player != null && player.isFallFlying();
    }
}