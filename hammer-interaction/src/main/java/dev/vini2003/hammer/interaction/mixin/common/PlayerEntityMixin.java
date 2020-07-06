package dev.vini2003.hammer.interaction.mixin.common;

import dev.vini2003.hammer.common.entity.ConfigurablePlayerEntity;
import dev.vini2003.hammer.interaction.common.interaction.InteractionType;
import dev.vini2003.hammer.interaction.common.manager.InteractionRuleManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ConfigurablePlayerEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("RETURN"), method = "isBlockBreakingRestricted", cancellable = true)
	private void hammer$isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
		if (!InteractionRuleManager.allows((PlayerEntity) (Object) this, InteractionType.BLOCK_BREAK, world.getBlockState(pos).getBlock())) {
			cir.setReturnValue(true);
			cir.cancel();
		}
	}
}
