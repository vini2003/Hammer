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
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.vini2003.hammer.core.api.common.math.position.Position;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class PositionArgumentType implements ArgumentType<Position> {
	private static final Collection<String> EXAMPLES = Arrays.asList("0.0 0.0 0.0");
	
	private static final SimpleCommandExceptionType INCOMPLETE_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("text.hammer.position.incomplete"));
	private static final SimpleCommandExceptionType INVALID_POSITION_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("text.hammer.position.invalid"));
	
	private static boolean isCharValid(char c, boolean[] b) {
		if (!b[1]) {
			if (c == '.' || c == ',') {
				b[0] = true;
				b[1] = true;
			}
			
			if (b[0]) {
				return true;
			}
		}
		
		return (c >= '0' && c <= '9');
	}
	
	
	public static PositionArgumentType position() {
		return new PositionArgumentType();
	}
	
	public static Position getPosition(CommandContext<ServerCommandSource> context, String name) {
		return context.getArgument(name, Position.class);
	}
	
	@Override
	public Position parse(StringReader reader) throws CommandSyntaxException {
		var cursor = reader.getCursor();
		
		float x;
		float y;
		float z;
		
		var b = new boolean[2];
		
		b[0] = false;
		b[1] = false;
		
		while (reader.canRead() && isCharValid(reader.peek(), b)) {
			reader.skip();
		}
		
		x = Float.parseFloat(reader.getString().substring(cursor, reader.getCursor()));
		cursor = reader.getCursor();
		
		if (!reader.canRead()) {
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		}
		
		b[0] = false;
		b[1] = false;
		
		while (reader.canRead() && isCharValid(reader.peek(), b)) {
			reader.skip();
		}
		
		y = Float.parseFloat(reader.getString().substring(cursor, reader.getCursor()));
		cursor = reader.getCursor();
		
		if (!reader.canRead()) {
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		}
		
		b[0] = false;
		b[1] = false;
		
		while (reader.canRead() && isCharValid(reader.peek(), b)) {
			reader.skip();
		}
		
		z = Float.parseFloat(reader.getString().substring(cursor, reader.getCursor()));
		cursor = reader.getCursor();
		
		try {
			return new Position(x, y, z);
		} catch (NumberFormatException exception) {
			reader.setCursor(cursor);
			
			throw INVALID_POSITION_EXCEPTION.createWithContext(reader);
		}
	}
	
	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
