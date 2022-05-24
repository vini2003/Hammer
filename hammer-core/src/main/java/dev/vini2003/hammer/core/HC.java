package dev.vini2003.hammer.core;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HC {
	public static final String ID = "hammer";
	
	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}
}
