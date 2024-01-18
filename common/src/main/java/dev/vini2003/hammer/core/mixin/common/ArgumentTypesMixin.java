package dev.vini2003.hammer.core.mixin.common;

import com.mojang.brigadier.arguments.ArgumentType;
import dev.vini2003.hammer.core.api.common.command.argument.ColorArgumentType;
import dev.vini2003.hammer.core.api.common.command.argument.PositionArgumentType;
import dev.vini2003.hammer.core.api.common.command.argument.SizeArgumentType;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArgumentTypes.class)
public abstract class ArgumentTypesMixin {
	@Shadow
	private static <A extends ArgumentType<?>, T extends ArgumentSerializer.ArgumentTypeProperties<A>> ArgumentSerializer<A, T> register(Registry<ArgumentSerializer<?, ?>> registry, String id, Class<? extends A> clazz, ArgumentSerializer<A, T> serializer) {
		return null;
	}
	
	@Inject(at = @At("HEAD"), method = "register(Lnet/minecraft/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;")
	private static void hammer$register(Registry<ArgumentSerializer<?, ?>> registry, CallbackInfoReturnable<ArgumentSerializer<?, ?>> cir) {
 		register(Registries.COMMAND_ARGUMENT_TYPE, "hammer:color", ColorArgumentType.class, ConstantArgumentSerializer.of(ColorArgumentType::color));
		register(Registries.COMMAND_ARGUMENT_TYPE, "hammer:position", PositionArgumentType.class, ConstantArgumentSerializer.of(PositionArgumentType::position));
		register(Registries.COMMAND_ARGUMENT_TYPE, "hammer:size", SizeArgumentType.class, ConstantArgumentSerializer.of(SizeArgumentType::size));
	}
	
}
