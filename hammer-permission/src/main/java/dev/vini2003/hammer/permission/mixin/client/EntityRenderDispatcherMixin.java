package dev.vini2003.hammer.permission.mixin.client;

import dev.vini2003.hammer.core.api.client.util.InstanceUtil;
import dev.vini2003.hammer.core.api.common.util.RaycastUtil;
import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import dev.vini2003.hammer.permission.api.common.util.RoleUtil;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	@Inject(at = @At("HEAD"), method = "render")
	private <E extends Entity> void hammer$render(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider provider, int light, CallbackInfo ci) {
		var client = InstanceUtil.getClient();
		
		if (entity instanceof PlayerEntity player) {
			if (RoleUtil.hasRoleOutline(player)) {
				var color = RoleManager.getRolePrefixColor(player);
				
				if (color != null) {
					var outlineProvider = client.worldRenderer.bufferBuilders.getOutlineVertexConsumers();
					
					var rgba = color.getRgb();
					
					var r = ((rgba >> 16) & 0xFF);
					var g = ((rgba >> 8) & 0xFF);
					var b = (rgba & 0xFF);
					
					outlineProvider.setColor(r, g, b, 255);
				} else {
					var outlineProvider = client.worldRenderer.bufferBuilders.getOutlineVertexConsumers();
					
					outlineProvider.setColor(255, 255, 255, 255);
				}
			}
		}
	}
}
