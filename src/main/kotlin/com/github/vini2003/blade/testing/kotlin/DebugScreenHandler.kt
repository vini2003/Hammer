package com.github.vini2003.blade.testing.kotlin

import com.github.vini2003.blade.common.data.Position
import com.github.vini2003.blade.common.data.Size
import com.github.vini2003.blade.common.data.Slots
import com.github.vini2003.blade.common.handler.BaseScreenHandler
import com.github.vini2003.blade.common.widget.base.*
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.registry.Registry

class DebugScreenHandler(syncId: Int, player: PlayerEntity) : BaseScreenHandler(DebugContainers.DEBUG_HANDLER, syncId, player) {
	override fun initialize(width: Int, height: Int) {
		val panelA = PanelWidget()
		panelA.position = Position.of(48, 48)
		panelA.size = Size.of(96, 96)

		addWidget(panelA)

		val panelB = PanelWidget()
		panelB.position = Position.of(48, 48)
		panelB.size = Size.of(80, 96)

		panelA.addWidget(panelB)

		val panelC = PanelWidget()
		panelC.position = Position.of(48, 48)
		panelC.size = Size.of(64, 96)

		panelA.addWidget(panelC)

	}

	override fun canUse(player: PlayerEntity?): Boolean = true
}