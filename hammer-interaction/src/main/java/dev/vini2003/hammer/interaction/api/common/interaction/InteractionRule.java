package dev.vini2003.hammer.interaction.api.common.interaction;

import net.minecraft.tag.TagKey;

public class InteractionRule<T> {
	private final InteractionType type;
	private final InteractionMode mode;
	
	private final TagKey<T> tag;
	
	public InteractionRule(InteractionType type, InteractionMode mode, TagKey<T> tag) {
		this.type = type;
		this.mode = mode;
		
		this.tag = tag;
	}
	
	public InteractionType getType() {
		return type;
	}
	
	public InteractionMode getMode() {
		return mode;
	}
	
	public TagKey<T> getTag() {
		return tag;
	}
}
