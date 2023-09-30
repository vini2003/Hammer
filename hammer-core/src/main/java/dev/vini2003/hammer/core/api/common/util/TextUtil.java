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

package dev.vini2003.hammer.core.api.common.util;

import dev.vini2003.hammer.core.api.client.util.DrawingUtil;
import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.text.WordUtils;

public class TextUtil {
	public static MutableText getEmpty() {
		return Text.translatable("text.hammer.empty").formatted(Formatting.GRAY);
	}
	
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
	
	public static Text getPercentage(Number a, Number b) {
		return Text.literal((int) (a.floatValue() / b.floatValue() * 100.0F) + "%");
	}
	
	public static MutableText getModId(Identifier id) {
		var loader = FabricLoader.getInstance();
		var optionalMod = loader.getModContainer(id.getNamespace());
		
		if (optionalMod.isPresent()) {
			var mod = optionalMod.get();
			
			return Text.literal(mod.getMetadata().getName()).formatted(Formatting.BLUE, Formatting.ITALIC);
		} else {
			return Text.literal(WordUtils.capitalize(id.getNamespace().replace("_", " ").replace("-", " "))).formatted(Formatting.BLUE, Formatting.ITALIC);
		}
	}
}
