package dev.vini2003.hammer.border.mixin.common;

import com.mojang.serialization.DynamicLike;
import dev.vini2003.hammer.border.common.border.CubicWorldBorder;
import dev.vini2003.hammer.border.common.border.CubicWorldBorderProperties;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldBorder.Properties.class)
public class WorldBorderPropertiesMixin implements CubicWorldBorderProperties {
	private double centerY;
	
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/world/border/WorldBorder;)V")
	private void hammer$init(WorldBorder worldBorder, CallbackInfo ci) {
		var cubicWorldBorder = (CubicWorldBorder) worldBorder;
		
		centerY = cubicWorldBorder.getCenterY();
	}
	
	@Inject(at = @At("RETURN"), method = "fromDynamic", cancellable = true)
	private static void hammer$fromDynamic(DynamicLike<?> dynamic, WorldBorder.Properties properties, CallbackInfoReturnable<WorldBorder.Properties> cir) {
		var cubicProperties = (CubicWorldBorderProperties) properties;
		
		var returnProperties = cir.getReturnValue();
		
		var cubicReturnProperties = (CubicWorldBorderProperties) returnProperties;
		cubicReturnProperties.setCenterY(dynamic.get("BorderCenterY").asDouble(cubicProperties.getCenterY()));
		
		cir.setReturnValue((WorldBorder.Properties) cubicReturnProperties);
	}
	
	@Inject(at = @At("RETURN"), method = "writeNbt")
	private void hammer$writeNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putDouble("BorderCenterY", centerY);
	}
	
	@Override
	public double getCenterY() {
		return centerY;
	}
	
	@Override
	public void setCenterY(double centerY) {
		this.centerY = centerY;
	}
}
