package dev.vini2003.hammer.core.api.common.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import dev.vini2003.hammer.core.api.client.color.Color;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.Arrays;
import java.util.Collection;

public class ColorArgumentType implements ArgumentType<Color> {
	private static final Collection<String> EXAMPLES = Arrays.asList("#7E337EAE");
	
	private static final DynamicCommandExceptionType INVALID_COLOR_EXCEPTION = new DynamicCommandExceptionType(id -> new TranslatableText("text.hammer.color.invalid", id));
	
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
	
	@Override
	public Color parse(StringReader reader) throws CommandSyntaxException {
		var cursor = reader.getCursor();
		
		var foundHashTag = false;
		
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
