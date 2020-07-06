package dev.vini2003.hammer.common.util

import net.minecraft.loot.LootTable
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier

object LootUtils {
	@JvmStatic
	fun getTable(world: ServerWorld, id: Identifier): LootTable {
		return world.server.lootManager.getTable(id)
	}
}