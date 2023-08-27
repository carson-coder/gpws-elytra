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
import nl.enjarai.cicada.api.util.ProperLogger;

public class gpwsElytraClient implements ClientModInitializer  {
	public static gpwsConfig CONFIG = gpwsConfig.load();
    public static final Logger LOGGER = ProperLogger.getLogger("gpws-elytra");
	public static gpwsElytraClient instance;
	public static final gpwsSounds SOUNDS_MANAGER = new gpwsSounds();

    public static final class State {
        int Y;
        boolean Pull;
        boolean Bank;
        String State;

        public State(int y, boolean pull, boolean bank, String state) {
            this.Y = y;
            this.Pull = pull;
            this.Bank = bank;
            this.State = state;
        }

        @Override
        public int hashCode() {
            int result = Y;
            result = 31 * result + (Pull? 1 : 0);
            result = 31 * result + (Bank? 1 : 0);
            result = 31 * result + State.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass()!= o.getClass()) return false;

            State state = (State) o;

            if (Y!= state.Y) return false;
            if (Pull!= state.Pull) return false;
            if (Bank!= state.Bank) return false;
            return State.equals(state.State);
        }
    }

	private State gpwsState = null;

	@Override
	public void onInitializeClient() {
		instance = this;
		SOUNDS_MANAGER.init();
		HudRenderCallback.EVENT.register(new gpwsHud(this));
    }

	public void tick()
	{
		gpwsState = null;
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

	private State StateLogic(float delta) {
		int StallAngle = CONFIG.BankAngleAngle;

		Entity cam = MinecraftClient.getInstance().getCameraEntity();
		BlockPos pos = MinecraftClient.getInstance().player.getBlockPos();
		Chunk chunk = MinecraftClient.getInstance().world.getChunk(pos);
		Heightmap heightmap = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE);

		int YPos = (int)Math.round(MinecraftClient.getInstance().player.getBodyY(1)) - heightmap.get(Math.max(Math.min(pos.getX() - chunk.getPos().getStartX(), 15), 0), Math.max(Math.min(pos.getX() - chunk.getPos().getStartZ(), 15), 0));
		YPos = Math.round(YPos / CONFIG.Round) * CONFIG.Round;
        double DownVel = MinecraftClient.getInstance().player.getVelocity().y;
		float Pitch = cam.getPitch();

		// LOGGER.info(String.valueOf(heightmap.get(Math.max(Math.min(pos.getX() - chunk.getPos().getStartX(), 15), 0), Math.max(Math.min(pos.getX() - chunk.getPos().getStartZ(), 15), 0))));

		if (Pitch < StallAngle) {
			return new State(0, false, true, "Bank Angle");
		} else if (PullUp(delta)) {
			return new State(0, true, false, "Pull Up");
		} else if (YPos <= 2500) {
			return new State(YPos, false, false, String.valueOf(YPos));
		}

		return null;
	}

	public State GetState(float delta) {
		if (gpwsState == null) {
			gpwsState = StateLogic(delta);
		}
		return gpwsState;
	}
	public static boolean isFallFlying() {
        var player = MinecraftClient.getInstance().player;
        return player != null && player.isFallFlying();
    }
}