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
	private static ThreadLocal<Boolean> hammer$isHexCode = ThreadLocal.withInitial(() -> false);
	
	private static ThreadLocal<Integer> hammer$hexCode = ThreadLocal.withInitial(() -> Integer.MIN_VALUE);
	
	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/String;charAt(I)C", ordinal = 0, shift = At.Shift.AFTER), method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private static void hammer$visitFormatted$charAt$1(String text, int startIndex, Style startingStyle, Style resetStyle, CharacterVisitor visitor, CallbackInfoReturnable<Boolean> cir, int i, Style style, int j, char c) {
		if (c == '§' && j + 7 <= i && text.charAt(j + 1) == '#') {
			try {
				var hex = Integer.parseInt(text.substring(j + 2, j + 8), 16);
				
				hammer$isHexCode.set(true);
				
				hammer$hexCode.set(hex);
			} catch (Exception ignored) {
			}
		}
	}
	
	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/String;charAt(I)C", ordinal = 1, shift = At.Shift.AFTER), method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", ordinal = 2)
	private static int hamemr$visitFormatted$charAt$2(int original) {
		if (hammer$isHexCode.get()) {
			return original + 6;
		}
		
		return original;
	}
	
	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/Formatting;byCode(C)Lnet/minecraft/util/Formatting;", shift = At.Shift.AFTER), method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", ordinal = 0)
	private static Formatting hamemr$visitFormatted$charAt$3(Formatting original) {
		if (hammer$isHexCode.get()) {
			return null;
		}
		
		return original;
	}
	
	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/Formatting;byCode(C)Lnet/minecraft/util/Formatting;", shift = At.Shift.AFTER), method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z", ordinal = 2)
	private static Style hamemr$visitFormatted$charAt$4(Style original) {
		if (hammer$isHexCode.get()) {
			var style = original.withColor(hammer$hexCode.get());
			
			hammer$isHexCode.set(false);
			
			return style;
		}
		
		return original;
	}
}
