package dev.vini2003.hammer.core.api.common.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LootTableUtil {
	public static class LootTableBuilder {
		private final LootContextParameterSet.Builder contextBuilder;
		
		public LootTableBuilder(World world) {
			contextBuilder = new LootContextParameterSet.Builder((ServerWorld) world);
		}
		
		public LootTableBuilder withThisEntity(Entity entity) {
			contextBuilder.add(LootContextParameters.THIS_ENTITY, entity);
			return this;
		}
		
		public LootTableBuilder withLastDamagePlayer(PlayerEntity player) {
			contextBuilder.add(LootContextParameters.LAST_DAMAGE_PLAYER, player);
			return this;
		}
		
		public LootTableBuilder withDamageSource(DamageSource damageSource) {
			contextBuilder.add(LootContextParameters.DAMAGE_SOURCE, damageSource);
			return this;
		}
		
		public LootTableBuilder withKillerEntity(Entity killerEntity) {
			contextBuilder.add(LootContextParameters.KILLER_ENTITY, killerEntity);
			return this;
		}
		
		public LootTableBuilder withDirectKillerEntity(Entity directKillerEntity) {
			contextBuilder.add(LootContextParameters.DIRECT_KILLER_ENTITY, directKillerEntity);
			return this;
		}
		
		public LootTableBuilder withOrigin(Vec3d origin) {
			contextBuilder.add(LootContextParameters.ORIGIN, origin);
			return this;
		}
		
		public LootTableBuilder withBlockState(BlockState blockState) {
			contextBuilder.add(LootContextParameters.BLOCK_STATE, blockState);
			return this;
		}
		
		public LootTableBuilder withBlockEntity(BlockEntity blockEntity) {
			contextBuilder.add(LootContextParameters.BLOCK_ENTITY, blockEntity);
			return this;
		}
		
		public LootTableBuilder withTool(ItemStack tool) {
			contextBuilder.add(LootContextParameters.TOOL, tool);
			return this;
		}
		
		public LootTableBuilder withExplosionRadius(float explosionRadius) {
			contextBuilder.add(LootContextParameters.EXPLOSION_RADIUS, explosionRadius);
			return this;
		}
		
		public LootContext build() {
			return new LootContext.Builder(contextBuilder.build(LootContextTypes.CHEST)).build(null);
		}
	}
	
	public static LootTableBuilder builder(World world) {
		return new LootTableBuilder(world);
	}
}
