package dev.vini2003.hammer.example

import dev.vini2003.hammer.core.HC
import dev.vini2003.hammer.core.api.common.util.serializer.NbtCompoundSerializer
import dev.vini2003.hammer.core.api.common.util.serializer.format.Nbt
import dev.vini2003.hammer.example.registry.common.HECommands
import dev.vini2003.hammer.example.registry.common.HEScreenHandlers
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import kotlinx.serialization.decodeFromByteArray
import net.fabricmc.api.ModInitializer
import net.minecraft.nbt.NbtCompound
import java.util.*


object HE : ModInitializer {
	override fun onInitialize() {
		HECommands.init()
		HEScreenHandlers.init()
	}
}