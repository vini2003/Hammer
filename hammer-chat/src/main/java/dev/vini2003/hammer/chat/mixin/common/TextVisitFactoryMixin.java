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

package dev.vini2003.hammer.chat.mixin.common;

import net.minecraft.client.font.TextVisitFactory;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TextVisitFactory.class)
public class TextVisitFactoryMixin {
	private static final ThreadLocal<Boolean> HAMMER$IS_HEX_CODE = ThreadLocal.withInitial(() -> false);
	
	private static final ThreadLocal<Integer> HAMMER$HEX_CODE = ThreadLocal.withInitial(() -> Integer.MIN_VALUE);
	
	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/String;charAt(I)C", ordinal = 0, shift = At.Shift.AFTER), method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private static void hammer$visitFormatted$charAt$1(String text, int startIndex, Style startingStyle, Style resetStyle, CharacterVisitor visitor, CallbackInfoReturnable<Boolean> cir, int i, Style style, int j, char c) {
		if (c == '§' && j + 7 <= i && text.charAt(j + 1) == '#') {
			try {
				var hex = Integer.parseInt(text.substring(j + 2, j + 8), 16);
				
				HAMMER$IS_HEX_CODE.set(true);
				
				HAMMER$HEX_CODE.set(hex);
			} catch (Exception ignored) {
			}
		}
	}
	
	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/String;charAt(I)C", ordinal = 1, shift = At.Shift.AFTER), method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", ordinal = 2)
	private static int hamemr$visitFormatted$charAt$2(int original) {
		if (HAMMER$IS_HEX_CODE.get()) {
			return original + 6;
		}
		
		return original;
	}
	
	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/Formatting;byCode(C)Lnet/minecraft/util/Formatting;", shift = At.Shift.AFTER), method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", ordinal = 0)
	private static Formatting hamemr$visitFormatted$charAt$3(Formatting original) {
		if (HAMMER$IS_HEX_CODE.get()) {
			return null;
		}
		
		return original;
	}
	
	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/Formatting;byCode(C)Lnet/minecraft/util/Formatting;", shift = At.Shift.AFTER), method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", ordinal = 2)
	private static Style hamemr$visitFormatted$charAt$4(Style original) {
		if (HAMMER$IS_HEX_CODE.get()) {
			var style = original.withColor(HAMMER$HEX_CODE.get());
			
			HAMMER$IS_HEX_CODE.set(false);
			
			return style;
		}
		
		return original;
	}
}
