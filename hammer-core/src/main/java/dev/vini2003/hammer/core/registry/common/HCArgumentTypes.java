package dev.vini2003.hammer.core.registry.common;

import dev.vini2003.hammer.core.api.common.command.argument.ColorArgumentType;
import dev.vini2003.hammer.core.api.common.command.argument.PositionArgumentType;
import dev.vini2003.hammer.core.api.common.command.argument.SizeArgumentType;
import dev.vini2003.hammer.core.api.common.manager.CommandManager;
import net.minecraft.command.argument.*;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.registry.Registry;

public class HCArgumentTypes {
	public static void init() {
		ArgumentTypes.register(Registry.COMMAND_ARGUMENT_TYPE, "hammer:color", ColorArgumentType.class, ConstantArgumentSerializer.of(ColorArgumentType::color));
		ArgumentTypes.register(Registry.COMMAND_ARGUMENT_TYPE, "hammer:position", PositionArgumentType.class, ConstantArgumentSerializer.of(PositionArgumentType::position));
		ArgumentTypes.register(Registry.COMMAND_ARGUMENT_TYPE, "hammer:size", SizeArgumentType.class, ConstantArgumentSerializer.of(SizeArgumentType::size));
		
		CommandManager.registerArgumentType(PositionArgumentType.class, PositionArgumentType::position);
		
		CommandManager.registerArgumentType(Vec3ArgumentType.class, () -> Vec3ArgumentType.vec3());
		CommandManager.registerArgumentType(Vec2ArgumentType.class, () -> Vec2ArgumentType.vec2());
		CommandManager.registerArgumentType(BlockStateArgumentType.class, BlockStateArgumentType::blockState);
		CommandManager.registerArgumentType(BlockPredicateArgumentType.class, BlockPredicateArgumentType::blockPredicate);
		CommandManager.registerArgumentType(ItemStackArgumentType.class, ItemStackArgumentType::itemStack);
		CommandManager.registerArgumentType(ItemPredicateArgumentType.class, ItemPredicateArgumentType::itemPredicate);
		CommandManager.registerArgumentType(ScoreHolderArgumentType.class, ScoreHolderArgumentType::scoreHolder);
		
		
		CommandManager.registerArgumentTypeGetter(ColorArgumentType.class, ColorArgumentType::getColor);
		CommandManager.registerArgumentTypeGetter(PositionArgumentType.class, PositionArgumentType::getPosition);
		CommandManager.registerArgumentTypeGetter(SizeArgumentType.class, SizeArgumentType::getSize);
		
		CommandManager.registerArgumentTypeGetter(GameProfileArgumentType.class, (context, name) -> {
			try {
				return GameProfileArgumentType.getProfileArgument(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(BlockPosArgumentType.class, (context, name) -> {
			try {
				return BlockPosArgumentType.getBlockPos(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(ColumnPosArgumentType.class, (context, name) -> {
			try {
				return ColumnPosArgumentType.getColumnPos(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(Vec3ArgumentType.class, (context, name) -> {
			try {
				return Vec3ArgumentType.getVec3(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(Vec2ArgumentType.class, (context, name) -> {
			try {
				return Vec2ArgumentType.getVec2(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(BlockStateArgumentType.class, (context, name) -> {
			try {
				return BlockStateArgumentType.getBlockState(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(BlockPredicateArgumentType.class, (context, name) -> {
			try {
				return BlockPredicateArgumentType.getBlockPredicate(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(ItemPredicateArgumentType.class, (context, name) -> {
			try {
				return ItemPredicateArgumentType.getItemStackPredicate(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(ItemStackArgumentType.class, (context, name) -> {
			try {
				return ItemStackArgumentType.getItemStackArgument(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(MessageArgumentType.class, (context, name) -> {
			try {
				return MessageArgumentType.getMessage(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(ScoreboardObjectiveArgumentType.class, (context, name) -> {
			try {
				return ScoreboardObjectiveArgumentType.getObjective(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(RotationArgumentType.class, (context, name) -> {
			try {
				return RotationArgumentType.getRotation(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(SwizzleArgumentType.class, (context, name) -> {
			try {
				return SwizzleArgumentType.getSwizzle(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(CommandFunctionArgumentType.class, (context, name) -> {
			try {
				return CommandFunctionArgumentType.getFunctionOrTag(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(EntitySummonArgumentType.class, (context, name) -> {
			try {
				return EntitySummonArgumentType.getEntitySummon(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(DimensionArgumentType.class, (context, name) -> {
			try {
				return DimensionArgumentType.getDimensionArgument(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
		
		CommandManager.registerArgumentTypeGetter(ScoreHolderArgumentType.class, (context, name) -> {
			try {
				return ScoreHolderArgumentType.getScoreHolder(context, name);
			} catch (Exception ignored) {
				return null;
			}
		});
	}
}
