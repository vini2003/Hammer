package dev.vini2003.hammer.preset.mixin.common;

import dev.vini2003.hammer.core.HC;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldChunk.DirectBlockEntityTickInvoker.class)
public class WorldChunk$DirectBlockEntityTickInvokerMixin {
	@Redirect(require = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntityTicker;tick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;)V"), method = "tick")
	private <T extends BlockEntity> void hammer$tick$tick(BlockEntityTicker instance, World world, BlockPos blockPos, BlockState blockState, T blockEntity) {
		try {
			instance.tick(world, blockPos, blockState, blockEntity);
		} catch (Exception e) {
			HC.LOGGER.error("A block entity failed to tick!");
			e.printStackTrace();
			
			world.getPlayers().forEach(player -> {
				player.sendMessage(Text.literal("A block entity failed to tick!").formatted(Formatting.RED, Formatting.BOLD), false);
				player.sendMessage(Text.literal(Registries.BLOCK_ENTITY_TYPE.getId(blockEntity.getType()) + " at " + blockEntity.getPos().toString()).formatted(Formatting.RED), false);
			});
			
			world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
		}
	}
}
