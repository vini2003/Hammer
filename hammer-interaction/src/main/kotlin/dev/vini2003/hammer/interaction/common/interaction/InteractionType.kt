package dev.vini2003.hammer.interaction.common.interaction

import kotlinx.serialization.Serializable

@Serializable
enum class InteractionType {
	ITEM_USE,
	
	BLOCK_BREAK,
	BLOCK_PLACE,
	BLOCK_USE,
	BLOCK_ATTACK,
	
	ENTITY_USE,
	ENTITY_ATTACK;
}