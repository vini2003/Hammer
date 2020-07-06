package dev.vini2003.hammer.border.mixin.common;

import dev.vini2003.hammer.border.common.border.CubicWorldBorder;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldBorderListener.WorldBorderSyncer.class)
public class WorldBorderListenerMixin {
	@Shadow
	@Final
	private WorldBorder border;
	
	@Inject(at = @At("HEAD"), method = "onCenterChanged", cancellable = true)
	private void hammer$onCenterChanged(WorldBorder border, double centerX, double centerZ, CallbackInfo ci) {
		var extendedBorder = (CubicWorldBorder) border;
		var thisExtendedBorder = (CubicWorldBorder) this.border;
			
		thisExtendedBorder.setCenter(border.getCenterX(), extendedBorder.getCenterY(), border.getCenterZ());
			
		ci.cancel();
	}
}
