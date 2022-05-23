package dev.vini2003.hammer.core.api.common.util;

import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.text.WordUtils;

public class TextUtil {
	public static final Text EMPTY = toLiteralText("text.hammer.empty").formatted(Formatting.GRAY);
	
	public static float getWidth(Text text) {
		return DrawingUtil.getTextRenderer().getWidth(text);
	}
	
	public static float getWidth(String string) {
		return DrawingUtil.getTextRenderer().getWidth(string);
	}
	
	public static float getHeight(Text text) {
		return 9.0F;
	}
	
	public static float getHeight(String string) {
		return 9.0F;
	}
	
	public static LiteralText toLiteralText(String string) {
		return new LiteralText(string);
	}
	
	public static TranslatableText toTranslatableText(String string) {
		return new TranslatableText(string);
	}
	
	public static Text getPercentage(Number a, Number b) {
		return toLiteralText((int) ((float) a / (float) b * 100.0F) + "%").formatted(Formatting.GRAY);
	}
	
	public static MutableText getModId(Identifier id) {
		var loader = FabricLoader.getInstance();
		var optionalMod = loader.getModContainer(id.getNamespace());
		
		if (optionalMod.isPresent()) {
			var mod = optionalMod.get();
			
			return toLiteralText(mod.getMetadata().getName()).formatted(Formatting.BLUE, Formatting.ITALIC);
		} else {
			return toLiteralText(WordUtils.capitalize(id.getNamespace().replace("_", " ").replace("-", " "))).formatted(Formatting.BLUE, Formatting.ITALIC);
		}
	}
}
