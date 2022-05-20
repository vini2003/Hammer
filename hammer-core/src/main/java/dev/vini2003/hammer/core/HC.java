package dev.vini2003.hammer.core;

import net.minecraft.util.Identifier;

public class HC {
	public static final String ID = "hammer";
	
	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}
}
