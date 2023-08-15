package com.carsoncoder.gpws.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.carsoncoder.gpws.gpwsElytraClient;

import net.minecraft.client.MinecraftClient;

@Mixin(MinecraftClient.class)
public class gpwsElytraClientMixin {
	// @Inject(at = @At("HEAD"), method = "doItemUse")
	// private void doItemUse(CallbackInfo info) {
	// }
	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		gpwsElytraClient.instance.tick();
	}
}