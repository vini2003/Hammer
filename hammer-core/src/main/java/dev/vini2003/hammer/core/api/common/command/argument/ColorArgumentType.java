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

package dev.vini2003.hammer.core.api.common.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.vini2003.hammer.core.api.client.color.Color;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Collection;

public class ColorArgumentType implements ArgumentType<Color> {
	private static final Collection<String> EXAMPLES = Arrays.asList("#7E337EAE");
	
	private static final DynamicCommandExceptionType INVALID_COLOR_EXCEPTION = new DynamicCommandExceptionType(id -> Text.translatable("text.hammer.color.invalid", id));
	
	private static boolean isCharValid(char c, boolean[] b) {
		if (!b[1]) {
			b[0] = c == '#';
			b[1] = true;
			
			if (b[0]) {
				return true;
			}
		}
		
		return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
	}
	
	public static ColorArgumentType color() {
		return new ColorArgumentType();
	}
	
	public static Color getColor(CommandContext<ServerCommandSource> context, String name) {
		return context.getArgument(name, Color.class);
	}
	
	@Override
	public Color parse(StringReader reader) throws CommandSyntaxException {
		var cursor = reader.getCursor();
		
		var b = new boolean[] { false, false };
		
		while (reader.canRead() && isCharValid(reader.peek(), b)) {
			reader.skip();
		}
		
		var input = reader.getString().substring(cursor, reader.getCursor());
		
		try {
			return new Color(input.replace("#", ""));
		} catch (NumberFormatException exception) {
			reader.setCursor(cursor);
			
			throw INVALID_COLOR_EXCEPTION.createWithContext(reader, input);
		}
	}
	
	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
