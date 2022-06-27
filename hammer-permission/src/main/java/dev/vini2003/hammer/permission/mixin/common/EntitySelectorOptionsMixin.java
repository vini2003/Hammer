package dev.vini2003.hammer.permission.mixin.common;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import dev.vini2003.hammer.permission.impl.common.accessor.EntitySelectorReaderAccessor;
import net.minecraft.command.EntitySelectorOptions;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(EntitySelectorOptions.class)
public abstract class EntitySelectorOptionsMixin {
	private static final DynamicCommandExceptionType INVALID_ROLE_EXCEPTION = new DynamicCommandExceptionType(option -> new TranslatableText("argument.entity.options.role.invalid", option));
	
	@Shadow
	private static void putOption(String id, EntitySelectorOptions.SelectorHandler handler, Predicate<EntitySelectorReader> condition, Text description) {
		throw new AssertionError("");
	}
	
	@Inject(at = @At("RETURN"), method = "register")
	private static void hammer$register(CallbackInfo ci) {
		putOption("role", reader -> {
			var cursor = reader.getReader().getCursor();
			var negationCharacter = reader.readNegationCharacter();
			var roleName = reader.getReader().readString();
			var role = RoleManager.getRoleByName(roleName);
			
			if (role == null) {
				throw INVALID_ROLE_EXCEPTION.createWithContext(reader.getReader(), "role");
			}
			
			if (negationCharacter) {
				((EntitySelectorReaderAccessor) reader).setExcludesRole(true);
			} else {
				((EntitySelectorReaderAccessor) reader).setSelectsRole(true);
			}
			
			reader.setPredicate(entity -> {
				if (entity instanceof PlayerEntity player) {
					return role.isIn(player) != negationCharacter;
				} else {
					return false;
				}
			});
		}, reader -> !((EntitySelectorReaderAccessor) reader).selectsRole(), new TranslatableText("argument.entity.options.role.description"));
	}
}
