/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.hammer.permission.mixin.common;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.vini2003.hammer.permission.api.common.manager.RoleManager;
import dev.vini2003.hammer.permission.impl.common.accessor.EntitySelectorReaderAccessor;
import net.minecraft.command.EntitySelectorOptions;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(EntitySelectorOptions.class)
public abstract class EntitySelectorOptionsMixin {
	private static final DynamicCommandExceptionType INVALID_ROLE_EXCEPTION = new DynamicCommandExceptionType(option -> Text.translatable("argument.entity.options.role.invalid", option));
	
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
					return player.hammer$hasRole(role) != negationCharacter;
				} else {
					return false;
				}
			});
		}, reader -> !((EntitySelectorReaderAccessor) reader).selectsRole(), Text.translatable("argument.entity.options.role.description"));
	}
}
