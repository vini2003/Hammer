package dev.vini2003.hammer.core.registry.common;

import dev.vini2003.hammer.core.api.common.command.argument.ColorArgumentType;
import dev.vini2003.hammer.core.api.common.command.argument.PositionArgumentType;
import dev.vini2003.hammer.core.api.common.command.argument.SizeArgumentType;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.util.registry.Registry;

public class HCArgumentTypes {
	public static void init() {
		ArgumentTypes.register(Registry.COMMAND_ARGUMENT_TYPE, "hammer:color", ColorArgumentType.class, ConstantArgumentSerializer.of(ColorArgumentType::color));
		ArgumentTypes.register(Registry.COMMAND_ARGUMENT_TYPE, "hammer:position", PositionArgumentType.class, ConstantArgumentSerializer.of(PositionArgumentType::position));
		ArgumentTypes.register(Registry.COMMAND_ARGUMENT_TYPE, "hammer:size", SizeArgumentType.class, ConstantArgumentSerializer.of(SizeArgumentType::size));
	}
}
