package dev.vini2003.hammer.example.impl.common.screen.handler

import dev.vini2003.hammer.core.api.common.math.position.Position
import dev.vini2003.hammer.core.api.common.math.size.Size
import dev.vini2003.hammer.core.api.common.util.extension.toLiteralText
import dev.vini2003.hammer.example.registry.common.HEScreenHandlers
import dev.vini2003.hammer.gui.api.common.screen.handler.BaseScreenHandler
import dev.vini2003.hammer.gui.api.common.widget.bar.FluidBarWidget
import dev.vini2003.hammer.gui.api.common.widget.bar.TextureBarWidget
import dev.vini2003.hammer.gui.api.common.widget.button.ButtonWidget
import dev.vini2003.hammer.gui.api.common.widget.panel.PanelWidget
import dev.vini2003.hammer.gui.api.common.widget.tab.TabWidget
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType

class DebugScreenHandler(syncId: Int, player: PlayerEntity) : BaseScreenHandler(HEScreenHandlers.DEBUG, syncId, player) {
	override fun initialize(width: Int, height: Int) {
		val panel = PanelWidget()
		panel.position = Position(width / 2 - 88.5F, height / 2 - 95.0F)
		panel.size = Size(93.0F + 84.0F, 100.0F + 84.0F)
		
		add(panel)
		
		val button = ButtonWidget()
		button.clickAction = { player.sendMessage("Hey! You clicked me!".toLiteralText(), false) }
		
		button.position = Position(panel, 8.0F, 8.0F)
		button.size = Size(36.0F, 24.0F)
		
		panel.add(button)
		
		val tabs = TabWidget()
		tabs.position = Position(panel)
		
		tabs.addTab(Items.IRON_INGOT)
		tabs.addTab(Items.GOLD_INGOT)
		
		val textureBar = TextureBarWidget()
		textureBar.position = Position(panel, 8.0F, 24.0F)
		textureBar.size = Size(24.0F, 72.0F)
		
		textureBar.current = { 50.0F }
		textureBar.maximum = { 100.0F }
		
		add(textureBar)
		
		add(tabs)
		
	}
	
	override fun canUse(player: PlayerEntity?): Boolean {
		return true
	}
}