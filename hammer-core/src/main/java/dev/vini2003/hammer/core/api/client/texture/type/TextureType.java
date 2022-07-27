package dev.vini2003.hammer.core.api.client.texture.type;

import dev.vini2003.hammer.core.HC;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum TextureType {
	FLUID(HC.id("fluid")),
	IMAGE(HC.id("image")),
	SPRITE(HC.id("sprite")),
	PARTITIONED(HC.id("partitioned")),
	TILED_FLUID(HC.id("tiled_fluid")),
	TILED_IMAGE(HC.id("tiled_image")),
	TILED_SPRITE(HC.id("tiled_sprite"));
	
	public static final Map<Identifier, TextureType> TYPES = new HashMap<>();
	
	private final Identifier id;
	
	TextureType(Identifier id) {
		this.id = id;
	}
	
	public Identifier getId() {
		return id;
	}
	
	public static TextureType getById(Identifier id) {
		return TYPES.get(id);
	}
	
	static {
		for (var value : values()) {
			TYPES.put(value.getId(), value);
		}
	}
}
