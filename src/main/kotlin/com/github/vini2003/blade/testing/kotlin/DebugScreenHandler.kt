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
		val panel = PanelWidget()
		panel.position = Position.of(48, 48)
		panel.size = Size.of(96, 96)

		addWidget(panel)

		val button = ButtonWidget {
			println("What the fuck?")
		}
		button.position = Position.of(48, 48)
		button.size = Size.of(18, 18)

		addWidget(button)
	}

	override fun canUse(player: PlayerEntity?): Boolean = true
}