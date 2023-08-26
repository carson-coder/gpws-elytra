package com.carsoncoder.gpws;

import org.joml.Vector3f;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class gpwsRaycast {
    public static Vec3d map(float anglePerPixel, Vec3d center, Vector3f horizontalRotationAxis,
        Vector3f verticalRotationAxis, int x, int y, int width, int height) {
    
        final Vector3f temp2 = center.toVector3f();
        return new Vec3d(temp2);
    }

    public static HitResult raycastInDirection(MinecraftClient client, float tickDelta, Vec3d direction) {
        Vec3d cameraDirection = client.cameraEntity.getRotationVec(tickDelta);
        Vector3f verticalRotationAxis = cameraDirection.toVector3f();
        verticalRotationAxis.cross(new Vector3f(0, 1, 0));
        if(verticalRotationAxis.normalize() == null) {
            return null;//The camera is pointing directly up or down, you'll have to fix this one
        }
        
        Vector3f horizontalRotationAxis = cameraDirection.toVector3f();
        horizontalRotationAxis.cross(verticalRotationAxis);
        horizontalRotationAxis.normalize();
        
        verticalRotationAxis = cameraDirection.toVector3f();
        verticalRotationAxis.cross(horizontalRotationAxis);

        Entity entity = client.getCameraEntity();
        if (entity == null || client.world == null) {
            return null;
        }
    
        double reachDistance = gpwsElytraClient.CONFIG.PullUpRange; //Change this to extend the reach
        HitResult target = raycast(entity, reachDistance, tickDelta, false, direction);
        boolean tooFar = false;
        double extendedReach = reachDistance;
    
        Vec3d cameraPos = entity.getCameraPosVec(tickDelta);
    
        extendedReach = extendedReach * extendedReach;
        if (target != null) {
            extendedReach = target.getPos().squaredDistanceTo(cameraPos);
        }
    
        Vec3d vec3d3 = cameraPos.add(direction.multiply(reachDistance));
        Box box = entity
                .getBoundingBox()
                .stretch(entity.getRotationVec(1.0F).multiply(reachDistance))
                .expand(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(
                entity,
                cameraPos,
                vec3d3,
                box,
                (entityx) -> !entityx.isSpectator(),
                extendedReach
        );
    
        if (entityHitResult == null) {
            return target;
        }
    
        Entity entity2 = entityHitResult.getEntity();
        Vec3d vec3d4 = entityHitResult.getPos();
        double g = cameraPos.squaredDistanceTo(vec3d4);
        if (tooFar && g > 9.0D) {
            return null;
        } else if (g < extendedReach || target == null) {
            target = entityHitResult;
            if (entity2 instanceof LivingEntity || entity2 instanceof ItemFrameEntity) {
                client.targetedEntity = entity2;
            }
        }
    
        return target;
    }
    
    private static HitResult raycast(
            Entity entity,
            double maxDistance,
            float tickDelta,
            boolean includeFluids,
            Vec3d direction
    ) {
        Vec3d end = entity.getCameraPosVec(tickDelta).add(direction.multiply(maxDistance));
        return entity.getWorld().raycast(new RaycastContext(
                entity.getCameraPosVec(tickDelta),
                end,
                RaycastContext.ShapeType.OUTLINE,
                includeFluids ? RaycastContext.FluidHandling.ANY : RaycastContext.FluidHandling.NONE,
                entity
        ));
    }
}
