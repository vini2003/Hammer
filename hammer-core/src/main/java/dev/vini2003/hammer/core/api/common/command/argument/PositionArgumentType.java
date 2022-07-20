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
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class PositionArgumentType implements ArgumentType<PositionArgumentType> {
	private static final Collection<String> EXAMPLES = Arrays.asList("0.0 0.0 0.0");
	
	private static final SimpleCommandExceptionType INCOMPLETE_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("text.hammer.position.incomplete"));
	private static final SimpleCommandExceptionType INVALID_POSITION_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("text.hammer.position.invalid"));
	
	private float x = 0.0F;
	private float y = 0.0F;
	private float z = 0.0F;
	
	private boolean relativeX = false;
	private boolean relativeY = false;
	private boolean relativeZ = false;
	
	public PositionArgumentType(float x, float y, float z, boolean relativeX, boolean relativeY, boolean relativeZ) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.relativeZ = relativeZ;
	}
	
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
		
		return (c >= '0' && c <= '9') || c == '~' || c == '^';
	}
	
	
	public static PositionArgumentType position() {
		return new PositionArgumentType(0.0F, 0.0F, 0.0F, false, false, false);
	}
	
	public static Position getPosition(CommandContext<ServerCommandSource> context, String name) {
		var posArg = context.getArgument(name, PositionArgumentType.class);
		var pos = new Position(posArg.x, posArg.y, posArg.z);
		
		if (posArg.relativeX) {
			pos = pos.offset((float) context.getSource().getPosition().getX(), 0.0F, 0.0F);
		}
		
		if (posArg.relativeY) {
			pos = pos.offset(0.0F, (float) context.getSource().getPosition().getY(), 0.0F);
		}
		
		if (posArg.relativeZ) {
			pos = pos.offset(0.0F, 0.0F, (float) context.getSource().getPosition().getZ());
		}
		
		return pos;
	}
	
	@Override
	public PositionArgumentType parse(StringReader reader) throws CommandSyntaxException {
		var cursor = reader.getCursor();
		
		var b = new boolean[2];
		
		b[0] = false;
		b[1] = false;
		
		while (reader.canRead() && isCharValid(reader.peek(), b)) {
			reader.skip();
		}
		
		reader.skip();
		
		var xString = reader.getString().substring(cursor, reader.getCursor());
		
		if (xString.startsWith("~")) {
			cursor += 1;
			
			xString = xString.substring(1);
			
			if (!xString.isEmpty() && xString.charAt(0) != ' ' && xString.charAt(0) != '.' && xString.charAt(0) != ',' && (xString.charAt(0) >= '0' && xString.charAt(0) <= '9')) {
				x = Float.parseFloat(xString);
			}
			
			relativeX = true;
		} else {
			x = Float.parseFloat(reader.getString().substring(cursor, reader.getCursor()));
		}
		
		cursor = reader.getCursor();
		
		if (!reader.canRead()) {
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		}
		
		b[0] = false;
		b[1] = false;
		
		while (reader.canRead() && isCharValid(reader.peek(), b)) {
			reader.skip();
		}
		
		reader.skip();
		
		var yString = reader.getString().substring(cursor, reader.getCursor());
		
		if (yString.startsWith("~")) {
			cursor += 1;
			
			yString = yString.substring(1);
			
			if (!yString.isEmpty() && yString.charAt(0) != ' ' && yString.charAt(0) != '.' && yString.charAt(0) != ',' && (yString.charAt(0) >= '0' && yString.charAt(0) <= '9')) {
				y = Float.parseFloat(yString);
			}
			
			relativeY = true;
		} else {
			y = Float.parseFloat(reader.getString().substring(cursor, reader.getCursor()));
		}
		
		cursor = reader.getCursor();
		
		if (!reader.canRead()) {
			throw INCOMPLETE_EXCEPTION.createWithContext(reader);
		}
		
		b[0] = false;
		b[1] = false;
		
		while (reader.canRead() && isCharValid(reader.peek(), b)) {
			reader.skip();
		}
		
		var zString = reader.getString().substring(cursor, reader.getCursor());
		
		if (zString.startsWith("~")) {
			cursor += 1;
			
			zString = zString.substring(1);
			
			if (!zString.isEmpty() && zString.charAt(0) != ' ' && zString.charAt(0) != '.' && zString.charAt(0) != ',' && (zString.charAt(0) >= '0' && zString.charAt(0) <= '9')) {
				z = Float.parseFloat(zString);
			}
			
			relativeZ = true;
		} else {
			z = Float.parseFloat(reader.getString().substring(cursor, reader.getCursor()));
		}
		
		cursor = reader.getCursor();
		
		try {
			return new PositionArgumentType(x, y, z, relativeX, relativeY, relativeZ);
		} catch (NumberFormatException exception) {
			reader.setCursor(cursor);
			
			throw INVALID_POSITION_EXCEPTION.createWithContext(reader);
		}
	}
	
	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		if (context.getSource() instanceof CommandSource) {
			var remaining = builder.getRemaining();
			var suggestions = !remaining.isEmpty() && remaining.charAt(0) == '^' ? Collections.singleton(CommandSource.RelativePosition.ZERO_LOCAL) : ((CommandSource) context.getSource()).getPositionSuggestions();
			
			return CommandSource.suggestPositions(remaining, suggestions, builder, CommandManager.getCommandValidator(this::parse));
		}
		
		return Suggestions.empty();
	}
	
	@Override
	public Collection<String> getExamples() {
		return EXAMPLES;
	}
}
