package dev.vini2003.hammer.stage.mixin.common;

import net.minecraft.util.registry.BuiltinRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.vini2003.hammer.stage.registry.common.HSDimensions.VOID_DIMENSION_TYPE;
import static dev.vini2003.hammer.stage.registry.common.HSDimensions.VOID_DIMENSION_TYPE_KEY;

@Mixin(BuiltinRegistries.class)
public class BuiltinRegistriesMixin {
	@Inject(at = @At("TAIL"), method = "<clinit>")
	private static void hammer$clinit(CallbackInfo ci) {
		BuiltinRegistries.add(BuiltinRegistries.DIMENSION_TYPE, VOID_DIMENSION_TYPE_KEY, VOID_DIMENSION_TYPE);
	}
}
