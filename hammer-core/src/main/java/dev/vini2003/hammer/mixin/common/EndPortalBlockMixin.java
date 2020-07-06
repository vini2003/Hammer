package dev.vini2003.hammer.mixin.common;

import dev.vini2003.hammer.registry.common.HConfig;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;canUsePortals()Z"), method = "onEntityCollision")
	private boolean hammer$onEntityCollision$canUsePortal(Entity instance) {
		return instance.canUsePortals() && HConfig.ENABLE_NETHER_PORTAL;
	}
}
