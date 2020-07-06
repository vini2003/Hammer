package dev.vini2003.hammer.interaction.common.interaction

import dev.vini2003.hammer.common.util.serializer.TagKeySerializer
import dev.vini2003.hammer.common.util.serializer.UnsupportedSerializer
import kotlinx.serialization.Serializable
import net.minecraft.tag.TagKey

@Serializable
data class InteractionRule<out T>(
	val type: InteractionType,
	val mode: InteractionMode,
	val tagKey: @Serializable(with = TagKeySerializer::class) TagKey<@Serializable(with = UnsupportedSerializer::class) Any>
) {
	// Ignore is used to differentiate the two constructors, due to having the same JVM signature after type erasure.
	constructor(type: InteractionType, mode: InteractionMode, tagKey: TagKey<T>, ignore: Byte = 0) : this(type, mode, tagKey as TagKey<Any>)
}