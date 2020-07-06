package dev.vini2003.hammer.border.mixin.common;

import dev.vini2003.hammer.border.common.border.CubicWorldBorder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
	@Shadow public abstract ServerWorld getWorld();
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorder;getDistanceInsideBorder(DD)D"), method = "moveToSpawn")
	private double hammer$moveToSpawn$getDistanceInsideBorder(WorldBorder instance, double x, double z) {
		return ((CubicWorldBorder) instance).getDistanceInsideBorder(getWorld().getSpawnPos().getX(), getWorld().getSpawnPos().getY(), getWorld().getSpawnPos().getZ());
	}
}
