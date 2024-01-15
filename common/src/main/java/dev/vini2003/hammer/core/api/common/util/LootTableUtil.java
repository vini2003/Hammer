package dev.vini2003.hammer.core.api.common.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LootTableUtil {
	public static class LootTableBuilder {
		private final LootContext.Builder contextBuilder;
		
		public LootTableBuilder(World world) {
			contextBuilder = new LootContext.Builder((ServerWorld) world);
		}
		
		public LootTableBuilder withThisEntity(Entity entity) {
			contextBuilder.parameter(LootContextParameters.THIS_ENTITY, entity);
			return this;
		}
		
		public LootTableBuilder withLastDamagePlayer(PlayerEntity player) {
			contextBuilder.parameter(LootContextParameters.LAST_DAMAGE_PLAYER, player);
			return this;
		}
		
		public LootTableBuilder withDamageSource(DamageSource damageSource) {
			contextBuilder.parameter(LootContextParameters.DAMAGE_SOURCE, damageSource);
			return this;
		}
		
		public LootTableBuilder withKillerEntity(Entity killerEntity) {
			contextBuilder.parameter(LootContextParameters.KILLER_ENTITY, killerEntity);
			return this;
		}
		
		public LootTableBuilder withDirectKillerEntity(Entity directKillerEntity) {
			contextBuilder.parameter(LootContextParameters.DIRECT_KILLER_ENTITY, directKillerEntity);
			return this;
		}
		
		public LootTableBuilder withOrigin(Vec3d origin) {
			contextBuilder.parameter(LootContextParameters.ORIGIN, origin);
			return this;
		}
		
		public LootTableBuilder withBlockState(BlockState blockState) {
			contextBuilder.parameter(LootContextParameters.BLOCK_STATE, blockState);
			return this;
		}
		
		public LootTableBuilder withBlockEntity(BlockEntity blockEntity) {
			contextBuilder.parameter(LootContextParameters.BLOCK_ENTITY, blockEntity);
			return this;
		}
		
		public LootTableBuilder withTool(ItemStack tool) {
			contextBuilder.parameter(LootContextParameters.TOOL, tool);
			return this;
		}
		
		public LootTableBuilder withExplosionRadius(float explosionRadius) {
			contextBuilder.parameter(LootContextParameters.EXPLOSION_RADIUS, explosionRadius);
			return this;
		}
		
		public LootContext build() {
			return contextBuilder.build(LootContextTypes.CHEST);
		}
	}
	
	public static LootTableBuilder builder(World world) {
		return new LootTableBuilder(world);
	}
}
